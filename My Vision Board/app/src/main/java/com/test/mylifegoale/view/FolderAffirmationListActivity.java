package com.test.mylifegoale.view;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.mylifegoale.R;
import com.test.mylifegoale.adapters.AffirmationAdapter;
import com.test.mylifegoale.base.roomDb.AppDatabase;
import com.test.mylifegoale.baseClass.BaseActivityRecyclerBinding;
import com.test.mylifegoale.databinding.ActivityFolderAffirmationListBinding;
import com.test.mylifegoale.itemClick.OnAsyncBackground;
import com.test.mylifegoale.itemClick.RecyclerItemClick;
import com.test.mylifegoale.itemClick.TwoButtonDialogListener;
import com.test.mylifegoale.model.AffirmationRowModel;
import com.test.mylifegoale.model.FolderRowModel;
import com.test.mylifegoale.model.affirm.AffirmListModel;
import com.test.mylifegoale.model.toolbar.ToolbarModel;
import com.test.mylifegoale.utilities.AppConstants;
import com.test.mylifegoale.utilities.AppPref;
import com.test.mylifegoale.utilities.BackgroundAsync;
import com.test.mylifegoale.utilities.SwipeAndDragHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FolderAffirmationListActivity extends BaseActivityRecyclerBinding {
    public static final String EXTRA_MODEL = "EXTRA_MODEL";
    public static final String EXTRA_POSITION = "EXTRA_POSITION";

    public ArrayList<AffirmationRowModel> activeList;

    public ActivityFolderAffirmationListBinding binding;

    public AppDatabase db;
    private boolean isUpdateAllCount;

    public boolean isUpdateCheckCount;

    public AffirmListModel model;
    private ArrayList<AffirmationRowModel> playerList;

    public FolderRowModel rowModelFolder;
    public ToolbarModel toolbarModel;


    public void callApi() {
    }


    public void initMethods() {
    }


    public void setBinding() {
        this.binding = (ActivityFolderAffirmationListBinding) DataBindingUtil.setContentView(this, R.layout.activity_folder_affirmation_list);
        this.db = AppDatabase.getAppDatabase(this.context);
        setModelDetail();
    }

    private void setModelDetail() {
        this.model = new AffirmListModel();
        this.model.setArrayList(new ArrayList());
        this.activeList = new ArrayList<>();
        this.playerList = new ArrayList<>();
        this.model.setNoDataIcon(R.drawable.affirmations);
        this.model.setNoDataText(getString(R.string.noDataTitleFolderList));
        this.model.setNoDataDetail(getString(R.string.noDataDescFolderList));
        this.binding.setAffirmListModel(this.model);
        if (getIntent() != null && getIntent().hasExtra(EXTRA_MODEL)) {
            this.rowModelFolder = (FolderRowModel) getIntent().getParcelableExtra(EXTRA_MODEL);
        }
    }


    public void setToolbar() {
        this.toolbarModel = new ToolbarModel();
        this.toolbarModel.setTitle(this.rowModelFolder.getName());
        this.toolbarModel.setAdd(true);
        this.toolbarModel.setOtherMenu(true);
        this.binding.includedToolbar.setToolbarModel(this.toolbarModel);
        this.binding.includedToolbar.imgOther.setImageResource(this.model.isDragEnable() ? R.drawable.done : R.drawable.edit);
        setSupportActionBar(this.binding.includedToolbar.toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_folder_affirmation_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.checkAll) {
            checkAll(true);
            return true;
        } else if (itemId == R.id.deleteFolder) {
            deleteItemDialog();
            return true;
        } else if (itemId == R.id.editFolder) {
            editDetail(-1, this.rowModelFolder, true);
            return true;
        } else if (itemId != R.id.unCheckAll) {
            return super.onOptionsItemSelected(menuItem);
        } else {
            checkAll(false);
            return true;
        }
    }

    private void checkAll(boolean z) {
        for (int i = 0; i < this.model.getArrayList().size(); i++) {
            this.model.getArrayList().get(i).setActive(z);
            if (z && this.model.getArrayList().get(i).getSequence() == -1) {
                try {
                    this.model.getArrayList().get(i).setSequence(this.db.affirmationDao().getFolderListActiveCount());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                this.db.affirmationDao().update(this.model.getArrayList().get(i));
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        this.isUpdateAllCount = true;
        if (z) {
            this.activeList.clear();
            this.activeList.addAll(this.model.getArrayList());
            return;
        }
        this.activeList.clear();
    }

    private void editDetail(int i, FolderRowModel folderRowModel, boolean z) {
        Intent intent = new Intent(this.context, AddEditFolderActivity.class);
        intent.putExtra(AddEditFolderActivity.EXTRA_IS_EDIT, z);
        intent.putExtra(AddEditFolderActivity.EXTRA_POSITION, i);
        intent.putExtra(AddEditFolderActivity.EXTRA_MODEL, folderRowModel);
        intent.putExtra(AddEditFolderActivity.EXTRA_IS_DELETEABLE, false);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, 1003);
    }

    public void deleteItemDialog() {
        AppConstants.showTwoButtonDialog(this.context, getString(R.string.app_name), getString(R.string.delete_msg) + "<br /> <b>" + this.rowModelFolder.getName() + "</b>", true, true, getString(R.string.delete), getString(R.string.cancel), (TwoButtonDialogListener) new TwoButtonDialogListener() {
            public void onCancel() {
            }

            public void onOk() {
                deleteItem();
            }
        });
    }


    public void deleteItem() {
        try {
            this.db.affirmationDao().deleteFolder(this.rowModelFolder.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.db.folderDao().delete(this.rowModelFolder);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        this.isUpdateAllCount = true;
        onBackPressed();
    }


    public void setOnClicks() {
        this.binding.includedToolbar.imgBack.setOnClickListener(this);
        this.binding.includedToolbar.imgAdd.setOnClickListener(this);
        this.binding.includedToolbar.imgOther.setOnClickListener(this);
        this.binding.fabAdd.setOnClickListener(this);
        this.binding.fabPlay.setOnClickListener(this);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.fabPlay) {
            checkSizeAndPlayer();
        } else if (id == R.id.imgAdd) {
            addItem();
        } else if (id == R.id.imgBack) {
            onBackPressed();
        } else if (id == R.id.imgOther) {
            AffirmListModel affirmListModel = this.model;
            affirmListModel.setDragEnable(!affirmListModel.isDragEnable());
            enableDrag(this.model.isDragEnable());
        }
    }

    private void checkSizeAndPlayer() {
        fillPlayerList();
        if (this.playerList.size() > 0) {
            openPlayer();
        } else {
            AppConstants.toastShort(this.context, getString(R.string.player_list_is_empty));
        }
    }

    private void fillPlayerList() {
        this.playerList.clear();
        if (AppPref.isPlayAllInFolder(this.context)) {
            this.playerList.addAll(this.model.getArrayList());
        } else {
            getAcitiveList();
        }
    }

    private void getAcitiveList() {
        for (int i = 0; i < this.model.getArrayList().size(); i++) {
            if (this.model.getArrayList().get(i).isActive()) {
                this.playerList.add(this.model.getArrayList().get(i));
            }
        }
    }

    private void openPlayer() {
        sortBySequence();
        Intent intent = new Intent(this.context, AffirmPlayerActivity.class);
        intent.putParcelableArrayListExtra(AffirmPlayerActivity.EXTRA_LIST, this.playerList);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void sortBySequence() {
        Collections.sort(this.playerList, new Comparator<AffirmationRowModel>() {
            public int compare(AffirmationRowModel affirmationRowModel, AffirmationRowModel affirmationRowModel2) {
                return affirmationRowModel.getSequenceFolder() > affirmationRowModel2.getSequenceFolder() ? 1 : -1;
            }
        });
    }

    private void addItem() {
        AffirmationRowModel affirmationRowModel = new AffirmationRowModel();
        affirmationRowModel.setFolderId(this.rowModelFolder.getId());
        affirmationRowModel.setFolderRowModel(this.rowModelFolder);
        affirmationRowModel.setActive(true);
        openDetail(-1, affirmationRowModel, false);
    }

    private void enableDrag(boolean z) {
        this.binding.includedToolbar.imgOther.setImageResource(z ? R.drawable.done : R.drawable.edit);
        for (int i = 0; i < this.model.getArrayList().size(); i++) {
            this.model.getArrayList().get(i).setEnableDrag(z);
        }
        notifyAdapter();
    }


    public void fillData() {
        new BackgroundAsync(this.context, false, "", new OnAsyncBackground() {
            public void onPreExecute() {
            }

            public void doInBackground() {
                List<AffirmationRowModel> list;
                try {
                    list = db.affirmationDao().getFolderList(rowModelFolder.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                    list = null;
                }
                for (int i = 0; i < list.size(); i++) {
                    AffirmationRowModel affirmationRowModel = list.get(i);
                    affirmationRowModel.setFolderRowModel(rowModelFolder);
                    model.getArrayList().add(affirmationRowModel);
                    if (affirmationRowModel.isActive()) {
                        activeList.add(affirmationRowModel);
                    }
                }
            }

            public void onPostExecute() {
                notifyAdapter();
            }
        }).execute(new Object[0]);
    }


    public void setRecycler() {
        this.binding.recycler.setLayoutManager(new LinearLayoutManager(this.context));
        AffirmationAdapter affirmationAdapter = new AffirmationAdapter(true, this.context, this.model.getArrayList(), new RecyclerItemClick() {
            public void onClick(int i, int i2) {
                if (i2 == 2) {
                    if (model.getArrayList().get(i).isActive()) {
                        activeList.remove(model.getArrayList().get(i));
                    } else {
                        activeList.add(model.getArrayList().get(i));
                    }
                    model.getArrayList().get(i).setActive(!model.getArrayList().get(i).isActive());
                    if (model.getArrayList().get(i).getSequence() == -1) {
                        try {
                            model.getArrayList().get(i).setSequence(db.affirmationDao().getFolderListActiveCount());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        db.affirmationDao().update(model.getArrayList().get(i));
                        isUpdateCheckCount = true;
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                } else {
                    FolderAffirmationListActivity folderAffirmationListActivity = FolderAffirmationListActivity.this;
                    folderAffirmationListActivity.openDetail(i, folderAffirmationListActivity.model.getArrayList().get(i), true);
                }
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeAndDragHelper(affirmationAdapter));
        affirmationAdapter.setTouchHelper(itemTouchHelper);
        this.binding.recycler.setAdapter(affirmationAdapter);
        itemTouchHelper.attachToRecyclerView(this.binding.recycler);
        this.binding.recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                super.onScrolled(recyclerView, i, i2);
                if (i2 > 0 && binding.fabPlay.getVisibility() == 0) {
                    binding.fabPlay.hide();
                } else if (i2 < 0 && binding.fabPlay.getVisibility() != 0) {
                    binding.fabPlay.show();
                }
            }
        });
    }


    public void openDetail(int i, AffirmationRowModel affirmationRowModel, boolean z) {
        Intent intent = new Intent(this.context, AddEditAffirmationActivity.class);
        intent.putExtra(AddEditAffirmationActivity.EXTRA_IS_EDIT, z);
        intent.putExtra(AddEditAffirmationActivity.EXTRA_POSITION, i);
        intent.putExtra(AddEditAffirmationActivity.EXTRA_MODEL, affirmationRowModel);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, 1002);
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
            this.model.setDragEnable(false);
            enableDrag(this.model.isDragEnable());
            switch (i) {
                case 1002:
                    updateList(intent);
                    return;
                case 1003:
                    updateFolderDetail(intent);
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
                    if (!intent.getBooleanExtra(AddEditAffirmationActivity.EXTRA_IS_DELETED, false)) {
                        if (affirmationRowModel.getFolderId().equalsIgnoreCase(this.rowModelFolder.getId())) {
                            if (intent.getBooleanExtra(AddEditAffirmationActivity.EXTRA_IS_EDIT, false)) {
                                this.model.getArrayList().set(intent.getIntExtra(AddEditAffirmationActivity.EXTRA_POSITION, 0), affirmationRowModel);
                                this.isUpdateAllCount = true;
                            } else {
                                this.model.getArrayList().add(affirmationRowModel);
                                this.isUpdateAllCount = true;
                            }
                            notifyAdapter();
                        }
                    }
                    if (intent.getIntExtra(AddEditAffirmationActivity.EXTRA_POSITION, 0) != -1) {
                        this.model.getArrayList().remove(intent.getIntExtra(AddEditAffirmationActivity.EXTRA_POSITION, 0));
                    }
                    this.isUpdateAllCount = true;
                    notifyAdapter();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateFolderDetail(Intent intent) {
        if (intent != null) {
            try {
                if (intent.hasExtra(AddEditAffirmationActivity.EXTRA_MODEL)) {
                    FolderRowModel folderRowModel = (FolderRowModel) intent.getParcelableExtra(AddEditAffirmationActivity.EXTRA_MODEL);
                    if (intent.getBooleanExtra(AddEditAffirmationActivity.EXTRA_IS_DELETED, false)) {
                        deleteItem();
                        this.isUpdateAllCount = true;
                    } else if (intent.getBooleanExtra(AddEditAffirmationActivity.EXTRA_IS_EDIT, false)) {
                        if (!folderRowModel.getColorCode().equalsIgnoreCase(this.rowModelFolder.getColorCode())) {
                            resetListData(folderRowModel);
                        } else {
                            this.rowModelFolder = folderRowModel;
                        }
                        this.toolbarModel.setTitle(this.rowModelFolder.getName());
                        this.isUpdateAllCount = true;
                    }
                    notifyAdapter();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void resetListData(FolderRowModel folderRowModel) {
        this.rowModelFolder = folderRowModel;
        this.model.getArrayList().clear();
        this.activeList.clear();
        fillData();
    }

    @Override
    public void onBackPressed() {
        if (this.isUpdateAllCount || this.isUpdateCheckCount) {
            openList();
        }
        super.onBackPressed();
    }

    private void openList() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_POSITION, getIntent().getIntExtra(AddEditAffirmationActivity.EXTRA_POSITION, 0));
        setResult(-1, intent);
        finish();
    }
}
