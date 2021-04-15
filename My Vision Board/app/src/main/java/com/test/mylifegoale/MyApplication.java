package com.test.mylifegoale;
import android.util.Log;
import android.app.Application;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.test.mylifegoale.data.APIService;

import java.io.IOException;

public class MyApplication extends Application {
    private static MyApplication mInstance;
    public Retrofit retrofit;
    public APIService.API API;
    private static final String TAG = MyApplication.class.getSimpleName();
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://letsbuckit.herokuapp.com/api/login/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.API = retrofit.create(APIService.API.class);
        APIService.LoginResponse result = null;
        try {
            Response<APIService.LoginResponse> loginRequest = this.API.login(new APIService.LoginRequest("testbogani", "testbogani")).;
            result = loginRequest.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, result.accessToken);

    }

    public static synchronized MyApplication getInstance() {
        MyApplication myApplication;
        synchronized (MyApplication.class) {
            myApplication = mInstance;
        }
        return myApplication;
    }
}