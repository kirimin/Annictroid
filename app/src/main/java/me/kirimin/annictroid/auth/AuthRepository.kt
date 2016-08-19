package me.kirimin.annictroid.auth

import me.kirimin.annictroid.BuildConfig
import me.kirimin.annictroid._common.networks.RetrofitClient
import me.kirimin.annictroid._common.networks.apis.AnnictService
import me.kirimin.annictroid._common.networks.entities.Token
import rx.Observable
import rx.Single

class AuthRepository {

    fun requestToken(code: String): Single<Token> {
        if (BuildConfig.FLAVOR == "mock") {
            return Single.just(Token(access_token = "mock_token", created_at = 0, expires_in = 0, scope = "", token_type = ""))
        }
        return RetrofitClient.default().build().create(AnnictService::class.java).token(code = code)
    }
}