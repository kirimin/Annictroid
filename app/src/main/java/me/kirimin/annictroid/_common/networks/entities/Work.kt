package me.kirimin.annictroid._common.networks.entities

data class Work(val id: String,
                val title: String,
                val title_kana: String,
                val media: String,
                val media_text: String,
                val season_name: String,
                val season_name_text: String,
                val release_on: String,
                val release_on_about: String,
                val official_site_url: String,
                val wikipedia_url: String,
                val twitter_username: String,
                val Twitter_hashtag: String,
                val episodes_count: Int,
                val watchers_count: Int)