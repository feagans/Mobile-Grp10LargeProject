package com.test.mylifegoale.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.test.mylifegoale.MyApplication;
import com.test.mylifegoale.R;
import com.test.mylifegoale.adapters.DrawerAdapter;
import com.test.mylifegoale.base.BaseActivity;
import com.test.mylifegoale.base.roomDb.AppDatabase;
import com.test.mylifegoale.databinding.ActivityHomeBinding;
import com.test.mylifegoale.itemClick.RecyclerItemClick;
import com.test.mylifegoale.model.AffirmationRowModel;
import com.test.mylifegoale.model.drawer.DrawerRowModel;
import com.test.mylifegoale.utilities.AdConstants;
import com.test.mylifegoale.utilities.AppConstants;
import com.test.mylifegoale.utilities.AppPref;
import com.test.mylifegoale.utilities.Constants;
import com.test.mylifegoale.utilities.adBackScreenListener;
import com.test.mylifegoale.view.AffirmationActivity;
import com.test.mylifegoale.view.QuoteOfTheDayActivity;
import com.test.mylifegoale.view.SettingFragment;

import java.util.ArrayList;

public class HomeActivity extends BaseActivity {
    private static InterstitialAd admob_interstitial;
    private static adBackScreenListener mAdBackScreenListener;
    ActivityHomeBinding binding;
    public ArrayList<DrawerRowModel> drawerArrayList;
    ActionBarDrawerToggle toggle;

