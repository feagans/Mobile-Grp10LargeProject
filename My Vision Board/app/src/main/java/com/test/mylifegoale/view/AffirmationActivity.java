package com.test.mylifegoale.view;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.test.mylifegoale.R;
import com.test.mylifegoale.baseClass.BaseActivityBinding;
import com.test.mylifegoale.databinding.ActivityMainDrawerBinding;
import com.test.mylifegoale.itemClick.OnFragmentInteractionListener;
import com.test.mylifegoale.model.toolbar.ToolbarModel;
import com.test.mylifegoale.utilities.AdConstants;

public class AffirmationActivity extends BaseActivityBinding implements OnFragmentInteractionListener {
    private ActivityMainDrawerBinding binding;
    private Fragment currentFragment;
    public int drawerPosition = -1;
    public ImageView imgAdd;
    public ImageView imgOther;
    public ToolbarModel toolbarModel;


    public void setBinding() {
        this.binding = (ActivityMainDrawerBinding) DataBindingUtil.setContentView(this, R.layout.activity_main_drawer);
        AdConstants.bannerad(this.binding.includedMainView.llads, this);
    }


    public void setToolbar() {
        this.toolbarModel = new ToolbarModel();
        this.toolbarModel.setBack(true);
        this.binding.includedMainView.includedToolbar.setToolbarModel(this.toolbarModel);
        this.binding.includedMainView.includedToolbar.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String str) {
                return false;
            }

            public boolean onQueryTextSubmit(String str) {
                return false;
            }
        });
    }


    public void setOnClicks() {
        this.binding.includedMainView.includedToolbar.imgDrawer.setOnClickListener(this);
        this.binding.linDrawerTop.setOnClickListener(this);
        this.binding.includedMainView.includedToolbar.imgBack.setOnClickListener(this);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.imgBack) {
            onBackPressed();
        }
    }


    public void initMethods() {
        this.imgAdd = this.binding.includedMainView.includedToolbar.imgAdd;
        this.imgOther = this.binding.includedMainView.includedToolbar.imgOther;
        openHome();
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public void openFragmentUsingType(int i) {
        switch (i) {
            case 9:
                if (this.drawerPosition != 9) {
                    this.drawerPosition = 9;
                    makeFragmentVisible(new FolderListFragment());
                    setToolbarAndMenu(getString(R.string.drawerTitleFolders), false, false, false, true);
                }
                return;
            case 10:
                if (this.drawerPosition != 10) {
                    this.drawerPosition = 10;
                    makeFragmentVisible(new ManageFolderFragment());
                    setToolbarAndMenu(getString(R.string.drawerTitleManageFolders), false, false, true, false);
                }
                return;
            default:
                return;
        }
    }

    public void openHome() {
        this.drawerPosition = 1;
        makeFragmentVisible(new AllActiveListFragment());
        setToolbarAndMenu(getString(R.string.drawerTitleHome), false, false, true, true);
    }

    public void setToolbarAndMenu(String str, boolean z, boolean z2, boolean z3, boolean z4) {
        this.toolbarModel.setBack(true);
        this.toolbarModel.setTitle(str);
        this.toolbarModel.setSearchMenu(z);
        this.toolbarModel.setProgressMenu(z2);
        this.toolbarModel.setOtherMenu(z3);
        this.toolbarModel.setAdd(z4);
    }

    public void makeFragmentVisible(Fragment fragment) {
        this.currentFragment = fragment;
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        getSupportFragmentManager().popBackStack((String) null, 1);
        beginTransaction.replace(R.id.frameContainer, fragment);
        beginTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        if (!(this.currentFragment instanceof AllActiveListFragment)) {
            openHome();
        } else {
            super.onBackPressed();
        }
    }


    public void onDestroy() {
        super.onDestroy();
    }

}
