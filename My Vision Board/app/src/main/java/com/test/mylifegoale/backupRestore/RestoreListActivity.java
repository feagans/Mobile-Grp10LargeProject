package com.test.mylifegoale.backupRestore;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.test.mylifegoale.R;
import com.test.mylifegoale.base.BaseActivity;
import com.test.mylifegoale.databinding.ActivityRestoreListBinding;
import com.test.mylifegoale.itemClick.OnAsyncBackground;
import com.test.mylifegoale.model.toolbar.ToolbarModel;
import com.test.mylifegoale.utilities.AppConstants;
import com.test.mylifegoale.utilities.BackgroundAsync;
import com.test.mylifegoale.utilities.Constants;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class RestoreListActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {
    private BackupRestore backupRestore;
    private ActivityRestoreListBinding binding;

    public boolean isDesc;

    public boolean isResultOK;

    public RestoreListModel model;
    private BackupRestoreProgress progressDialog;
    private ToolbarModel toolbarModel;

    public void onRationaleAccepted(int i) {
    }

    public void onRationaleDenied(int i) {
    }

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
        setOnClicks();
        checkPermAndFill();
        setRecycler();
    }

    public void setToolbar() {
        this.toolbarModel = new ToolbarModel();
        this.toolbarModel.setTitle(getString(R.string.local_backups));
        this.toolbarModel.setAdd(true);
        this.binding.includedToolbar.setToolbarModel(this.toolbarModel);
    }


    public void setOnClicks() {
        this.binding.includedToolbar.imgBack.setOnClickListener(this);
        this.binding.includedToolbar.imgAdd.setOnClickListener(this);
    }

    private void checkPermAndFill() {
        if (isHasPermissions(this, "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE")) {
            fillList();
        } else {
            requestPermissions(this, getString(R.string.rationale_save), Constants.REQUEST_PERM_FILE, "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE");
        }
    }

    private void fillList() {
        new BackgroundAsync(this, true, "", new OnAsyncBackground() {
            public void onPreExecute() {
            }

            public void doInBackground() {
                try {
                    File[] listFiles = new File(AppConstants.getLocalFileDir()).listFiles();
                    if (listFiles != null) {
                        for (int i = 0; i < listFiles.length; i++) {
                            if (FilenameUtils.getExtension(listFiles[i].getName()).equalsIgnoreCase("zip")) {
                                RestoreRowModel restoreRowModel = new RestoreRowModel();
                                restoreRowModel.setTitle(listFiles[i].getName());
                                restoreRowModel.setPath(listFiles[i].getAbsolutePath());
                                restoreRowModel.setDateModified(AppConstants.getFormattedDate(listFiles[i].lastModified(), Constants.FILE_DATE_FORMAT));
                                long length = listFiles[i].length() / FileUtils.ONE_KB;
                                restoreRowModel.setSize(length + "KB");
                                restoreRowModel.setTimestamp(Long.valueOf(listFiles[i].lastModified()));
                                model.getArrayList().add(restoreRowModel);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onPostExecute() {
                shortList();
            }
        }).execute(new Object[0]);
    }


    public void shortList() {
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
        this.binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        this.binding.recycler.setAdapter(new RestoreAdapter(this, this.model.getArrayList(), new OnRecyclerItemClick() {
            public void onClick(int i, int i2) {
                if (i2 == 2) {
                    deleteItem(i);
                } else {
                    restoreItem(i);
                }
            }
        }));
    }

    private void notifyAdapter() {
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
        AppConstants.showTwoButtonDialog((Context) this, getString(R.string.app_name), getString(R.string.delete_msg) + "<br /> <b>This Backup</b> <br />", true, true, getString(R.string.delete), getString(R.string.cancel), (OnTwoButtonDialogClick) new OnTwoButtonDialogClick() {
            public void onCancel() {
            }

            public void onOk() {
                deleteFile(i);
            }
        });
    }


    public void deleteFile(int i) {
        File file = new File(this.model.getArrayList().get(i).getPath());
        try {
            if (!file.exists()) {
                return;
            }
            if (file.delete()) {
                this.model.getArrayList().remove(i);
                this.binding.recycler.getAdapter().notifyItemRemoved(i);
                AppConstants.toastShort(this, "File delete");
                notifyAdapter();
                return;
            }
            AppConstants.toastShort(this, "Unable to delete");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void restoreItem(final int i) {
        AppConstants.showRestoreDialog(this, "<b>" + getString(R.string.restore_msg) + "</b>", true, true, new OnTwoButtonDialogClick() {
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
        this.backupRestore.backupRestore(this.progressDialog, true, false, str, z, new OnBackupRestore() {
            public void getList(ArrayList<RestoreRowModel> arrayList) {
            }

            public void onSuccess(boolean z) {
                if (z) {
                    AppConstants.toastShort(RestoreListActivity.this, getString(R.string.import_successfully));
                    return;
                }
                AppConstants.toastShort(RestoreListActivity.this, getString(R.string.failed_to_import));
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
            fillList();
        }
    }

    public void onPermissionsDenied(int i, @NonNull List<String> list) {
        if (EasyPermissions.somePermissionPermanentlyDenied((Activity) this, list)) {
            new AppSettingsDialog.Builder((Activity) this).build().show();
        }
    }

    @Override
    public void onBackPressed() {
        if (this.isResultOK) {
            setResult(-1);
        }
        finish();
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
}
