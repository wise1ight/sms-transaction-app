package com.karlasa.smstransaction.api;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by kuvh on 2016-12-29.
 */

public interface RemoteServerService {
    @FormUrlEncoded
    @POST("api.php")
    Call<ResponseBody> getResponse(@Field("address") String address, @Field("body") String body, @Field("timestamp") long timestamp, @Field("unique") String unique);

    public static final OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new LoggingInterceptor())
            .build();

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("[Here is base url]")
            .client(client)
            .build();
}
