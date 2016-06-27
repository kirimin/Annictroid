package me.kirimin.annictroid._common.networks

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    val endPoint = "https://api.annict.com/"
    val defaultClient = OkHttpClient()

    fun default(): Retrofit.Builder {
        return Retrofit.Builder()
                .baseUrl(endPoint)
                .client(defaultClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
    }
}
