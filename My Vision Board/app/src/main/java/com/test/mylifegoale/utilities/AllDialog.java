package com.test.mylifegoale.utilities;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.test.mylifegoale.R;
import com.test.mylifegoale.databinding.DeleteDialogBinding;
import com.test.mylifegoale.itemClick.DialogClick;

public class AllDialog {
    public void callDialog(String str, String str2, String str3, String str4, Activity activity, final DialogClick dialogClick) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        DeleteDialogBinding deleteDialogBinding = (DeleteDialogBinding) DataBindingUtil.inflate(activity.getLayoutInflater(), R.layout.delete_dialog, (ViewGroup) null, false);
        builder.setView(deleteDialogBinding.getRoot());
        final AlertDialog create = builder.create();
        create.getWindow().setBackgroundDrawableResource(17170445);
        if (!str3.isEmpty()) {
            deleteDialogBinding.okAction.setText(str3);
        }
        if (!str4.isEmpty()) {
            deleteDialogBinding.cancelAction.setText(str4);
        }
        if (!str2.isEmpty()) {
            deleteDialogBinding.message.setText(str2);
        }
        if (!str.isEmpty()) {
            deleteDialogBinding.title.setText(str);
        }
        deleteDialogBinding.okAction.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                create.dismiss();
                dialogClick.onPositiveClick();
            }
        });
        deleteDialogBinding.cancelAction.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                create.dismiss();
                dialogClick.onNegetiveClick();
            }
        });
        create.show();
    }
}
