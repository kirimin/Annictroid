package me.kirimin.annictroid._common.networks

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    val endPoint = "https://api.annict.com/"

    fun default(): Retrofit.Builder {
        val client = OkHttpClient.Builder().build();
        return Retrofit.Builder()
                .baseUrl(endPoint)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
    }
}
