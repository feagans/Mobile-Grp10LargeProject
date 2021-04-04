package com.test.mylifegoale.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.test.mylifegoale.R;
import com.test.mylifegoale.activities.HomeActivity;
import com.test.mylifegoale.base.roomDb.AppDatabase;
import com.test.mylifegoale.baseClass.BaseActivityBinding;
import com.test.mylifegoale.databinding.ActivityAffirmPlayerBinding;
import com.test.mylifegoale.databinding.RowAffirmationImageBinding;
import com.test.mylifegoale.itemClick.OnFragmentInteractionListener;
import com.test.mylifegoale.model.AffirmationRowModel;
import com.test.mylifegoale.model.affirm.AffirmListModel;
import com.test.mylifegoale.utilities.AdConstants;
import com.test.mylifegoale.utilities.AppConstants;
import com.test.mylifegoale.utilities.AppPref;
import com.test.mylifegoale.utilities.Constants;
import com.test.mylifegoale.utilities.adBackScreenListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class AffirmPlayerActivity extends BaseActivityBinding implements OnFragmentInteractionListener, EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {
    public static String EXTRA_ID = "id";
    public static final String EXTRA_IS_NOTIFICATION = "EXTRA_IS_NOTIFICATION";
    public static final String EXTRA_LIST = "EXTRA_LIST";
    public static final String FROM_VISION = "FROM_VISION";
    private long SECOND_IN_MS = 1000;

    public ActivityAffirmPlayerBinding binding;

    public int currentPage = 0;
    private Handler handler;
    private Handler handlerSleep;
    boolean isFromVision = false;
    private ArrayList<MediaPlayer> mediaPlayerList;

    public AffirmListModel model;
    private Runnable runnableSleep;
    private Runnable update;

    public void onFragmentInteraction(Uri uri) {
    }

    public void onRationaleAccepted(int i) {
    }

    public void onRationaleDenied(int i) {
    }


    public void setToolbar() {
    }

    public float volumeForMusic(int i) {
        return ((float) i) / 100.0f;
    }


    public void setBinding() {
        getWindow().addFlags(128);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        this.binding = (ActivityAffirmPlayerBinding) DataBindingUtil.setContentView(this, R.layout.activity_affirm_player);
        setModelDetails();
        this.binding.setAffirmListModel(this.model);
    }

    private void setModelDetails() {
        this.model = new AffirmListModel();
        this.model.setArrayList(new ArrayList());
    }


    public void setOnClicks() {
        this.binding.imgFullScreen.setOnClickListener(this);
        this.binding.imgPlayPause.setOnClickListener(this);
        this.binding.imgDownload.setOnClickListener(this);
        this.binding.imgShowSeekMenu.setOnClickListener(this);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.imgDownload) {
            saveScreenShotToStorage();
        } else if (id == R.id.imgFullScreen) {
            AffirmListModel affirmListModel = this.model;
            affirmListModel.setFullSreen(!affirmListModel.isFullSreen());
            this.binding.linSeekBar.setVisibility(!this.model.isFullSreen() ? View.VISIBLE : View.GONE);
        } else if (id == R.id.imgPlayPause) {
            AffirmListModel affirmListModel2 = this.model;
            affirmListModel2.setPause(!affirmListModel2.isPause());
            playPause();
        } else if (id == R.id.imgShowSeekMenu) {
            AffirmListModel affirmListModel3 = this.model;
            affirmListModel3.setShowSeekMenu(!affirmListModel3.isShowSeekMenu());
        }
    }


    public void initMethods() {
        setVisibility();
        pagerSetup();
        mediaPlayerSetup();
    }

    private void setVisibility() {
        int i = 0;
        if (AppPref.isScreenControls(this.context)) {
            this.binding.imgFullScreen.setVisibility(View.VISIBLE);
        }
        this.binding.linSeekBar.setVisibility(AppPref.isScreenControls(this.context) ? View.VISIBLE : View.GONE);
        ImageView imageView = this.binding.imgDownload;
        if (!AppPref.isShowDownloadButton(this.context)) {
            i = 8;
        }
        imageView.setVisibility(i);
    }

    private void pagerSetup() {
        fillData();
        this.binding.viewPager.setAdapter(new PagerAdapterAffirmationList(this.context, this.model.getArrayList()));
        this.handler = new Handler();
        this.update = new Runnable() {
            public void run() {
                scrollPagerToNext(true);
            }
        };
        setupHandler();
        this.binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int i) {
            }

            public void onPageScrolled(int i, float f, int i2) {
            }

            public void onPageSelected(int i) {
                setupHandler();
                currentPage = i;
                if (model.isPause()) {
                    model.setPause(false);
                    playPause();
                } else if (!AppPref.isEnableBackgroundVoice(context)) {
                    pause(1);
                    playVoice();
                }
            }
        });
        this.binding.viewPager.setPageTransformer(true, new PageTransformerAffirmationList());
    }


    public void scrollPagerToNext(boolean z) {
        if (AppPref.isEnableAutoPlaying(this.context) && !this.model.isPause()) {
            if (!z || this.model.getArrayList().get(this.currentPage).isVoiceFound()) {
                if (this.currentPage != this.model.getArrayList().size() - 1) {
                    this.currentPage++;
                } else if (AppPref.isLoopPlaying(this.context)) {
                    this.currentPage = 0;
                }
            } else if (this.currentPage != this.model.getArrayList().size() - 1) {
                this.currentPage++;
            } else if (AppPref.isLoopPlaying(this.context)) {
                this.currentPage = 0;
            }
            this.binding.viewPager.setCurrentItem(this.currentPage, true);
        }
    }


    public void setupHandler() {
        this.handler.removeCallbacks(this.update);
        this.handler.postDelayed(this.update, this.SECOND_IN_MS * AppPref.getAutoPlayInterval(this.context));
    }

    private void fillData() {
        AffirmationRowModel affirmationRowModel;
        try {
            if (getIntent() != null && getIntent().hasExtra(EXTRA_LIST)) {
                this.isFromVision = getIntent().getBooleanExtra(FROM_VISION, false);
                this.model.getArrayList().addAll(getIntent().<AffirmationRowModel>getParcelableArrayListExtra(EXTRA_LIST));
            } else if (getIntent() != null && getIntent().hasExtra(EXTRA_IS_NOTIFICATION) && getIntent().getBooleanExtra(EXTRA_IS_NOTIFICATION, false)) {
                AdConstants.adCount = 2;
                try {
                    if (!getIntent().hasExtra(EXTRA_ID) || getIntent().getStringExtra(EXTRA_ID) == null || getIntent().getStringExtra(EXTRA_ID).equalsIgnoreCase("0")) {
                        affirmationRowModel = new AffirmationRowModel();
                        affirmationRowModel.setQuoteText(Constants.DEFAULT_AFFIRMATION_TEXT);
                        this.model.getArrayList().add(affirmationRowModel);
                    } else {
                        affirmationRowModel = AppDatabase.getAppDatabase(this.context).affirmationDao().getDetail(getIntent().getStringExtra(EXTRA_ID));
                        if (affirmationRowModel == null || affirmationRowModel.getQuoteText().isEmpty()) {
                            if (AppDatabase.getAppDatabase(this.context).affirmationDao().getAllCount() > 0) {
                                affirmationRowModel = AppDatabase.getAppDatabase(this.context).affirmationDao().getDetail(AppDatabase.getAppDatabase(this.context).affirmationDao().getRandomId());
                            } else {
                                affirmationRowModel = new AffirmationRowModel();
                                affirmationRowModel.setQuoteText(Constants.DEFAULT_AFFIRMATION_TEXT);
                            }
                        }
                        this.model.getArrayList().add(affirmationRowModel);
                    }
                } catch (Exception e) {
                    AffirmationRowModel affirmationRowModel2 = new AffirmationRowModel();
                    affirmationRowModel2.setQuoteText(Constants.DEFAULT_AFFIRMATION_TEXT);
                    e.printStackTrace();
                    affirmationRowModel = affirmationRowModel2;
                }
            }
            if (AppPref.isPlayInRandomOrder(this.context)) {
                Collections.shuffle(this.model.getArrayList());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private class PagerAdapterAffirmationList extends PagerAdapter {
        private ArrayList<AffirmationRowModel> arrayList;
        private Context context;

        public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
            return view == obj;
        }

        public PagerAdapterAffirmationList(Context context2, ArrayList<AffirmationRowModel> arrayList2) {
            this.context = context2;
            this.arrayList = arrayList2;
        }

        @NonNull
        public Object instantiateItem(@NonNull ViewGroup viewGroup, int i) {
            RowAffirmationImageBinding rowAffirmationImageBinding = (RowAffirmationImageBinding) DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.row_affirmation_image, viewGroup, false);
            rowAffirmationImageBinding.text.setMovementMethod(new ScrollingMovementMethod());
            rowAffirmationImageBinding.setAffirmationRowModel(this.arrayList.get(i));
            View root = rowAffirmationImageBinding.getRoot();
            RequestOptions requestOptions = new RequestOptions();
            if (isFromVision) {
                requestOptions.error((int) R.drawable.place_holder);
            } else {
                ((RequestOptions) ((RequestOptions) requestOptions.fitCenter()).centerCrop()).error((int) R.drawable.place_holder);
            }
            Glide.with(this.context).load(this.arrayList.get(i).getImageUrlPlayer()).apply((BaseRequestOptions<?>) requestOptions).into(rowAffirmationImageBinding.img);
            viewGroup.addView(root);
            return root;
        }

        public void destroyItem(@NonNull ViewGroup viewGroup, int i, @NonNull Object obj) {
            viewGroup.removeView((View) obj);
        }

        public int getCount() {
            return this.arrayList.size();
        }
    }

    private class PageTransformerAffirmationList implements ViewPager.PageTransformer {
        private PageTransformerAffirmationList() {
        }

        public void transformPage(@NonNull View view, float f) {
            view.setTranslationX(((float) view.getWidth()) * (-f));
            if (f <= -1.0f || f >= 1.0f) {
                view.setAlpha(0.0f);
            } else if (f == 0.0f) {
                view.setAlpha(1.0f);
            } else {
                view.setAlpha(1.0f - Math.abs(f));
            }
        }
    }

    private void mediaPlayerSetup() {
        setupMediaList();
        setRangeSeekBar();
        playPause();
        setSleepTimer();
    }

    private void setSleepTimer() {
        if (AppPref.getSleepTimer(this.context) > 0) {
            this.handlerSleep = new Handler();
            this.runnableSleep = new Runnable() {
                public void run() {
                    AppConstants.toastShort(context, getString(R.string.sleep_timer));
                    onBackPressed();
                }
            };
            this.handlerSleep.postDelayed(this.runnableSleep, this.SECOND_IN_MS * AppPref.getSleepTimer(this.context));
        }
    }

    private void setupMediaList() {
        this.mediaPlayerList = new ArrayList<>();
        this.mediaPlayerList.add(null);
        this.mediaPlayerList.add(null);
    }

    private void setRangeSeekBar() {
        this.binding.seekBarBack.setProgress(AppPref.getBackgroundMusicVolume(this.context));
        this.binding.seekBarBack.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                AffirmPlayerActivity affirmPlayerActivity = AffirmPlayerActivity.this;
                affirmPlayerActivity.setVolume(0, affirmPlayerActivity.binding.seekBarBack.getProgress());
                AppPref.setBackgroundMusicVolume(context, binding.seekBarBack.getProgress());
            }
        });
        this.binding.seekBarVoice.setProgress(AppPref.getVoiceRecordingVolume(this.context));
        this.binding.seekBarVoice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                AffirmPlayerActivity affirmPlayerActivity = AffirmPlayerActivity.this;
                affirmPlayerActivity.setVolume(1, affirmPlayerActivity.binding.seekBarVoice.getProgress());
                AppPref.setVoiceRecordingVolume(context, binding.seekBarVoice.getProgress());
            }
        });
    }


    public void playPause() {
        if (!this.model.isPause()) {
            play(0, AppPref.getBackgroundMusicUrl(this.context), this.binding.seekBarBack.getProgress(), true);
            playVoice();
            return;
        }
        pause(0);
        pause(1);
    }


    public void playVoice() {
        Log.d("Hello", "playVoice" + AppPref.isEnableBackgroundVoice(this.context));
        if (AppPref.isEnableBackgroundVoice(this.context)) {
            if (isVoiceFound()) {
                play(1, AppPref.getBackgroundVoiceUrl(this.context), this.binding.seekBarVoice.getProgress(), true);
            }
        } else if (this.model.getArrayList().get(this.currentPage).isVoiceFound()) {
            play(1, this.model.getArrayList().get(this.currentPage).getVoiceUrl(), this.binding.seekBarVoice.getProgress(), false);
        }
    }

    public boolean isVoiceFound() {
        String backgroundVoiceUrl = AppPref.getBackgroundVoiceUrl(this.context);
        return backgroundVoiceUrl != null && backgroundVoiceUrl.trim().length() > 0 && new File(backgroundVoiceUrl).exists();
    }

    private void play(int i, String str, int i2, boolean z) {
        if ((i == 0 && AppPref.isEnableBackgroundMusic(this.context)) || (i == 1 && AppPref.isPlayVoiceFile(this.context))) {
            try {
                if (!new File(str).exists() || str.contains(Constants.PATH_RESOURCE)) {
                    this.mediaPlayerList.set(i, MediaPlayer.create(this.context, AppConstants.getDefaultSong(str)));
                } else if (Build.VERSION.SDK_INT >= 21) {
                    this.mediaPlayerList.set(i, MediaPlayer.create(this.context, FileProvider.getUriForFile(getApplicationContext(), "com.test.mylifegoale.provider", new File(str))));
                } else {
                    this.mediaPlayerList.set(i, MediaPlayer.create(this.context, Uri.parse(str)));
                }
                this.mediaPlayerList.get(i).setVolume(volumeForMusic(i2), volumeForMusic(i2));
                this.mediaPlayerList.get(i).setLooping(z);
                this.mediaPlayerList.get(i).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(40);
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
        }
    }


    public void pause(int i) {
        if (((i == 0 && AppPref.isEnableBackgroundMusic(this.context)) || (i == 1 && AppPref.isPlayVoiceFile(this.context))) && this.mediaPlayerList.get(i) != null) {
            try {
                this.mediaPlayerList.get(i).stop();
                try {
                    this.mediaPlayerList.get(i).release();
                    this.mediaPlayerList.set(i, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                this.mediaPlayerList.get(i).release();
                this.mediaPlayerList.set(i, null);
            } catch (Throwable th) {
                try {
                    this.mediaPlayerList.get(i).release();
                    this.mediaPlayerList.set(i, null);
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
                throw th;
            }
        }
    }

    public void setVolume(int i, int i2) {
        if (this.mediaPlayerList.get(i) != null && this.mediaPlayerList.get(i) != null) {
            float volumeForMusic = volumeForMusic(i2);
            try {
                this.mediaPlayerList.get(i).setVolume(volumeForMusic, volumeForMusic);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveScreenShotToStorage() {
        if (isHasPermissions(this.context, "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE")) {
            saveScreenShot();
        } else {
            requestPermissions(this.context, getString(R.string.rationale_save), Constants.REQUEST_PERM_FILE, "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE");
        }
    }

    private void saveScreenShot() {
        captureScreenshot(Constants.PATH_IMAGE, AppConstants.getFormattedDate(Calendar.getInstance().getTimeInMillis(), Constants.SIMPLE_DATE_FORMAT_DATE_TIME), this.binding.viewPager, this.context);
    }

    public void captureScreenshot(String str, String str2, View view, Context context) {
        try {
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache(true);
            Bitmap createBitmap = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + str);
            if (!file.exists()) {
                file.mkdir();
            }
            File file2 = new File(file.getPath(), str2 + ".jpg");
            if (file2.exists()) {
                file2.delete();
            }
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                createBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                MediaStore.Images.Media.insertImage(context.getContentResolver(), createBitmap, "Screen", "screen");
                Context applicationContext = getApplicationContext();
                Toast.makeText(applicationContext, "Image has been saved to " + str + " / " + str2, Toast.LENGTH_SHORT).show();
                AppConstants.refreshFile(getApplicationContext(), file);
                AppConstants.refreshFile(getApplicationContext(), file2);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            Log.d("Hello", "onBackPressed");
            this.model.setPause(true);
            playPause();
            if (!(this.handlerSleep == null || this.runnableSleep == null)) {
                this.handlerSleep.removeCallbacks(this.runnableSleep);
            }
            getWindow().clearFlags(128);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HomeActivity.BackPressedAd(new adBackScreenListener() {
            public void BackScreen() {
                finish();
            }
        });
    }

    private boolean isHasPermissions(Context context, String... strArr) {
        return EasyPermissions.hasPermissions(context, strArr);
    }

    private void requestPermissions(Context context, String str, int i, String... strArr) {
        EasyPermissions.requestPermissions((Activity) context, str, i, strArr);
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        EasyPermissions.onRequestPermissionsResult(i, strArr, iArr, this);
    }

    public void onPermissionsGranted(int i, @NonNull List<String> list) {
        if (i == 1051) {
            saveScreenShot();
        }
    }

    public void onPermissionsDenied(int i, @NonNull List<String> list) {
        if (EasyPermissions.somePermissionPermanentlyDenied((Activity) this, list)) {
            new AppSettingsDialog.Builder((Activity) this).build().show();
        }
    }
}
