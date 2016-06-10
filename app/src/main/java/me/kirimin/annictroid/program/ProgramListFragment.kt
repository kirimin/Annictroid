package me.kirimin.annictroid.program

import android.os.Bundle
import android.support.v4.app.Fragment
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
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.text.SimpleDateFormat
import java.util.*

class ProgramListFragment : Fragment() {

    private val subscriptions = CompositeSubscription()
    private lateinit var adapter: ProgramListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_program_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = ProgramListAdapter(context)
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
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        subscriptions.add(RetrofitClient.default().build().create(AnnictService::class.java)
                .mePrograms(token = AppPreferences.getToken(context),
                        filterStartedAt = ApiDateFormatter.getApiTime(calendar),
                        filterUnWatched = "true",
                        sortStartedAt = "desc")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    adapter.clear()
                    adapter.addAll(it.programs)
                    view?.textViewEmpty?.visibility = if (adapter.isEmpty) View.VISIBLE else View.GONE
                    view?.swipeLayout?.isRefreshing = false
                }, {
                    Toast.makeText(context, "通信に失敗したようです。", Toast.LENGTH_SHORT).show()
                    view?.swipeLayout?.isRefreshing = false
                }))
    }
}