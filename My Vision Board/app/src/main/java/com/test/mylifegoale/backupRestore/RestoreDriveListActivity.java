package com.test.mylifegoale.backupRestore;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.test.mylifegoale.R;
import com.test.mylifegoale.base.BaseActivity;
import com.test.mylifegoale.databinding.ActivityRestoreListBinding;
import com.test.mylifegoale.model.toolbar.ToolbarModel;
import com.test.mylifegoale.utilities.AppConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RestoreDriveListActivity extends BaseActivity {
    private BackupRestore backupRestore;

    public ActivityRestoreListBinding binding;
    Context context;

    public boolean isDesc;

    public boolean isResultOK;

    public RestoreListModel model;
    private BackupRestoreProgress progressDialog;
    private ToolbarModel toolbarModel;

    public void setBinding() {
        this.binding = (ActivityRestoreListBinding) DataBindingUtil.setContentView(this, R.layout.activity_restore_list);
        this.model = new RestoreListModel();
        this.model.setArrayList(new ArrayList());
        this.model.setNoDataIcon(R.drawable.no_data);
        this.model.setNoDataText(getString(R.string.noDataTitleBackup));
        this.model.setNoDataDetail(getString(R.string.noDataDescBackup));
        this.binding.setRestoreListModel(this.model);
        this.backupRestore = new BackupRestore(this);
        this.progressDialog = new BackupRestoreProgress(this);
    }

    public void init() {
        this.context = this;
        setRecycler();
        getDriveBackupList();
        this.binding.txtPath.setVisibility(View.GONE);
    }

    public void setToolbar() {
        this.toolbarModel = new ToolbarModel();
        this.toolbarModel.setTitle(getString(R.string.drive_backups));
        this.binding.includedToolbar.setToolbarModel(this.toolbarModel);
        setOnClicks();
    }

    public void setOnClicks() {
        this.binding.includedToolbar.imgBack.setOnClickListener(this);
        this.binding.includedToolbar.imgAdd.setOnClickListener(this);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.imgAdd) {
            this.isDesc = !this.isDesc;
            shortList();
        } else if (id == R.id.imgBack) {
            onBackPressed();
        }
    }

    private void getDriveBackupList() {
        this.backupRestore.driveBackupList(this.progressDialog, new OnBackupRestore() {
            public void onSuccess(boolean z) {
            }

            public void getList(ArrayList<RestoreRowModel> arrayList) {
                model.getArrayList().addAll(arrayList);
                notifyAdapter();
            }
        });
    }

    private void shortList() {
        this.binding.includedToolbar.imgAdd.setImageResource(this.isDesc ? R.drawable.sort_down : R.drawable.sort_up);
        Collections.sort(this.model.getArrayList(), new Comparator<RestoreRowModel>() {
            @SuppressLint({"NewApi"})
            public int compare(RestoreRowModel restoreRowModel, RestoreRowModel restoreRowModel2) {
                if (isDesc) {
                    return Long.compare(restoreRowModel.getTimestamp().longValue(), restoreRowModel2.getTimestamp().longValue());
                }
                return Long.compare(restoreRowModel2.getTimestamp().longValue(), restoreRowModel.getTimestamp().longValue());
            }
        });
        notifyAdapter();
    }


    public void setRecycler() {
        this.binding.recycler.setLayoutManager(new LinearLayoutManager(this.context));
        this.binding.recycler.setAdapter(new RestoreAdapter(this.context, this.model.getArrayList(), new OnRecyclerItemClick() {
            public void onClick(int i, int i2) {
                if (i2 == 2) {
                    deleteItem(i);
                } else {
                    restoreItem(i);
                }
            }
        }));
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

    public void deleteItem(final int i) {
        AppConstants.showTwoButtonDialog(this.context, getString(R.string.app_name), getString(R.string.delete_msg) + "<br /> <b>This Backup</b> <br />", true, true, getString(R.string.delete), getString(R.string.cancel), (OnTwoButtonDialogClick) new OnTwoButtonDialogClick() {
            public void onCancel() {
            }

            public void onOk() {
                deleteFile(i);
            }
        });
    }


    public void deleteFile(final int i) {
        this.backupRestore.deleteFromDrive(this.progressDialog, this.model.getArrayList().get(i).getPath(), new OnBackupRestore() {
            public void getList(ArrayList<RestoreRowModel> arrayList) {
            }

            public void onSuccess(boolean z) {
                if (z) {
                    model.getArrayList().remove(i);
                    binding.recycler.getAdapter().notifyItemRemoved(i);
                    AppConstants.toastShort(context, "File delete");
                    notifyAdapter();
                    return;
                }
                AppConstants.toastShort(context, "Unable to delete");
            }
        });
    }

    public void restoreItem(final int i) {
        AppConstants.showRestoreDialog(this.context, "<b>" + getString(R.string.restore_msg) + "</b>", true, true, new OnTwoButtonDialogClick() {
            public void onOk() {
                isResultOK = true;
                backupData(model.getArrayList().get(i).getPath(), false);
            }

            public void onCancel() {
                isResultOK = true;
                backupData(model.getArrayList().get(i).getPath(), true);
            }
        });
    }


    public void backupData(String str, boolean z) {
        this.backupRestore.backupRestore(this.progressDialog, false, false, str, z, new OnBackupRestore() {
            public void getList(ArrayList<RestoreRowModel> arrayList) {
            }

            public void onSuccess(boolean z) {
                if (z) {
                    AppConstants.toastShort(context, context.getString(R.string.import_successfully));
                } else {
                    AppConstants.toastShort(context, context.getString(R.string.failed_to_export));
                }
            }
        });
    }


    @Override
    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1 && i == 1005) {
            handleSignIn(intent);
        }
    }

    private void handleSignIn(Intent intent) {
        this.backupRestore.handleSignInResult(intent, true, true, (String) null, this.progressDialog, new OnBackupRestore() {
            public void onSuccess(boolean z) {
            }

            public void getList(ArrayList<RestoreRowModel> arrayList) {
                model.getArrayList().addAll(arrayList);
                notifyAdapter();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (this.isResultOK) {
            setResult(-1);
        }
        finish();
    }
}
