package com.test.mylifegoale;

import android.util.Log;
import android.app.Application;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.test.mylifegoale.data.APIService;

import java.io.IOException;

public class MyApplication extends Application {
    private static MyApplication mInstance;
    public Retrofit retrofit;
    public APIService.API API;
    public APIService.LoginResponse user;

    public void onCreate() {
        super.onCreate();
        mInstance = this;
        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://letsbuckit.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.API = retrofit.create(APIService.API.class);

        try {
            APIService.LoginRequest user = new APIService.LoginRequest("trev", "testing");
            this.API.login(user).enqueue(new Callback<APIService.LoginResponse>() {
                @Override
                public void onResponse(Call<APIService.LoginResponse> call, Response<APIService.LoginResponse> response) {
                    MyApplication.getInstance().user = response.body();
                }

                @Override
                public void onFailure(Call<APIService.LoginResponse> call, Throwable t) {
                    Log.d("some tag", "bad");
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        // Log.d(TAG, result.accessToken);

    }

    public static synchronized MyApplication getInstance() {
        MyApplication myApplication;
        synchronized (MyApplication.class) {
            myApplication = mInstance;
        }
        return myApplication;
    }
}