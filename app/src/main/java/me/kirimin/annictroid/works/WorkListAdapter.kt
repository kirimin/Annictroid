package me.kirimin.annictroid.works

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import me.kirimin.annictroid.R
import me.kirimin.annictroid._common.models.AnimeInfo
import me.kirimin.annictroid._common.networks.entities.Work
import me.kirimin.annictroid.work.WorkActivity

class WorkListAdapter(private val context: Context,
                      private val onItemClick: (work: Work) -> Unit) : RecyclerView.Adapter<WorkListAdapter.ViewHolder>() {

    private val inflater: LayoutInflater
    val data: MutableList<Work>

    init {
        inflater = LayoutInflater.from(context)
        data = mutableListOf()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.row_work_list, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val work = data[position]
        holder.view.setOnClickListener {
            onItemClick(work)
        }
        holder.title.text = work.title
        holder.season.text = work.season_name_text
        holder.watchers.text = context.getString(R.string.work_watchers_count, work.watchers_count)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val title: TextView = view.findViewById(R.id.workListTextViewTitle) as TextView
        val watchers: TextView = view.findViewById(R.id.workListTextViewWatchers) as TextView
        val season: TextView = view.findViewById(R.id.workListTextViewSeason) as TextView
    }
}
