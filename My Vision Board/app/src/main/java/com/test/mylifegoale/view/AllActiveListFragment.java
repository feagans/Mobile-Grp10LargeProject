package com.test.mylifegoale.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.test.mylifegoale.R;
import com.test.mylifegoale.adapters.AffirmationAdapter;
import com.test.mylifegoale.adapters.FolderAdapter;
import com.test.mylifegoale.base.roomDb.AppDatabase;
import com.test.mylifegoale.baseClass.BaseFragmentRecyclerBinding;
import com.test.mylifegoale.databinding.FragmentAllActiveListBinding;
import com.test.mylifegoale.itemClick.OnAsyncBackground;
import com.test.mylifegoale.itemClick.RecyclerItemClick;
import com.test.mylifegoale.model.AffirmationRowModel;
import com.test.mylifegoale.model.FolderRowModel;
import com.test.mylifegoale.model.affirm.AffirmListModel;
import com.test.mylifegoale.utilities.AppConstants;
import com.test.mylifegoale.utilities.BackgroundAsync;
import com.test.mylifegoale.utilities.ItemOffsetDecoration;
import com.test.mylifegoale.utilities.SwipeAndDragHelper;

import java.util.ArrayList;
import java.util.List;

public class AllActiveListFragment extends BaseFragmentRecyclerBinding {
    public FragmentAllActiveListBinding binding;
    private AppDatabase db;

    public AffirmListModel model;


    public void callApi() {
    }


    public void initMethods() {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }


    public View getViewBinding() {
        return this.binding.getRoot();
    }


    public void setBinding(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        this.binding = (FragmentAllActiveListBinding) DataBindingUtil.inflate(layoutInflater, R.layout.fragment_all_active_list, viewGroup, false);
        this.model = new AffirmListModel();
        this.model.setArrayList(new ArrayList());
        this.model.setFolderList(new ArrayList());
        this.model.setNoDataIcon(R.drawable.affirmations);
        this.model.setNoDataText(getString(R.string.noDataTitleActive));
        this.model.setNoDataDetail(getString(R.string.noDataDescActive));
        this.binding.setAffirmListModel(this.model);
        this.db = AppDatabase.getAppDatabase(this.context);
    }


    public void setToolbar() {
        if (getActivity() instanceof AffirmationActivity) {
            ((AffirmationActivity) getActivity()).imgAdd.setOnClickListener(this);
        }
        if (getActivity() instanceof AffirmationActivity) {
            ((AffirmationActivity) getActivity()).imgOther.setOnClickListener(this);
        }
        if (getActivity() instanceof AffirmationActivity) {
            ((AffirmationActivity) getActivity()).imgOther.setImageResource(this.model.isDragEnable() ? R.drawable.done : R.drawable.edit);
        }
    }


