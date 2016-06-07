package me.kirimin.annictroid._common.networks.entities

data class Episode(val id: String,
                   val number: Int,
                   val number_text: String,
                   val sort_number: Int,
                   val title: String?,
                   val records_count: Int)