package com.test.mylifegoale.activities;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.test.mylifegoale.R;
import com.test.mylifegoale.base.BaseActivity;
import com.test.mylifegoale.base.roomDb.AppDatabase;
import com.test.mylifegoale.databinding.ActivityAddLifePurposeBinding;
import com.test.mylifegoale.model.LifePurposeModel;
import com.test.mylifegoale.utilities.AdConstants;
import com.test.mylifegoale.utilities.AppConstants;
import com.test.mylifegoale.utilities.Constants;

public class AddLifePurposeActivity extends BaseActivity {
    AppDatabase appDatabase;
    ActivityAddLifePurposeBinding binding;
    Context context;
    boolean dataAdded = false;
    boolean isForEdit = false;
    LifePurposeModel lifePurposeModel;

    public void setBinding() {
        this.binding = (ActivityAddLifePurposeBinding) DataBindingUtil.setContentView(this, R.layout.activity_add_life_purpose);
        this.binding.setLifePurposeModel(this.lifePurposeModel);
    }

    public void init() {
        AdConstants ads = new AdConstants();
        ads.loadNativeAd(this, binding.nativeadcontainer);

        this.context = this;
        this.isForEdit = getIntent().getBooleanExtra(Constants.EDIT_ADD_VISION_TAG, false);
        this.appDatabase = AppDatabase.getAppDatabase(this.context);
        if (this.isForEdit && getIntent() != null) {
            this.lifePurposeModel = (LifePurposeModel) getIntent().getParcelableExtra(Constants.VISION_DATA_TAG);
            this.binding.setLifePurposeModel(this.lifePurposeModel);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_save, menu);
        menu.findItem(R.id.saveAction).setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.saveAction) {
            this.lifePurposeModel.setTitle(this.binding.goal.getText().toString());
            this.lifePurposeModel.setDescription(this.binding.goalDescription.getText().toString());
            if (!this.isForEdit) {
                this.lifePurposeModel.setId(AppConstants.getUniqueId());
                if (this.appDatabase.lifePurposeDao().insert(this.lifePurposeModel) > 0) {
                    this.dataAdded = true;
                    onBackPressed();
                }
            } else if (this.appDatabase.lifePurposeDao().update(this.lifePurposeModel) > 0) {
                this.dataAdded = true;
                onBackPressed();
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void setToolbar() {
        setToolbarTitle(this.lifePurposeModel.getTitle());
        setToolbarBack(true);
    }

    public void onClick(View view) {
        view.getId();
    }

    @Override
    public void onBackPressed() {
        if (this.dataAdded) {
            Intent intent = getIntent();
            intent.putExtra(Constants.EDIT_ADD_VISION_TAG, this.isForEdit);
            intent.putExtra(Constants.VISION_DATA_TAG, this.lifePurposeModel);
            setResult(0, intent);
            super.onBackPressed();
            return;
        }
        super.onBackPressed();
    }
}
