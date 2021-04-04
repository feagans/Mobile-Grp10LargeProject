package com.test.mylifegoale.backupRestore;

import android.app.Activity;
import android.app.Dialog;
import android.widget.TextView;

import com.test.mylifegoale.R;

import java.lang.ref.WeakReference;

public class BackupRestoreProgress {
    boolean isShowing = false;
    private Dialog progressDialog = null;
    TextView textView;
    WeakReference<Activity> weakReference;

    public BackupRestoreProgress(Activity activity2) {
        this.progressDialog = new Dialog(activity2);
        this.weakReference = new WeakReference<>(activity2);
        this.progressDialog.setContentView(R.layout.bacup_restore_progress_dialog);
        this.textView = (TextView) this.progressDialog.findViewById(R.id.message);
        this.progressDialog.setCancelable(false);
    }

    public void showDialog() {
        Dialog dialog = this.progressDialog;
        if (dialog != null && !this.isShowing) {
            dialog.show();
            this.isShowing = true;
        }
    }

    public void dismissDialog() {
        Dialog dialog = this.progressDialog;
        if (dialog != null && dialog.isShowing() && this.weakReference.get() != null) {
            this.progressDialog.dismiss();
            this.isShowing = false;
        }
    }

    public void setMessage(String str) {
        this.textView.setText(str);
    }

}
