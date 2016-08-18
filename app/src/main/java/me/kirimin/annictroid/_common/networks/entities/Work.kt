package me.kirimin.annictroid._common.networks.entities

data class Work @JvmOverloads constructor(val id: String = "",
                val title: String = "",
                val title_kana: String = "",
                val media: String = "",
                val media_text: String = "",
                val season_name: String = "",
                val season_name_text: String = "",
                val release_on: String = "",
                val release_on_about: String = "",
                val official_site_url: String? = null,
                val wikipedia_url: String? = null,
                val twitter_username: String? = null,
                val twitter_hashtag: String? = null,
                val episodes_count: Int = 0,
                val watchers_count: Int = 0)