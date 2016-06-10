package me.kirimin.annictroid.program

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import me.kirimin.annictroid.R
import me.kirimin.annictroid._common.networks.RetrofitClient
import me.kirimin.annictroid._common.networks.apis.AnnictService
import me.kirimin.annictroid._common.networks.entities.Program
import me.kirimin.annictroid._common.networks.entities.Record
import me.kirimin.annictroid._common.preferences.AppPreferences
import me.kirimin.annictroid._common.utils.ApiDateFormatter
import me.kirimin.annictroid.episode.EpisodeActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProgramListAdapter(context: Context) : ArrayAdapter<Program>(context, 0) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ViewHolder
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.row_program_list, null)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }
        val program = getItem(position)
        holder.work.text = program.work.title
        holder.episode.text = "#" + program.episode.number + " " + (program.episode.title ?: "")
        holder.channel.text = program.channel.name
        holder.startedAt.text = ApiDateFormatter.getDisplayDateTimeByApiTime(program.started_at)
        holder.view.setOnClickListener {
            val intent = Intent(context, EpisodeActivity::class.java)
            intent.putExtras(EpisodeActivity.getBundle(program.episode.id, program.work.title))
            context.startActivity(intent)
        }
        holder.watched.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(context, R.anim.list_item_drop)
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationEnd(animation: Animation?) {
                    remove(program)
                    notifyDataSetChanged()
                    RetrofitClient.default().build().create(AnnictService::class.java)
                            .meRecords(token = AppPreferences.getToken(context), episodeId = program.episode.id)
                            .enqueue(object : Callback<Record> {
                                override fun onFailure(call: Call<Record>?, t: Throwable?) {
                                }

                                override fun onResponse(call: Call<Record>?, response: Response<Record>?) {
                                }
                            })
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationStart(animation: Animation?) {
                }
            })
            view.startAnimation(animation)
        }
        return view
    }

    class ViewHolder(val view: View) {
        val work: TextView = view.findViewById(R.id.programTextViewWork) as TextView
        val episode: TextView = view.findViewById(R.id.programTextViewEpisode) as TextView
        val channel: TextView = view.findViewById(R.id.programTextViewChannel) as TextView
        val startedAt: TextView = view.findViewById(R.id.programTextViewStartedAt) as TextView
        val watched: Button = view.findViewById(R.id.programCheckBoxWatched) as Button
    }
}