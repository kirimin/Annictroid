package me.kirimin.annictroid._common.networks.entities

data class Program(val id: String,
                   val started_at: String,
                   val is_rebroadcast: Boolean,
                   val channel: Channel,
                   val work: Work,
                   val episode: Episode) {

    data class Episode(val id: String,
                       val number: Int,
                       val number_text: String,
                       val sort_number: Int,
                       val title: String?,
                       val records_count: Int)
}