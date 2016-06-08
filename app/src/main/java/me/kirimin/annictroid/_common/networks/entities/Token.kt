package me.kirimin.annictroid._common.networks.entities

data class Token(var access_token: String,
            var token_type: String,
            var expires_in: Int,
            var scope: String,
            var created_at: Int)
