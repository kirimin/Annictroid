package me.kirimin.annictroid.program

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.android.synthetic.main.fragment_program_list.*
import me.kirimin.annictroid.R
import me.kirimin.annictroid._common.networks.entities.Program
import me.kirimin.annictroid.episode.EpisodeActivity

interface ProgramListView {

    class ProgramListFragment : Fragment(), ProgramListView {

        private val presenter = ProgramListPresenter()
        private lateinit var adapter: ProgramListAdapter

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
            return inflater.inflate(R.layout.fragment_program_list, container, false)
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)
            presenter.onCreate(this)
        }

        override fun onDestroy() {
            presenter.onDestroy()
            super.onDestroy()
        }

        override fun isRefreshing(value: Boolean) {
            swipeLayout.isRefreshing = value
        }

        override fun initRecyclerView() {
            adapter = ProgramListAdapter(context, {
                presenter.onItemClick(it)
            }, { position, program ->
                presenter.onChecked(position, program)
            })
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.itemAnimator = SlideInLeftAnimator()
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                    presenter.onScroll(dx, dy, totalItemCount, lastVisibleItem)
                }
            })
            swipeLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent)
            swipeLayout.setProgressViewOffset(false, 0, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24f, resources.displayMetrics).toInt())
            swipeLayout.setOnRefreshListener { presenter.onRefresh() }
        }

        override fun startEpisodeView(id: String, title: String) {
            val intent = Intent(context, EpisodeActivity::class.java)
            intent.putExtras(EpisodeActivity.getBundle(id, title))
            startActivity(intent)
        }

        override fun removeProgram(position: Int) {
            adapter.data.removeAt(position)
            adapter.notifyItemRemoved(position)
        }

        override fun clearPrograms() {
            adapter.data.clear()
        }

        override fun addPrograms(programs: List<Program>) {
            adapter.data.addAll(programs)
            adapter.notifyDataSetChanged()
        }

        override fun setEmptyTextVisibility(visibility: Int) {
            textViewEmpty.visibility = visibility
        }

        override fun showError() {
            Toast.makeText(context, R.string.common_network_error, Toast.LENGTH_SHORT).show()
        }
    }

    fun isRefreshing(value: Boolean)

    fun initRecyclerView()

    fun startEpisodeView(id: String, title: String);

    fun removeProgram(position: Int)

    fun clearPrograms()

    fun addPrograms(programs: List<Program>)

    fun setEmptyTextVisibility(visibility: Int)

    fun showError()
}
