package com.test.mylifegoale.utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.test.mylifegoale.MyApplication;
import com.test.mylifegoale.data.APIService;
import com.test.mylifegoale.itemClick.OnAsyncBackground;

import java.io.IOException;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BackgroundAsync extends AsyncTask {
    private String defaultDialogMessage = "Please wait ...";
    private ProgressDialog dialog;
    private String dialogMessage;
    private boolean isProgress;
    private OnAsyncBackground onAsyncBackground;

    public BackgroundAsync(Context context2, boolean z, String str, OnAsyncBackground onAsyncBackground2) {
        this.isProgress = z;
        this.onAsyncBackground = onAsyncBackground2;
        this.dialogMessage = str;
        this.dialog = new ProgressDialog(context2);

        // Log.d(TAG, result.accessToken);
    }


    public void onPreExecute() {
        if (this.isProgress) {
            try {
                if (this.dialog != null) {
                    this.dialog.setMessage((this.dialogMessage == null || this.dialogMessage.length() <= 0) ? this.defaultDialogMessage : this.dialogMessage);
                    this.dialog.setCancelable(false);
                    try {
                        this.dialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        this.onAsyncBackground.onPreExecute();
        super.onPreExecute();
    }


    public Object doInBackground(Object[] objArr) {
        this.onAsyncBackground.doInBackground();
        return 0;
    }


    public void onPostExecute(Object obj) {
        try {
            if (this.dialog != null && this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.onAsyncBackground.onPostExecute();
        super.onPostExecute(obj);
    }
}
