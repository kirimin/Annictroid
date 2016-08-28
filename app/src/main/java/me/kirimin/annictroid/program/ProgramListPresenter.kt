package me.kirimin.annictroid.program

import android.view.View
import me.kirimin.annictroid._common.networks.entities.Program
import me.kirimin.annictroid._common.utils.ApiDateFormatter
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.*

class ProgramListPresenter {

    private val subscriptions = CompositeSubscription()
    private val repository = ProgramListRepository()
    private var nextPage: Int? = 1

    var view: ProgramListView? = null

    fun onCreate(view: ProgramListView) {
        this.view = view
        view.initRecyclerView()
        request()
    }

    fun onDestroy() {
        subscriptions.unsubscribe()
    }

    fun onScroll(dx: Int, dy: Int, totalItemCount: Int, lastVisibleItem: Int) {
        if (dx == 0 && dy == 0) return
        if (nextPage == null) return
        if (totalItemCount == lastVisibleItem + 1) {
            request()
            nextPage = null
        }
    }

    fun onRefresh() {
        view?.clearPrograms()
        nextPage = 1
        request()
    }

    fun onItemClick(program: Program) {
        view?.startEpisodeView(program.episode.id, program.work.title)
    }

    fun onChecked(position: Int, program: Program) {
        // アイテムをremoveしつつ裏で登録
        repository.postRecord(token = repository.getToken(), episodeId = program.episode.id)
        view?.removeProgram(position)
    }

    private fun request() {
        view?.isRefreshing(true)
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        subscriptions.add(repository.requestPrograms(token = repository.getToken(),
                filterStartedAt = ApiDateFormatter.getApiTime(calendar),
                filterUnWatched = "true",
                sortStartedAt = "desc",
                page = nextPage?.toString() ?: "")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val view = view ?: return@subscribe
                    view.addPrograms(it.programs)
                    nextPage = it.next_page
                    view.setEmptyTextVisibility(if (it.programs.isEmpty()) View.VISIBLE else View.GONE)
                    view.isRefreshing(false)
                }, {
                    val view = view ?: return@subscribe
                    view.showError()
                    view.isRefreshing(false)
                }))
    }
}