package com.karlasa.smstransaction.api;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by kuvh on 2017-03-15.
 */

public class LoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        Log.i("[[NET]]", String.format("Sending request %s on %s%n%s\nbody is %s", request.url(), chain.connection(), request.headers(), request.body() != null ? request.body().toString() : ""));

        Response response = chain.proceed(request);

        long t2 = System.nanoTime();
        Log.i("[[NET]]", String.format("Received response %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers()));

        return response;
    }
}
