package me.kirimin.annictroid.myanime

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.kirimin.annictroid.R
import me.kirimin.annictroid._common.networks.RetrofitClient
import me.kirimin.annictroid._common.networks.apis.AnnictService
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

import kotlinx.android.synthetic.main.fragment_my_anime_list.view.*
import me.kirimin.annictroid._common.models.AnimeInfo
import me.kirimin.annictroid._common.preferences.AppPreferences
import me.kirimin.annictroid._common.ui_parts.DividerItemDecoration
import me.kirimin.annictroid.work.WorkActivity
import me.kirimin.annictroid.works.WorkListFragment
import rx.Observable
import rx.subscriptions.CompositeSubscription

class MyAnimeListFragment : Fragment() {

    enum class Type(val string: String) {
        WANNA_WATCH("wanna_watch"), WATCHING("watching"), WATCHED("watched"), ON_HOLD("on_hold"), STOP_WATCHING("stop_watching")
    }

    private val subscriptions = CompositeSubscription()
    private lateinit var adapter: MyAnimeListAdapter
    private var type = Type.WATCHING
    private var nextPage: Int? = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_my_anime_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        nextPage = 1
        type = arguments.getSerializable("type") as Type
        adapter = MyAnimeListAdapter(context, onItemClick = {
            val intent = Intent(context, WorkActivity::class.java)
            intent.putExtras(WorkActivity.getBundle(it.work.id, it.work.title))
            startActivity(intent)
        })
        val view = view ?: return
        view.recyclerView.adapter = adapter
        view.recyclerView.layoutManager = LinearLayoutManager(context)
        view.recyclerView.addItemDecoration(DividerItemDecoration(context))
        // 更読み用処理
        view.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dx == 0 && dy == 0) return
                if (nextPage == null) return
                val layoutManager = getView()?.recyclerView?.layoutManager as? LinearLayoutManager ?: return
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                if (totalItemCount == lastVisibleItem + 1) {
                    request()
                    nextPage = null
                }
            }
        })
        view.swipeLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent)
        view.swipeLayout.setProgressViewOffset(false, 0, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24f, resources.displayMetrics).toInt())
        view.swipeLayout.setOnRefreshListener {
            adapter.data.clear()
            adapter.notifyDataSetChanged()
            nextPage = 1
            request()
        }
        view.swipeLayout.isRefreshing = true
        request()
    }

    override fun onDestroy() {
        subscriptions.unsubscribe()
        super.onDestroy()
    }

    fun request() {
        view?.swipeLayout?.isRefreshing = true
        val token = AppPreferences.getToken()
        val client = RetrofitClient.default().build().create(AnnictService::class.java)
        var nextPageTmp: Int? = null
        subscriptions.add(client.meWorks(token = token,
                status = type.string,
                sortSeason = "desc",
                page = nextPage?.toString() ?: "")
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .flatMap { nextPageTmp = it.next_page; Observable.from(it.works) }
                // 並列処理のために
                .flatMap {
                    Observable.just(it)
                            .subscribeOn(Schedulers.computation())
                            .map {
                                if (type != Type.WATCHING) return@map AnimeInfo(it, null)
                                val recentProgram = client.recentProgram(token = token, workIds = it.id).execute()
                                val programs = recentProgram.body().programs
                                if (recentProgram.isSuccessful && !programs.isEmpty()) AnimeInfo(it, programs[0]) else AnimeInfo(it, null)
                            }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .subscribe ({
                    view?.swipeLayout?.isRefreshing = false
                    adapter.data.addAll(it)
                    adapter.notifyDataSetChanged()
                    nextPage = nextPageTmp
                }, {
                    view?.swipeLayout?.isRefreshing = false
                }))
    }

    companion object {

        fun newInstance(type: Type): MyAnimeListFragment {
            val fragment = MyAnimeListFragment()
            val bundle = Bundle()
            bundle.putSerializable("type", type)
            fragment.arguments = bundle
            return fragment
        }
    }
}
