package me.kirimin.annictroid.program

import me.kirimin.annictroid.Annictroid
import me.kirimin.annictroid._common.networks.RetrofitClient
import me.kirimin.annictroid._common.networks.apis.AnnictService
import me.kirimin.annictroid._common.networks.entities.Programs
import me.kirimin.annictroid._common.preferences.AppPreferences
import rx.Single
import rx.schedulers.Schedulers

class ProgramListRepository {

    fun requestPrograms(token: String,
                        sortStartedAt: String = "",
                        filterUnWatched: String = "",
                        filterStartedAt: String = "",
                        page: String = ""): Single<Programs> {
        return RetrofitClient.default().build().create(AnnictService::class.java)
                .mePrograms(token = token,
                        filterStartedAt = filterStartedAt,
                        filterUnWatched = filterUnWatched,
                        sortStartedAt = sortStartedAt,
                        page = page)
    }

    fun postRecord(token: String, episodeId: String) {
        RetrofitClient.default().build().create(AnnictService::class.java)
                .meRecords(token = token, episodeId = episodeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe({}, {})
    }

    fun getToken() = AppPreferences.getToken()
}
