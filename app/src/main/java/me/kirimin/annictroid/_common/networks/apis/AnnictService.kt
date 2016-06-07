package me.kirimin.annictroid._common.networks.apis

import me.kirimin.annictroid.BuildConfig
import me.kirimin.annictroid._common.networks.entities.Programs
import me.kirimin.annictroid._common.networks.entities.Record
import me.kirimin.annictroid._common.networks.entities.Token
import me.kirimin.annictroid._common.networks.entities.Works
import retrofit2.Call
import retrofit2.http.*
import rx.Observable

interface AnnictService {

    @POST("oauth/token")
    fun token(@Query("client_id") clientId: String = BuildConfig.CLIENT_ID,
              @Query("client_secret") clientSecret: String = BuildConfig.CLIENT_SECRET,
              @Query("grant_type") grantType: String = "authorization_code",
              @Query("redirect_uri") redirectUri: String = "urn:ietf:wg:oauth:2.0:oob",
              @Query("code") code: String): Observable<Token>

    @Headers("Cache-Control: max-age=86400")
    @GET("v1/me/works")
    fun meWorks(@Query("access_token") token: String,
                @Query("filter_status") status: String = ""): Observable<Works>

    @Headers("Cache-Control: max-age=0")
    @GET("v1/me/programs")
    fun mePrograms(@Query("access_token") token: String,
                   @Query("sort_started_at") sortStartedAt: String = "",
                   @Query("filter_unwatched") filterUnWatched: String = "",
                   @Query("filter_started_at_lt") filterStartedAt: String = "",
                   @Query("filter_started_at_gt") filterStartedAtGt: String = "",
                   @Query("filter_work_ids") workIds: String = "",
                   @Query("per_page") perPage: String = "50"): Observable<Programs>

    @Headers("Cache-Control: max-age=86400")
    @GET("v1/me/programs")
    fun recentProgram(@Query("access_token") token: String,
                      @Query("filter_work_ids") workIds: String,
                      @Query("sort_started_at") filterStartedAt: String = "desc",
                      @Query("per_page") perPage: String = "1"): Call<Programs>

    @POST("v1/me/records")
    fun meRecords(@Query("access_token") token: String,
                  @Query("episode_id") episodeId: String): Call<Record>
}
