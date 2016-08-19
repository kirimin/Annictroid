package me.kirimin.annictroid._common.networks.apis;

import java.util.HashMap;
import java.util.Map;

import me.kirimin.annictroid._common.networks.entities.Works;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;
import rx.Single;

public interface HogeService {

    @GET("v1/works")
    Observable<Works> works(@Query("access_token") String token,
                            @Query("filter_ids") String workIds,
                            @Query("filter_season") String season,
                            @Query("filter_title") String title,
                            @Query("page") String page,
                            @Query("per_page") String perPage,
                            @Query("sort_id") String sortId,
                            @Query("sort_season") String sortSeason,
                            @Query("sort_watchers_count") String sortWatchers);

    @GET("v1/works")
    Single<Works> works(@Query("access_token") String token, @QueryMap Map<String, String> options);

    @GET("v1/works")
    Single<Works> works(@Query("access_token") String token,
                        @Query("filter_ids") String workIds);

    @GET("v1/works")
    Single<Works> worksFilterSeason(@Query("access_token") String token,
                        @Query("filter_season") String season,
                        @Query("sort_watchers_count") String sortWatchers);

    @GET("v1/works")
    Single<Works> worksFilterTitle(@Query("access_token") String token,
                        @Query("filter_title") String title,
                        @Query("sort_watchers_count") String sortWatchers);
}