    public void setBinding() {
        this.binding = (ActivityHomeBinding) DataBindingUtil.setContentView(this, R.layout.activity_home);
        LoadAd();
        AdConstants.bannerad(this.binding.llads, this);

        this.binding.scrollRoot.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            public void onScrollChange(NestedScrollView nestedScrollView, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    binding.toolbar.setBackgroundColor(ContextCompat.getColor(HomeActivity.this, R.color.colorPrimary));

                }
                if (scrollY == 0) {
                    binding.toolbar.setBackgroundColor(Color.parseColor("#00000000"));
                }
            }
        });
    }

    public void init() {
        setDrawer();
    }

    public void setToolbar() {
        setSupportActionBar(this.binding.toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (this.toggle.onOptionsItemSelected(menuItem)) {
            return true;
        }
        int itemId = menuItem.getItemId();
        if (itemId == R.id.action_settings) {
            startActivity(new Intent(this, SettingFragment.class));
        } else if (itemId == R.id.dashBoard) {
            startActivity(new Intent(this, DashBoardActivity.class));
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void onClick(View view) {
        int id = view.getId();
          //omitted button
        /*if (id == R.id.affirmation) {
            startActivity(new Intent(this, AffirmationActivity.class));
        } else */
            if (id == R.id.journal) {
            startActivity(new Intent(this, ToDolistActivity.class));
          //omitted button
        } /*else if (id == R.id.lifePurpose) {
            startActivity(new Intent(this, LifePurposeActivity.class));
        } */else if (id == R.id.vision) {
            startActivity(new Intent(this, VisionActivity.class));
        } /*else if (id == R.id.cvshare) {
            AppConstants.shareApp(this);
        } *//*else if (id == R.id.cvrate) {
            AppConstants.showRattingDialog(this, Constants.RATTING_BAR_TITLE);
        }*/
    }

    private void setDrawer() {
        this.toggle = new ActionBarDrawerToggle(this, this.binding.drawerLayout, this.binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        this.binding.drawerLayout.addDrawerListener(this.toggle);
        fillDrawerArray();
        setDrawerRecycler();
    }

    private void fillDrawerArray() {
        this.drawerArrayList = new ArrayList<>();
        this.drawerArrayList.add(new DrawerRowModel(getString(R.string.drawerTitleAffirmation), R.drawable.affirmation_of_the_day, 2, 11, false));
        this.drawerArrayList.add(new DrawerRowModel(getString(R.string.dashBoard), R.drawable.ic_dashboard, 2, 16, false));
        this.drawerArrayList.add(new DrawerRowModel(getString(R.string.download), R.drawable.drawer_ratting, 2, 12, false));
        this.drawerArrayList.add(new DrawerRowModel(getString(R.string.drawerTitleSetting), R.drawable.ic_settings, 2, 6, false));
        this.drawerArrayList.add(new DrawerRowModel(getString(R.string.drawerTitleRatting), R.drawable.ic_rate_us, 2, 2, false));
        this.drawerArrayList.add(new DrawerRowModel(getString(R.string.drawerTitleShare), R.drawable.ic_share, 2, 4, false));
        this.drawerArrayList.add(new DrawerRowModel(getString(R.string.drawerTitlePrivacyPolicy), R.drawable.ic_privacy_policy, 2, 7, false));
    }

    private void setDrawerRecycler() {
        this.binding.recyclerDrawer.setLayoutManager(new LinearLayoutManager(this));
        this.binding.recyclerDrawer.setAdapter(new DrawerAdapter(this.drawerArrayList, new RecyclerItemClick() {
            public void onClick(int i, int i2) {
                HomeActivity.this.openFragment(i);
            }
        }));
        setDetails();

    }


    public void openFragment(int i) {
        setDrawerSelection(i);
        openFragmentUsingType(i);
    }

    private void setDrawerSelection(int i) {
        for (int i2 = 0; i2 < this.drawerArrayList.size(); i2++) {
            this.drawerArrayList.get(i2).setSelected(false);
        }
        this.drawerArrayList.get(i).setSelected(true);
        this.binding.recyclerDrawer.getAdapter().notifyDataSetChanged();
    }

    private void openCloseDrawer(boolean z) {
        if (z) {
            if (!this.binding.drawerLayout.isDrawerOpen((int) GravityCompat.START)) {
                this.binding.drawerLayout.openDrawer((int) GravityCompat.START);
            }
        } else if (this.binding.drawerLayout.isDrawerOpen((int) GravityCompat.START)) {
            this.binding.drawerLayout.closeDrawers();
        }
    }


    private void setDetails() {
        String str = Constants.DEFAULT_AFFIRMATION_TEXT;
        String formattedDate = AppConstants.getFormattedDate(System.currentTimeMillis(), Constants.DATE_FORMAT_DATE_DB);
        if (!AppPref.getAffirmationOfTheDayDate(HomeActivity.this).equalsIgnoreCase(formattedDate)) {
            AppPref.setAffirmationOfTheDayId(HomeActivity.this, AppDatabase.getAppDatabase(HomeActivity.this).affirmationDao().getRandomId());
            AppPref.setAffirmationOfTheDayDate(HomeActivity.this, formattedDate);
        }
        this.binding.text.setMovementMethod(new ScrollingMovementMethod());
        setImage(this.binding.img);
        this.binding.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, QuoteOfTheDayActivity.class));
            }
        });
        try {
            AffirmationRowModel detail = AppDatabase.getAppDatabase(HomeActivity.this).affirmationDao().getDetail(AppPref.getAffirmationOfTheDayId(HomeActivity.this));
            if (detail != null) {
                str = detail.getQuoteText();
            } else if (AppDatabase.getAppDatabase(HomeActivity.this).affirmationDao().getAllCount() > 0) {
                AppPref.setAffirmationOfTheDayId(HomeActivity.this, AppDatabase.getAppDatabase(HomeActivity.this).affirmationDao().getRandomId());
                str = AppDatabase.getAppDatabase(HomeActivity.this).affirmationDao().getDetail(AppPref.getAffirmationOfTheDayId(HomeActivity.this)).getQuoteText();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.binding.text.setText(str);
    }

    private void setImage(ImageView imageView) {
        Glide.with(HomeActivity.this).load(AppConstants.getListBackgroundImage().get(AppConstants.getRandomWithBound(AppConstants.getListBackgroundImage().size())).getImageUrl()).apply(new RequestOptions().fitCenter().centerCrop()).into(imageView);
    }


    private void openFragmentUsingType(int i) {
        switch (this.drawerArrayList.get(i).getConsPosition()) {
            case 2:
                AppConstants.showRattingDialog(this, Constants.RATTING_BAR_TITLE);
                openCloseDrawer(false);
                return;
            case 4:
                AppConstants.shareApp(this);
                openCloseDrawer(false);
                return;
            case 6:
                startActivity(new Intent(this, SettingFragment.class));
                openCloseDrawer(false);
                return;
            case 7:
                uriparse(AdConstants.STR_PRIVACY_URI);
                openCloseDrawer(false);
                return;

            case 11:
                startActivity(new Intent(this, QuoteOfTheDayActivity.class));
                openCloseDrawer(false);
                return;
            case 12:
                startActivity(new Intent(this, DownloadActivity.class));
                openCloseDrawer(false);
                return;
            case 16:
                startActivity(new Intent(this, DashBoardActivity.class));
                return;
            default:
                return;
        }
    }

    @Override
    public void onBackPressed() {
        if (!AppPref.getIsRateUsShown(this)) {
            AppPref.setIsRateUsShown(this, true);
            AppConstants.showRattingDialog(this, Constants.RATTING_BAR_TITLE);
            return;
        }
        super.onBackPressed();
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
}
