package me.kirimin.annictroid.myanime

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import me.kirimin.annictroid.R
import me.kirimin.annictroid._common.models.AnimeInfo
import me.kirimin.annictroid._common.utils.ApiDateFormatter

class MyAnimeListAdapter(private val context: Context,
                         private val onItemClick: (anime: AnimeInfo) -> Unit) : RecyclerView.Adapter<MyAnimeListAdapter.ViewHolder>() {

    private val inflater: LayoutInflater
    val data: MutableList<AnimeInfo>

    init {
        inflater = LayoutInflater.from(context)
        data = mutableListOf()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAnimeListAdapter.ViewHolder {
        return MyAnimeListAdapter.ViewHolder(inflater.inflate(R.layout.row_my_anime_list, parent, false))
    }

    override fun onBindViewHolder(holder: MyAnimeListAdapter.ViewHolder, position: Int) {
        val anime = data[position]
        holder.view.setOnClickListener { onItemClick(anime) }
        holder.title.text = anime.work.title
        if (anime.recentProgram != null) {
            holder.season.text = anime.recentProgram.channel.name
            holder.startedAt.text = ApiDateFormatter.getDisplayDateTimeByApiTime(anime.recentProgram.started_at, "E kk:mm~")
        } else {
            holder.season.text = ""
            holder.startedAt.text = ""
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.myAnimeTextViewTitle) as TextView
        val season: TextView = view.findViewById(R.id.myAnimeTextViewChannel) as TextView
        val startedAt: TextView = view.findViewById(R.id.myAnimeTextViewStartedAt) as TextView
    }
}