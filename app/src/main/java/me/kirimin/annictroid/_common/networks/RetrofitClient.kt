package me.kirimin.annictroid._common.networks

import me.kirimin.annictroid.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    val endPoint = "https://api.annict.com/"
    val defaultClient: OkHttpClient

    init {
        val httpClient = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(logging)
        }
        defaultClient = httpClient.build()
    }

    fun default(): Retrofit.Builder {
        return Retrofit.Builder()
                .baseUrl(endPoint)
                .client(defaultClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
    }
}
