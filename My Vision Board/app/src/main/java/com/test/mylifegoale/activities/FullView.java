package com.test.mylifegoale.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.test.mylifegoale.R;
import com.test.mylifegoale.utilities.Constants;

import java.io.File;


public class FullView extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    Bundle bundle;
    ImageView imgMain;

    public static ProgressDialog dia;

    LinearLayout LL_Done;
    TextView txtHeaderName;
    ImageView imgButtonImage;

    View header;

    private void shareImage() {

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("*/*");
        share.putExtra(Intent.EXTRA_TEXT, "Download App From here : https://play.google.com/store/apps/details?id=" + getPackageName());
        File file1 = new File(Environment.getExternalStorageDirectory() + File.separator + Constants.PATH_IMAGE + "/" + bundle.get("FileName"));
        Uri uri = FileProvider.getUriForFile(this, "com.test.mylifegoale.provider", file1);

        share.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(share, "Share Image!"));
    }

    public void showProgress() {

        dia = new ProgressDialog(this);
        dia.setMessage("Loading ...");
        dia.setIndeterminate(false);
        dia.setCancelable(false);
        dia.setCanceledOnTouchOutside(false);
        dia.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_fullview);

        showProgress();

        LL_Done = (LinearLayout) findViewById(R.id.LL_Done);
        txtHeaderName = (TextView) findViewById(R.id.txtHeaderName);
        imgButtonImage = (ImageView) findViewById(R.id.imgButtonImage);

        LL_Done.setVisibility(View.VISIBLE);
        txtHeaderName.setText("My Gallery ");
        txtHeaderName.setTextSize(16);
        txtHeaderName.setGravity(Gravity.CENTER);
        imgButtonImage.setImageResource(R.drawable.ic_share);

        bundle = getIntent().getExtras();
        imgMain = (ImageView) findViewById(R.id.imgGallerImageView);

        header = (View) findViewById(R.id.header);

        fillData();

        LL_Done.setOnClickListener(this);

        imgMain.setOnTouchListener(this);

    }

    public void dismissProgress() {
        if (dia.isShowing())
            dia.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.LL_Done:
                shareImage();
                break;
        }
    }

    private void fillData() {
        File file1 = new File(Environment.getExternalStorageDirectory() + File.separator + Constants.PATH_IMAGE + "/" + bundle.get("FileName"));
        Uri uri = FileProvider.getUriForFile(
                getApplicationContext(), "com.test.mylifegoale.provider", file1);
        if (file1.exists()) {
            imgMain.setImageURI(uri);
        }
        dismissProgress();
    }

    int Counter = 0;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                if (Counter == 0) {
                    SingleHeaderOut(header);
                    Counter = 1;
                } else {
                    SingleHeaderIn(header);
                    Counter = 0;
                }
                break;

        }

        return false;
    }


    private static Animation animation;

    public void SingleHeaderOut(final View view) {
        animation = AnimationUtils.loadAnimation(this, R.anim.header_bottom_back_fast);
        view.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                animation.setAnimationListener(null);
                view.clearAnimation();
                view.setVisibility(View.GONE);
            }

        });
    }

    public void SingleHeaderIn(final View view) {

        animation = AnimationUtils.loadAnimation(this, R.anim.header_bottom_fast);
        view.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                animation.setAnimationListener(null);
                view.clearAnimation();
                view.setVisibility(View.VISIBLE);
            }

        });
    }

}
