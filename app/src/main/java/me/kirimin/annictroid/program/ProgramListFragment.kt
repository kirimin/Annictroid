package me.kirimin.annictroid.program

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_program_list.view.*
import me.kirimin.annictroid.R
import me.kirimin.annictroid._common.networks.RetrofitClient
import me.kirimin.annictroid._common.networks.apis.AnnictService
import me.kirimin.annictroid._common.preferences.AppPreferences
import me.kirimin.annictroid._common.utils.ApiDateFormatter
import me.kirimin.annictroid.episode.EpisodeActivity
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.*

class ProgramListFragment : Fragment() {

    private val subscriptions = CompositeSubscription()
    private val repository = ProgramListRepository()
    private lateinit var adapter: ProgramListAdapter
    private var nextPage: Int? = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_program_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = ProgramListAdapter(context, onItemClick = {
            val intent = Intent(context, EpisodeActivity::class.java)
            intent.putExtras(EpisodeActivity.getBundle(it.episode.id, it.work.title))
            startActivity(intent)
        }, onChecked = {
            // アイテムをremoveしつつ裏で登録
            repository.postRecord(token = AppPreferences.getToken(context), episodeId = adapter.data[it].episode.id)
            adapter.data.removeAt(it)
            adapter.notifyItemRemoved(it)
            adapter.notifyDataSetChanged()
        })
        val view = view ?: return
        view.recyclerView.adapter = adapter
        view.recyclerView.layoutManager = LinearLayoutManager(context)
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
            nextPage = 1
            request()
        }
        request()
    }

    override fun onDestroy() {
        subscriptions.unsubscribe()
        super.onDestroy()
    }

    private fun request() {
        view?.swipeLayout?.isRefreshing = true
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        subscriptions.add(repository.requestPrograms(token = AppPreferences.getToken(context),
                filterStartedAt = ApiDateFormatter.getApiTime(calendar),
                filterUnWatched = "true",
                sortStartedAt = "desc",
                page = nextPage?.toString() ?: "")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    adapter.data.addAll(it.programs)
                    adapter.notifyDataSetChanged()
                    nextPage = it.next_page
                    view?.textViewEmpty?.visibility = if (adapter.data.isEmpty()) View.VISIBLE else View.GONE
                    view?.swipeLayout?.isRefreshing = false
                }, {
                    Toast.makeText(context, R.string.common_network_error, Toast.LENGTH_SHORT).show()
                    view?.swipeLayout?.isRefreshing = false
                }))
    }
}