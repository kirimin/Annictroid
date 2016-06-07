package me.kirimin.annictroid._common.networks.entities

data class Program(val id: String,
                   val started_at: String,
                   val is_rebroadcast: Boolean,
                   val channel: Channel,
                   val work: Work,
                   val episode: Episode)