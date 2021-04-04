package com.test.mylifegoale.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.test.mylifegoale.R;
import com.test.mylifegoale.adapters.FolderAdapter;
import com.test.mylifegoale.base.roomDb.AppDatabase;
import com.test.mylifegoale.baseClass.BaseFragmentRecyclerBinding;
import com.test.mylifegoale.databinding.FragmentFolderListBinding;
import com.test.mylifegoale.itemClick.OnAsyncBackground;
import com.test.mylifegoale.itemClick.RecyclerItemClick;
import com.test.mylifegoale.itemClick.TwoButtonDialogListener;
import com.test.mylifegoale.model.FolderRowModel;
import com.test.mylifegoale.model.affirm.FolderListModel;
import com.test.mylifegoale.utilities.AppConstants;
import com.test.mylifegoale.utilities.BackgroundAsync;

import java.util.ArrayList;

public class FolderListFragment extends BaseFragmentRecyclerBinding {
    private FragmentFolderListBinding binding;

    public AppDatabase db;

    public FolderListModel model;


    public void callApi() {
    }


    public void initMethods() {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getArguments();
    }

    public View getViewBinding() {
        return this.binding.getRoot();
    }


    public void setBinding(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        this.binding = (FragmentFolderListBinding) DataBindingUtil.inflate(layoutInflater, R.layout.fragment_folder_list, viewGroup, false);
        this.model = new FolderListModel();
        this.model.setArrayList(new ArrayList());
        this.model.setNoDataIcon(R.drawable.affirmations);
        this.model.setNoDataText(getString(R.string.noDataTitleFolder));
        this.model.setNoDataDetail(getString(R.string.noDataDescFolder));
        this.binding.setFolderListModel(this.model);
        this.db = AppDatabase.getAppDatabase(this.context);
    }


    public void setToolbar() {
        if (getActivity() instanceof AffirmationActivity) {
            ((AffirmationActivity) getActivity()).imgAdd.setOnClickListener(this);
        }
    }


    public void setOnClicks() {
        this.binding.fabAdd.setOnClickListener(this);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.imgAdd) {
            openItemDetail(-1, new FolderRowModel(), false);
        }
    }

    private void openItemDetail(int i, FolderRowModel folderRowModel, boolean z) {
        Intent intent = new Intent(this.context, AddEditFolderActivity.class);
        intent.putExtra(AddEditFolderActivity.EXTRA_IS_EDIT, z);
        intent.putExtra(AddEditFolderActivity.EXTRA_POSITION, i);
        intent.putExtra(AddEditFolderActivity.EXTRA_MODEL, folderRowModel);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, 1002);
    }


    public void fillData() {
        new BackgroundAsync(this.context, false, "", new OnAsyncBackground() {
            public void onPreExecute() {
            }

            public void doInBackground() {
                try {
                    model.getArrayList().addAll(db.folderDao().getAllListWithCount());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onPostExecute() {
                notifyAdapter();
            }
        }).execute(new Object[0]);
    }


    public void setRecycler() {
        this.binding.recycler.setLayoutManager(new LinearLayoutManager(this.context));
        this.binding.recycler.setAdapter(new FolderAdapter(2, this.context, this.model.getArrayList(), new RecyclerItemClick() {
            public void onClick(int i, int i2) {
                if (i2 == 2) {
                    deleteItemDialog(i);
                    return;
                }
                FolderListFragment folderListFragment = FolderListFragment.this;
                folderListFragment.openList(folderListFragment.model.getArrayList().get(i), i);
            }
        }));
    }

    public void deleteItemDialog(final int i) {
        AppConstants.showTwoButtonDialog(this.context, getString(R.string.app_name), getString(R.string.delete_msg) + "<br /> <b>" + this.model.getArrayList().get(i).getName() + "</b>", true, true, getString(R.string.delete), getString(R.string.cancel), (TwoButtonDialogListener) new TwoButtonDialogListener() {
            public void onCancel() {
            }

            public void onOk() {
                deleteItem(i);
            }
        });
    }


    public void deleteItem(int i) {
        try {
            this.db.affirmationDao().deleteFolder(this.model.getArrayList().get(i).getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.db.folderDao().delete(this.model.getArrayList().get(i));
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        this.model.getArrayList().remove(i);
        this.binding.recycler.getAdapter().notifyItemRemoved(i);
    }


    public void openList(FolderRowModel folderRowModel, int i) {
        Intent intent = new Intent(this.context, FolderAffirmationListActivity.class);
        intent.putExtra(FolderAffirmationListActivity.EXTRA_MODEL, folderRowModel);
        intent.putExtra(FolderAffirmationListActivity.EXTRA_POSITION, i);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, 1001);
    }


    public void notifyAdapter() {
        setViewVisibility();
        if (this.binding.recycler.getAdapter() != null) {
            this.binding.recycler.getAdapter().notifyDataSetChanged();
        }
    }

    private void setViewVisibility() {
        int i = 0;
        this.binding.linData.setVisibility(this.model.isListData() ? View.VISIBLE : View.GONE);
        LinearLayout linearLayout = this.binding.linNoData;
        if (this.model.isListData()) {
            i = 8;
        }
        linearLayout.setVisibility(i);
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i2 == -1) {
            switch (i) {
                case 1001:
                    refreshList();
                    return;
                case 1002:
                    updateList(intent);
                    return;
                default:
                    return;
            }
        }
    }

    private void refreshList() {
        this.model.getArrayList().clear();
        fillData();
    }

    private void updateList(Intent intent) {
        if (intent != null) {
            try {
                if (intent.hasExtra(AddEditFolderActivity.EXTRA_MODEL)) {
                    FolderRowModel folderRowModel = (FolderRowModel) intent.getParcelableExtra(AddEditFolderActivity.EXTRA_MODEL);
                    if (intent.getBooleanExtra(AddEditFolderActivity.EXTRA_IS_DELETED, false)) {
                        this.model.getArrayList().remove(intent.getIntExtra(AddEditFolderActivity.EXTRA_POSITION, 0));
                    } else if (intent.getBooleanExtra(AddEditFolderActivity.EXTRA_IS_EDIT, false)) {
                        this.model.getArrayList().set(intent.getIntExtra(AddEditFolderActivity.EXTRA_POSITION, 0), folderRowModel);
                    } else {
                        this.model.getArrayList().add(folderRowModel);
                    }
                    notifyAdapter();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
