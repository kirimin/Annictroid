package me.kirimin.annictroid.myanime

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import me.kirimin.annictroid.R
import me.kirimin.annictroid._common.models.AnimeInfo
import me.kirimin.annictroid._common.utils.ApiDateFormatter

class MyAnimeListAdapter(context: Context) : ArrayAdapter<AnimeInfo>(context, 0) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ViewHolder
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.row_my_anime_list, null)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }
        val anime = getItem(position)
        holder.title.text = anime.work.title
        if (anime.recentProgram != null) {
            holder.season.text = anime.recentProgram.channel.name
            holder.startedAt.text = ApiDateFormatter.getDisplayDateTimeByApiTime(anime.recentProgram.started_at, "E kk:mm~")
        } else {
            holder.season.text = ""
            holder.startedAt.text = ""
        }
        return view
    }

    class ViewHolder(view: View) {
        val title: TextView = view.findViewById(R.id.myAnimeTextViewTitle) as TextView
        val season: TextView = view.findViewById(R.id.myAnimeTextViewChannel) as TextView
        val startedAt: TextView = view.findViewById(R.id.myAnimeTextViewStartedAt) as TextView
    }
}