    public void setOnClicks() {
        this.binding.fabAdd.setOnClickListener(this);
        this.binding.fabPlay.setOnClickListener(this);
        this.binding.folder.setOnClickListener(this);
        this.binding.foldermanager.setOnClickListener(this);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.fabPlay) {
            checkSizeAndPlayer();
        } else if (id == R.id.imgAdd) {
            checkFolderAndAdd();
        } else if (id == R.id.imgOther) {
            AffirmListModel affirmListModel = this.model;
            affirmListModel.setDragEnable(!affirmListModel.isDragEnable());
            enableDrag(this.model.isDragEnable());
        } else if (id == R.id.folder) {
            if (getActivity() instanceof AffirmationActivity) {
                ((AffirmationActivity) getActivity()).openFragmentUsingType(9);
            }
        } else if (id == R.id.foldermanager) {
            if (getActivity() instanceof AffirmationActivity) {
                ((AffirmationActivity) getActivity()).openFragmentUsingType(10);
            }
        }
    }

    private void checkFolderAndAdd() {
        try {
            if (this.db.folderDao().getFolderListCount() > 0) {
                addItem();
            } else {
                AppConstants.toastShort(this.context, getString(R.string.noFolderToAdd));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addItem() {
        FolderRowModel folderRowModel;
        AffirmationRowModel affirmationRowModel = new AffirmationRowModel();
        try {
            folderRowModel = this.db.folderDao().getDefault();
        } catch (Exception e) {
            e.printStackTrace();
            folderRowModel = null;
        }
        affirmationRowModel.setFolderId(folderRowModel.getId());
        affirmationRowModel.setFolderRowModel(folderRowModel);
        affirmationRowModel.setActive(true);
        openDetail(-1, affirmationRowModel, false);
    }

    private void checkSizeAndPlayer() {
        if (this.model.getArrayList().size() > 0) {
            openPlayer();
        } else {
            AppConstants.toastShort(this.context, getString(R.string.player_list_is_empty));
        }
    }

    private void openPlayer() {
        Intent intent = new Intent(this.context, AffirmPlayerActivity.class);
        intent.putParcelableArrayListExtra(AffirmPlayerActivity.EXTRA_LIST, this.model.getArrayList());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void enableDrag(boolean z) {
        if (getActivity() instanceof AffirmationActivity) {
            ((AffirmationActivity) getActivity()).imgOther.setImageResource(z ? R.drawable.done : R.drawable.edit);
        }
        for (int i = 0; i < this.model.getArrayList().size(); i++) {
            this.model.getArrayList().get(i).setEnableDrag(z);
        }
        notifyAdapter(false);
    }


    public void fillData() {
        resetListData(false);
    }

    private void resetListData(final boolean z) {
        new BackgroundAsync(this.context, false, "", new OnAsyncBackground() {
            public void onPreExecute() {
            }

            public void doInBackground() {
                if (!z) {
                    refillAffirmationList();
                    refillFolderList();
                    return;
                }
                refillFolderList();
            }

            public void onPostExecute() {
                notifyAdapter(!z);
            }
        }).execute(new Object[0]);
    }


    public void refillAffirmationList() {
        List<AffirmationRowModel> list2;
        this.model.getArrayList().clear();
        FolderRowModel folderRowModel = null;
        try {
            list2 = this.db.affirmationDao().getAllActiveList();
        } catch (Exception e) {
            e.printStackTrace();
            list2 = null;
        }
        for (int i = 0; i < list2.size(); i++) {
            AffirmationRowModel affirmationRowModel = list2.get(i);
            if (folderRowModel == null) {
                try {
                    folderRowModel = this.db.folderDao().getDetail(affirmationRowModel.getFolderId());
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            if (folderRowModel.getId().equalsIgnoreCase(affirmationRowModel.getFolderId())) {
                affirmationRowModel.setFolderRowModel(folderRowModel);
            } else {
                try {
                    folderRowModel = this.db.folderDao().getDetail(affirmationRowModel.getFolderId());
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
                affirmationRowModel.setFolderRowModel(folderRowModel);
            }
            this.model.getArrayList().add(affirmationRowModel);
        }
    }


    public void refillFolderList() {
        this.model.getFolderList().clear();
        try {
            this.model.getFolderList().addAll(this.db.folderDao().getActiveListWithCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setRecycler() {
        this.binding.recyclerFolder.setNestedScrollingEnabled(false);
        this.binding.recyclerFolder.setLayoutManager(new GridLayoutManager(this.context, 3));
        this.binding.recyclerFolder.addItemDecoration(new ItemOffsetDecoration(this.context, R.dimen.cardItemOffset));
        this.binding.recyclerFolder.setAdapter(new FolderAdapter(1, this.context, this.model.getFolderList(), new RecyclerItemClick() {
            public void onClick(int i, int i2) {
                AllActiveListFragment allActiveListFragment = AllActiveListFragment.this;
                allActiveListFragment.openList(allActiveListFragment.model.getFolderList().get(i));
            }
        }));
        this.binding.recycler.setNestedScrollingEnabled(false);
        this.binding.recycler.setLayoutManager(new LinearLayoutManager(this.context));
        AffirmationAdapter affirmationAdapter = new AffirmationAdapter(false, this.context, this.model.getArrayList(), new RecyclerItemClick() {
            public void onClick(int i, int i2) {
                AllActiveListFragment allActiveListFragment = AllActiveListFragment.this;
                allActiveListFragment.openDetail(i, allActiveListFragment.model.getArrayList().get(i), true);
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeAndDragHelper(affirmationAdapter));
        affirmationAdapter.setTouchHelper(itemTouchHelper);
        this.binding.recycler.setAdapter(affirmationAdapter);
        itemTouchHelper.attachToRecyclerView(this.binding.recycler);
        this.binding.scrollRoot.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            public void onScrollChange(NestedScrollView nestedScrollView, int i, int i2, int i3, int i4) {
                if (i2 > i4) {
                    binding.fabPlay.hide();
                } else {
                    binding.fabPlay.show();
                }
            }
        });
    }


    public void openList(FolderRowModel folderRowModel) {
        Intent intent = new Intent(this.context, FolderAffirmationListActivity.class);
        intent.putExtra(FolderAffirmationListActivity.EXTRA_MODEL, folderRowModel);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, 1001);
    }


    public void openDetail(int i, AffirmationRowModel affirmationRowModel, boolean z) {
        Intent intent = new Intent(this.context, AddEditAffirmationActivity.class);
        intent.putExtra(AddEditAffirmationActivity.EXTRA_IS_EDIT, z);
        intent.putExtra(AddEditAffirmationActivity.EXTRA_POSITION, i);
        intent.putExtra(AddEditAffirmationActivity.EXTRA_MODEL, affirmationRowModel);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, 1002);
    }


    public void notifyAdapter(boolean z) {
        setViewVisibility();
        if (this.binding.recycler.getAdapter() != null) {
            this.binding.recycler.getAdapter().notifyDataSetChanged();
        }
        if (z && this.binding.recyclerFolder.getAdapter() != null) {
            this.binding.recyclerFolder.getAdapter().notifyDataSetChanged();
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
            this.model.setDragEnable(false);
            enableDrag(this.model.isDragEnable());
            switch (i) {
                case 1001:
                    resetListData(false);
                    return;
                case 1002:
                    updateList(intent);
                    return;
                default:
                    return;
            }
        }
    }

    private void updateList(Intent intent) {
        if (intent != null) {
            try {
                if (intent.hasExtra(AddEditAffirmationActivity.EXTRA_MODEL)) {
                    AffirmationRowModel affirmationRowModel = (AffirmationRowModel) intent.getParcelableExtra(AddEditAffirmationActivity.EXTRA_MODEL);
                    if (intent.getBooleanExtra(AddEditAffirmationActivity.EXTRA_IS_DELETED, false)) {
                        this.model.getArrayList().remove(intent.getIntExtra(AddEditAffirmationActivity.EXTRA_POSITION, 0));
                        resetListData(true);
                    } else if (!intent.getBooleanExtra(AddEditAffirmationActivity.EXTRA_IS_EDIT, false)) {
                        this.model.getArrayList().add(affirmationRowModel);
                        resetListData(true);
                    } else if (!affirmationRowModel.getFolderId().equalsIgnoreCase(this.model.getArrayList().get(intent.getIntExtra(AddEditAffirmationActivity.EXTRA_POSITION, 0)).getFolderId())) {
                        this.model.getArrayList().set(intent.getIntExtra(AddEditAffirmationActivity.EXTRA_POSITION, 0), affirmationRowModel);
                        resetListData(true);
                    } else {
                        this.model.getArrayList().set(intent.getIntExtra(AddEditAffirmationActivity.EXTRA_POSITION, 0), affirmationRowModel);
                        notifyAdapter(false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
