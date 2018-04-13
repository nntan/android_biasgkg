package com.luan.dms_management.service;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by luan.nt on 8/29/2017.
 */

public class Network {
    private MediaType CONTENT_TYPE = MediaType.parse("application/x-www-form-urlencoded;charset=utf-8");

    public interface Callback {
        void onCallBack(String response);

        void onFail(String error);
    }

    private static Network instance;

    private Network() {
    }

    public static Network getInstance() {
        if (instance == null) {
            instance = new Network();
        }
        return instance;
    }

    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .build();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void executePost(String link, List<Param> params, final Callback callback) {
        FormBody.Builder builder = new FormBody.Builder();
        for (Param p : params) {
            builder.add(p.key, p.value);
        }
        RequestBody requestBody = builder.build();

        Request request = new Request.Builder()
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .url(link)
                .method("POST", RequestBody.create(null, new byte[0]))
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFail(e.getMessage());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                if (response.code() != 200 && response.code() != 201) {
                    callback.onFail(response.body().string());
                    return;
                }

                String body = response.body().string();
                callback.onCallBack(body);
            }
        });
    }

    public void executeGet(String link, final Callback callback) {
        Request request = new Request.Builder()
                .url(link)
                .method("GET", null)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFail(e.getMessage());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                if (response.code() != 200) {
                    callback.onFail(response.body().string());
                    return;
                }

                String body = response.body().string();
                callback.onCallBack(body);
            }
        });
    }
}
