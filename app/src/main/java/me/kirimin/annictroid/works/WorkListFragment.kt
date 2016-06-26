package me.kirimin.annictroid.works

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_work_list.view.*
import me.kirimin.annictroid.R
import me.kirimin.annictroid._common.networks.RetrofitClient
import me.kirimin.annictroid._common.networks.apis.AnnictService
import me.kirimin.annictroid._common.preferences.AppPreferences
import me.kirimin.annictroid._common.ui_parts.DividerItemDecoration
import me.kirimin.annictroid._common.utils.ApiDateFormatter
import me.kirimin.annictroid.work.WorkActivity
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.*

class WorkListFragment : Fragment() {

    enum class Type {
        THIS_SEASON, NEXT_SEASON, ALL
    }

    private val subscriptions = CompositeSubscription()
    private lateinit var adapter: WorkListAdapter
    private var type = Type.THIS_SEASON
    private var nextPage: Int? = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_work_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        type = arguments.getSerializable("type") as Type
        adapter = WorkListAdapter(context, onItemClick = {
            val intent = Intent(context, WorkActivity::class.java)
            intent.putExtras(WorkActivity.getBundle(it.id, it.title))
            startActivity(intent)
        })
        val view = view ?: return
        view.recyclerView.adapter = adapter
        view.recyclerView.layoutManager = LinearLayoutManager(context)
        view.recyclerView.addItemDecoration(DividerItemDecoration(context))
        adapter.notifyDataSetChanged()
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

    fun request() {
        view?.swipeLayout?.isRefreshing = true
        val token = AppPreferences.getToken(context)
        val client = RetrofitClient.default().build().create(AnnictService::class.java)
        val season = when (type) {
            Type.THIS_SEASON -> ApiDateFormatter.getCurrentSeason(Calendar.getInstance())
            Type.NEXT_SEASON -> ApiDateFormatter.getNextSeason(Calendar.getInstance())
            Type.ALL -> ""
        }
        subscriptions.add(client.works(token = token, season = season, sortWatchers = "desc", page = nextPage?.toString() ?: "")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    view?.swipeLayout?.isRefreshing = false
                    adapter.data.addAll(it.works)
                    adapter.notifyDataSetChanged()
                    nextPage = it.next_page
                }, {
                    view?.swipeLayout?.isRefreshing = false
                }))
    }

    companion object {

        fun newInstance(type: Type): WorkListFragment {
            val fragment = WorkListFragment()
            val bundle = Bundle()
            bundle.putSerializable("type", type)
            fragment.arguments = bundle
            return fragment
        }
    }
}