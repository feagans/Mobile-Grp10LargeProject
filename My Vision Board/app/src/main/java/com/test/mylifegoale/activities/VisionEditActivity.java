package com.test.mylifegoale.activities;

import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import com.test.mylifegoale.R;
import com.test.mylifegoale.base.BaseActivity;
import com.test.mylifegoale.base.roomDb.AppDatabase;
import com.test.mylifegoale.databinding.ActivityEditVisionBinding;
import com.test.mylifegoale.itemClick.DialogClick;
import com.test.mylifegoale.model.VisionModel;
import com.test.mylifegoale.utilities.AdConstants;
import com.test.mylifegoale.utilities.AllDialog;
import com.test.mylifegoale.utilities.Constants;

import java.io.File;

public class VisionEditActivity extends BaseActivity {
    ActivityEditVisionBinding binding;
    boolean isDelete = false;
    VisionModel visionModel;

    public void onClick(View view) {
    }

    public void setBinding() {
        this.binding = (ActivityEditVisionBinding) DataBindingUtil.setContentView(this, R.layout.activity_edit_vision);
    }

    public void init() {
        AdConstants ads = new AdConstants();
        ads.loadNativeAd(this, binding.nativeadcontainer);

        this.visionModel = (VisionModel) getIntent().getParcelableExtra(Constants.VISION_DATA_TAG);
        this.binding.setVisionModel(this.visionModel);
    }

    public void setToolbar() {
        setToolbarTitle(getString(R.string.vision_board));
        setToolbarBack(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.vision_edit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.delete) {
            new AllDialog().callDialog("", "", "", "", this, new DialogClick() {
                @Override
                public void onNegetiveClick() {
                    //ADD
                }

                @Override
                public void onPositiveClick() {
                    isDelete = true;
                    if (AppDatabase.getAppDatabase(VisionEditActivity.this).visionDao().delete(VisionEditActivity.this.visionModel) > 0) {
                        new File(VisionEditActivity.this.visionModel.getVisionProfile()).delete();
                        VisionEditActivity.this.onBackPressed();
                    }
                }
            });
        } else if (itemId == R.id.edit) {
            startActivityForResult(new Intent(this, AddGoalActivity.class).putExtra(Constants.EDIT_ADD_VISION_TAG, true).putExtra(Constants.VISION_DATA_TAG, this.visionModel), 100);
        } else if (itemId == R.id.share) {
            shareVision(this.visionModel.getName(), this.visionModel.getDescription());
        }
        return super.onOptionsItemSelected(menuItem);
    }


    @Override
    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        if (i == 100 && intent != null) {
            Intent intent2 = getIntent();
            intent2.putExtra(Constants.EDIT_ADD_VISION_TAG, true);
            intent2.putExtra(Constants.DELETE_VISION_TAG, this.isDelete);
            intent2.putExtra(Constants.VISION_DATA_TAG, (VisionModel) intent.getParcelableExtra(Constants.VISION_DATA_TAG));
            setResult(0, intent);
            finish();
        }
        super.onActivityResult(i, i2, intent);
    }

    public void shareVision(String str, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append(getPackageName());
        sb.append(".provider");
        Uri uriForFile = FileProvider.getUriForFile(this, sb.toString(), new File(this.visionModel.getVisionProfile()));
        Intent intent = new Intent("android.intent.action.SEND");
        intent.putExtra("android.intent.extra.STREAM", uriForFile);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setType("*/*");
        intent.putExtra("android.intent.extra.SUBJECT", str);
        intent.putExtra("android.intent.extra.TEXT", Html.fromHtml("<b>" + getString(R.string.title_note) + " : " + str + "</b>").toString() + "\n\n" + str2);
        startActivity(Intent.createChooser(intent, getResources().getString(R.string.share_using)));
    }

    @Override
    public void onBackPressed() {
        if (this.isDelete) {
            Intent intent = getIntent();
            intent.putExtra(Constants.EDIT_ADD_VISION_TAG, true);
            intent.putExtra(Constants.DELETE_VISION_TAG, this.isDelete);
            intent.putExtra(Constants.VISION_DATA_TAG, this.visionModel);
            setResult(0, intent);
        }
        super.onBackPressed();
    }
}
