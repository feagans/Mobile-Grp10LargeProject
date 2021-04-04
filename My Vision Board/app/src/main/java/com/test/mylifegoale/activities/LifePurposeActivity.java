//Omitted as its not needed
/*package com.test.mylifegoale.activities;

import android.content.Intent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.test.mylifegoale.R;
import com.test.mylifegoale.adapters.LifePurposeAdapter;
import com.test.mylifegoale.base.BaseActivity;
import com.test.mylifegoale.base.roomDb.AppDatabase;
import com.test.mylifegoale.databinding.ActivityVisionBinding;
import com.test.mylifegoale.model.LifePurposeModel;
import com.test.mylifegoale.utilities.AdConstants;
import com.test.mylifegoale.utilities.Constants;

import java.util.ArrayList;

public class LifePurposeActivity extends BaseActivity {
    AppDatabase appDatabase;
    ActivityVisionBinding binding;
    ArrayList<LifePurposeModel> visionModelArrayList = new ArrayList<>();

    public void setBinding() {
        this.binding = (ActivityVisionBinding) DataBindingUtil.setContentView(this, R.layout.activity_vision);
    }

    public void init() {
        AdConstants.bannerad(this.binding.llads, this);
        this.appDatabase = AppDatabase.getAppDatabase(this);
        this.visionModelArrayList = (ArrayList) this.appDatabase.lifePurposeDao().getAll();
        this.binding.visionList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.binding.visionList.setAdapter(new LifePurposeAdapter(this, this.visionModelArrayList));
        setDefaultLayout();
    }

    private void setDefaultLayout() {
        if (this.visionModelArrayList.size() > 0) {
            this.binding.defaultMsglayout.setVisibility(View.GONE);
            this.binding.bottomLayout.setVisibility(View.GONE);
            return;
        }
        this.binding.defaultMsglayout.setVisibility(View.VISIBLE);
        this.binding.bottomLayout.setVisibility(View.GONE);
    }

    public void setToolbar() {
        setToolbarTitle(getString(R.string.lifePurpose));
        setToolbarBack(true);
    }

    public void onClick(View view) {
        view.getId();
    }


    @Override
    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 100 && intent != null) {
            if (intent.getBooleanExtra(Constants.EDIT_ADD_VISION_TAG, false)) {
                int indexOf = this.visionModelArrayList.indexOf((LifePurposeModel) intent.getParcelableExtra(Constants.VISION_DATA_TAG));
                this.visionModelArrayList.set(indexOf, (LifePurposeModel) intent.getParcelableExtra(Constants.VISION_DATA_TAG));
                this.binding.visionList.getAdapter().notifyItemChanged(indexOf);
                return;
            }
            this.visionModelArrayList.add((LifePurposeModel) intent.getParcelableExtra(Constants.VISION_DATA_TAG));
            this.binding.visionList.getAdapter().notifyDataSetChanged();
        }
    }
}
*/