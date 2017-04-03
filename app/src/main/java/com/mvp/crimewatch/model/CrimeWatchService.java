package com.mvp.crimewatch.model;

import com.mvp.crimewatch.constant.NetworkConstant;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by ajayshrestha on 3/18/17.
 */

/**
 * Retrofit End Point
 */

public interface CrimeWatchService {

    @GET("major")
    Observable<List<Crime>> getMajorCrimes(@QueryMap Map<String, String> queryString);


    class Factory {
        public CrimeWatchService create() {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(NetworkConstant.TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(NetworkConstant.TIMEOUT, TimeUnit.SECONDS).build();

            Retrofit retrofit = new Retrofit.Builder().baseUrl(NetworkConstant.URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            return retrofit.create(CrimeWatchService.class);
        }
    }
}
