package com.test.mylifegoale.view;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.test.mylifegoale.MyApplication;
import com.test.mylifegoale.R;
import com.test.mylifegoale.activities.HomeActivity;
import com.test.mylifegoale.base.BaseActivity;
import com.test.mylifegoale.base.roomDb.AppDatabase;
import com.test.mylifegoale.baseClass.BaseActivityBinding;
import com.test.mylifegoale.data.model.LoggedInUser;
import com.test.mylifegoale.databinding.ActivityAccountInfoBinding;
import com.test.mylifegoale.databinding.ActivityDashboardBinding;
import com.test.mylifegoale.databinding.ActivityQuoteOfTheDayBinding;
import com.test.mylifegoale.model.AffirmationRowModel;
import com.test.mylifegoale.model.toolbar.ToolbarModel;
import com.test.mylifegoale.utilities.AdConstants;
import com.test.mylifegoale.utilities.AppConstants;
import com.test.mylifegoale.utilities.AppPref;
import com.test.mylifegoale.utilities.Constants;
import com.test.mylifegoale.utilities.adBackScreenListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class AccountInfoActivity extends BaseActivityBinding {
    private static InterstitialAd admob_interstitial;
    private static adBackScreenListener mAdBackScreenListener;
    private ActivityAccountInfoBinding binding;
    public ToolbarModel toolbarModel;

    public void initMethods() {
    }

    public static void LoadAd() {
        AdRequest adRequest;
        try {
            if (!AppPref.getIsAdfree(MyApplication.getInstance().getApplicationContext())) {
                Log.d("Loadad", "called");
                admob_interstitial = new InterstitialAd(MyApplication.getInstance().getApplicationContext());
                admob_interstitial.setAdUnitId(AdConstants.AD_FULL);
                if (AdConstants.npaflag) {
                    Log.d("NPA", "" + AdConstants.npaflag);
                    Bundle bundle = new Bundle();
                    bundle.putString("npa", "1");
                    adRequest = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, bundle).build();
                } else {
                    Log.d("NPA", "" + AdConstants.npaflag);
                    adRequest = new AdRequest.Builder().build();
                }
                admob_interstitial.loadAd(adRequest);
                admob_interstitial.setAdListener(new AdListener() {
                    public void onAdClosed() {
                        super.onAdClosed();
                        BackScreen();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void BackScreen() {
        adBackScreenListener adbackscreenlistener = mAdBackScreenListener;
        if (adbackscreenlistener != null) {
            adbackscreenlistener.BackScreen();
        }
        if (AdConstants.adCount < AdConstants.AD_LIMIT) {
            LoadAd();
        }
    }

    public static void BackPressedAd(adBackScreenListener adbackscreenlistener) {
        mAdBackScreenListener = adbackscreenlistener;
        InterstitialAd interstitialAd = admob_interstitial;
        if (interstitialAd == null) {
            BackScreen();
        } else if (!interstitialAd.isLoaded() || AdConstants.adCount >= AdConstants.AD_LIMIT) {
            BackScreen();
        } else {
            try {
                admob_interstitial.show();
            } catch (Exception unused) {
                BackScreen();
            }
            AdConstants.adCount++;
        }
    }

    public void setBinding() {
        this.binding = (ActivityAccountInfoBinding) DataBindingUtil.setContentView(this, R.layout.activity_account_info);
        LoadAd();
        AdConstants.bannerad(this.binding.llads, this);
        setDetails();
    }

    // Get all user data
    private void setDetails() {
    String fullName = LoggedInUser.getUserFullName();
    String email = LoggedInUser.getUserEmail();
    String userName = LoggedInUser.getUserName();
    Boolean verified = LoggedInUser.getUserVerifiedStatus();
    this.binding.fullName.setText("Name: "+ fullName);
    this.binding.userName.setText("Username: " + userName);
    this.binding.email.setText("Email: "+ email);
}

    public void setToolbar() {
        this.toolbarModel = new ToolbarModel();
        this.toolbarModel.setTitle(getString(R.string.quote_of_the_day));
        this.binding.includedToolbar.setToolbarModel(this.toolbarModel);
    }


    public void setOnClicks() {
        this.binding.includedToolbar.imgBack.setOnClickListener(this);
    }

    // Click back button to return to hoome page
    public void onClick(View view) {
        Log.d("TAGGY", "clicky");
        int id = view.getId();
        if (id == R.id.imgBack) {
            onBackPressed();
        }
    }
}
