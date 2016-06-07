package me.kirimin.annictroid.myanime

import android.os.Bundle
import android.support.v4.app.Fragment
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
import rx.Observable
import rx.subscriptions.CompositeSubscription

class MyAnimeListFragment : Fragment() {

    private val subscriptions = CompositeSubscription()
    private lateinit var adapter: MyAnimeListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_my_anime_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = MyAnimeListAdapter(context)
        val view = view ?: return
        view.listView.adapter = adapter
        view.swipeLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent)
        view.swipeLayout.setProgressViewOffset(false, 0, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24f, resources.displayMetrics).toInt())
        view.swipeLayout.setOnRefreshListener { request() }
        view.swipeLayout.isRefreshing = true
        request()
    }

    override fun onDestroy() {
        subscriptions.unsubscribe()
        super.onDestroy()
    }

    fun request() {
        val token = AppPreferences.getToken(context)
        val client = RetrofitClient.default().build().create(AnnictService::class.java)
        client.meWorks(token = token, status = "watching")
                .subscribeOn(Schedulers.newThread())
                .flatMap { Observable.from(it.works) }
                // 並列処理のために
                .flatMap {
                    Observable.just(it)
                            .subscribeOn(Schedulers.computation())
                            .map {
                                val recentProgram = client.recentProgram(token = token, workIds = it.id).execute()
                                if (recentProgram.isSuccessful) AnimeInfo(it, recentProgram.body().programs[0]) else AnimeInfo(it, null)
                            }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .subscribe ({
                    view?.swipeLayout?.isRefreshing = false
                    adapter.clear()
                    it.sortBy { it.recentProgram?.started_at }
                    adapter.addAll(it)
                }, {
                    view?.swipeLayout?.isRefreshing = false
                })
    }
}
