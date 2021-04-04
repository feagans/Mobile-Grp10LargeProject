package com.test.mylifegoale.base;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.codemybrainsout.ratingdialog.RatingDialog;
import com.test.mylifegoale.R;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    String playStoreUrl = "";
    String title = "If you enjoy using FotoFinder: Image Search, Image Downloader App, would you mind taking a moment to rate it? It won’t take more than a minute.";
    boolean toolbarBack = false;

    public abstract void init();

    public abstract void setBinding();

    public abstract void setToolbar();


    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setBinding();
        init();
        setToolbar();
    }

    public void setToolbarBack(boolean z) {
        try {
            this.toolbarBack = z;
            if (z) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(z);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean onSupportNavigateUp() {
        if (this.toolbarBack) {
            onBackPressed();
        }
        return super.onSupportNavigateUp();
    }

    public void setToolbarTitle(String str) {
        getSupportActionBar().setTitle((CharSequence) str);
    }

    public void EmailUs(String str) {
        try {
            String str2 = Build.MODEL;
            String str3 = Build.MANUFACTURER;
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("mailto:magnetic.lab2019@gmail.com"));
            intent.putExtra("android.intent.extra.SUBJECT", "Your Suggestion - " + getString(R.string.app_name) + "(" + getPackageName() + ")");
            intent.putExtra("android.intent.extra.TEXT", str + "\n\nDevice Manufacturer : " + str3 + "\nDevice Model : " + str2 + "\nAndroid Version : " + Build.VERSION.RELEASE);
            startActivityForResult(intent, 9);
        } catch (Exception e) {
            Log.d("", e.toString());
        }
    }

    public void showDialog() {
        final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.imageserach.imagesearch", 0);
        RatingDialog build = new RatingDialog.Builder(this).session(1).title(this.title).threshold(4.0f).icon(getResources().getDrawable(R.mipmap.ic_launcher)).titleTextColor(R.color.colorPrimaryDark).negativeButtonText("Never").positiveButtonTextColor(R.color.colorPrimaryDark).negativeButtonTextColor(R.color.colorPrimaryDark).formTitle("Submit Feedback").formHint("Tell us where we can improve").formSubmitText("Submit").formCancelText("Cancel").ratingBarColor(R.color.colorPrimaryDark).playstoreUrl(this.playStoreUrl).onRatingBarFormSumbit(new RatingDialog.Builder.RatingDialogFormListener() {
            public void onFormSubmitted(String str) {
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putBoolean("shownever", true);
                edit.commit();
            }
        }).build();
        if (sharedPreferences.getBoolean("shownever", false)) {
            Toast.makeText(this, "Already Submitted", Toast.LENGTH_SHORT).show();
        } else {
            build.show();
        }
    }

    public void uriparse(String str) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
        intent.addFlags(1208483840);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException unused) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/developer?id=Magnetic+Lab")));
        }
    }
}
