package com.test.mylifegoale.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.test.mylifegoale.R;
import com.test.mylifegoale.base.roomDb.AppDatabase;
import com.test.mylifegoale.baseClass.BaseActivityBinding;
import com.test.mylifegoale.databinding.ActivityQuoteOfTheDayBinding;
import com.test.mylifegoale.model.AffirmationRowModel;
import com.test.mylifegoale.model.toolbar.ToolbarModel;
import com.test.mylifegoale.utilities.AppConstants;
import com.test.mylifegoale.utilities.AppPref;
import com.test.mylifegoale.utilities.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class QuoteOfTheDayActivity extends BaseActivityBinding implements EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {
    private ActivityQuoteOfTheDayBinding binding;
    private boolean isShare = false;
    public ToolbarModel toolbarModel;


    public void initMethods() {
    }

    public void onRationaleAccepted(int i) {
    }

    public void onRationaleDenied(int i) {
    }


    public void setBinding() {
        this.binding = (ActivityQuoteOfTheDayBinding) DataBindingUtil.setContentView(this, R.layout.activity_quote_of_the_day);
        setDetails();
    }

    private void setDetails() {
        String str;
        String formattedDate = AppConstants.getFormattedDate(System.currentTimeMillis(), Constants.DATE_FORMAT_DATE_DB);
        if (!AppPref.getAffirmationOfTheDayDate(this.context).equalsIgnoreCase(formattedDate)) {
            AppPref.setAffirmationOfTheDayId(this.context, AppDatabase.getAppDatabase(this.context).affirmationDao().getRandomId());
            AppPref.setAffirmationOfTheDayDate(this.context, formattedDate);
        }
        this.binding.text.setMovementMethod(new ScrollingMovementMethod());
        setImage(this.binding.img);
        try {
            AffirmationRowModel detail = AppDatabase.getAppDatabase(this.context).affirmationDao().getDetail(AppPref.getAffirmationOfTheDayId(this.context));
            if (detail != null) {
                str = detail.getQuoteText();
            } else if (AppDatabase.getAppDatabase(this.context).affirmationDao().getAllCount() > 0) {
                AppPref.setAffirmationOfTheDayId(this.context, AppDatabase.getAppDatabase(this.context).affirmationDao().getRandomId());
                str = AppDatabase.getAppDatabase(this.context).affirmationDao().getDetail(AppPref.getAffirmationOfTheDayId(this.context)).getQuoteText();
            } else {
                str = Constants.DEFAULT_AFFIRMATION_TEXT;
            }
        } catch (Exception e) {
            e.printStackTrace();
            str = Constants.DEFAULT_AFFIRMATION_TEXT;
        }
        this.binding.text.setText(str);
    }

    private void setImage(ImageView imageView) {
        Glide.with(imageView.getContext()).load(AppConstants.getListBackgroundImage().get(AppConstants.getRandomWithBound(AppConstants.getListBackgroundImage().size())).getImageUrl()).apply((BaseRequestOptions<?>) ((RequestOptions) new RequestOptions().fitCenter()).centerCrop()).into(imageView);
    }


    public void setToolbar() {
        this.toolbarModel = new ToolbarModel();
        this.toolbarModel.setTitle(getString(R.string.quote_of_the_day));
        this.binding.includedToolbar.setToolbarModel(this.toolbarModel);
    }


    public void setOnClicks() {
        this.binding.includedToolbar.imgBack.setOnClickListener(this);
        this.binding.btnDownload.setOnClickListener(this);
        this.binding.btnShare.setOnClickListener(this);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnDownload) {
            saveScreenShotToStorage();
        } else if (id == R.id.btnShare) {
            this.isShare = true;
            saveScreenShotToStorage();
        } else if (id == R.id.imgBack) {
            onBackPressed();
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
        String formattedDate = AppConstants.getFormattedDate(Calendar.getInstance().getTimeInMillis(), Constants.SIMPLE_DATE_FORMAT_DATE_TIME);
        captureScreenshot(Constants.PATH_IMAGE, formattedDate, this.binding.relData, this.context);
        if (this.isShare) {
            shareImage(new File(Environment.getExternalStorageDirectory() + File.separator + Constants.PATH_IMAGE + "/" + formattedDate + ".jpg"));
            this.isShare = false;
        }
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

    @SuppressLint("WrongConstant")
    private void shareImage(File file) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("*/*");
        intent.addFlags(1073741824);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(getApplicationContext(), "com.test.mylifegoale.provider", file));
        try {
            startActivity(Intent.createChooser(intent, "Share File"));
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
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
