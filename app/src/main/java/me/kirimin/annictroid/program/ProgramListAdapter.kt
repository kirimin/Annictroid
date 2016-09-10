package me.kirimin.annictroid.program

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import me.kirimin.annictroid.R
import me.kirimin.annictroid._common.extensions.toIntegerString
import me.kirimin.annictroid._common.networks.entities.Program
import me.kirimin.annictroid._common.utils.ApiDateFormatter

class ProgramListAdapter(context: Context,
                         private val onItemClick: (program: Program) -> Unit,
                         private val onChecked: (position: Int, program: Program) -> Unit) : RecyclerView.Adapter<ProgramListAdapter.ViewHolder>() {

    private val inflater: LayoutInflater
    val data: MutableList<Program>

    init {
        inflater = LayoutInflater.from(context)
        data = mutableListOf()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.row_program_list, parent, false))
    }


    override fun onBindViewHolder(holder: ProgramListAdapter.ViewHolder, position: Int) {
        val program = data[position]
        holder.work.text = program.work.title
        holder.episode.text = "#" + program.episode.number.toIntegerString() + " " + (program.episode.title ?: "")
        holder.channel.text = program.channel.name
        holder.startedAt.text = ApiDateFormatter.getDisplayDateTimeByApiTime(program.started_at)
        holder.view.setOnClickListener {
            onItemClick(program)
        }
        holder.watched.setOnClickListener {
            onChecked(data.indexOf(program), program)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val view: CardView = view.findViewById(R.id.programCardView) as CardView
        val work: TextView = view.findViewById(R.id.programTextViewWork) as TextView
        val episode: TextView = view.findViewById(R.id.programTextViewEpisode) as TextView
        val channel: TextView = view.findViewById(R.id.programTextViewChannel) as TextView
        val startedAt: TextView = view.findViewById(R.id.programTextViewStartedAt) as TextView
        val watched: Button = view.findViewById(R.id.programCheckBoxWatched) as Button
    }
}