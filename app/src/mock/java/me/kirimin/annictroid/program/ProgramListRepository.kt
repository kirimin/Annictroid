package me.kirimin.annictroid.program

import me.kirimin.annictroid._common.networks.entities.Channel
import me.kirimin.annictroid._common.networks.entities.Program
import me.kirimin.annictroid._common.networks.entities.Programs
import me.kirimin.annictroid._common.networks.entities.Work
import rx.Single

class ProgramListRepository {

    fun requestPrograms(token: String,
                        sortStartedAt: String = "",
                        filterUnWatched: String = "",
                        filterStartedAt: String = "",
                        page: String = ""): Single<Programs> {
        val programs = listOf(
                Program(started_at = "2016-05-07T20:10:00.000Z", work = Work(title = "test1"), channel = Channel("", ""), episode = Program.Episode()),
                Program(started_at = "2016-05-07T20:10:00.000Z", work = Work(title = "test2"), channel = Channel("", ""), episode = Program.Episode()))
        return Single.just(Programs(programs = programs, next_page = null, prev_page = null, total_count = 1))
    }

    fun postRecord(token: String, episodeId: String) {
    }

    fun getToken() = ""
}
