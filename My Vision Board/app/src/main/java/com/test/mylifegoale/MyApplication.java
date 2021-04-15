package com.test.mylifegoale;


import android.util.Log;
import android.app.Application;
import android.widget.EditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.test.mylifegoale.data.APIService;
import com.test.mylifegoale.ui.login.LoginViewModel;

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

//        try {
//            APIService.LoginRequest user = new APIService.LoginRequest("trev", "testing");
//            this.API.login(user).enqueue(new Callback<APIService.LoginResponse>() {
//                @Override
//                public void onResponse(Call<APIService.LoginResponse> call, Response<APIService.LoginResponse> response) {
//                    MyApplication.getInstance().user = response.body();
//                }
//
//                @Override
//                public void onFailure(Call<APIService.LoginResponse> call, Throwable t) {
//                    Log.d("TAGGYTAG", "failing!");
//                }
//            });
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    public static synchronized MyApplication getInstance() {
        MyApplication myApplication;
        synchronized (MyApplication.class) {
            myApplication = mInstance;
        }
        return myApplication;
    }
}