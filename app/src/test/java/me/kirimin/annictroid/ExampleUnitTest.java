package me.kirimin.annictroid;

import android.util.Log;

import org.junit.Test;

import java.util.HashMap;

import me.kirimin.annictroid._common.networks.apis.HogeService;
import me.kirimin.annictroid._common.networks.entities.Works;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Single;
import rx.SingleSubscriber;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        HashMap<String, String> options = new HashMap<>();
        options.put("filter_ids", "1");
        Single<Works> o = retrofit.create(HogeService.class).works("token", options);
        o.subscribe(new SingleSubscriber<Works>() {
            @Override
            public void onSuccess(Works value) {

            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });
    }
}