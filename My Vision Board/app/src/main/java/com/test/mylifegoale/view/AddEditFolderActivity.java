package com.test.mylifegoale.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.test.mylifegoale.R;
import com.test.mylifegoale.adapters.ImageAdapter;
import com.test.mylifegoale.base.roomDb.AppDatabase;
import com.test.mylifegoale.baseClass.BaseActivityBinding;
import com.test.mylifegoale.databinding.ActivityFolderAddEditBinding;
import com.test.mylifegoale.databinding.AlertDialogRecyclerListBinding;
import com.test.mylifegoale.itemClick.RecyclerItemClick;
import com.test.mylifegoale.itemClick.TwoButtonDialogListener;
import com.test.mylifegoale.model.FolderRowModel;
import com.test.mylifegoale.model.image.ImageRowModel;
import com.test.mylifegoale.model.toolbar.ToolbarModel;
import com.test.mylifegoale.utilities.AdConstants;
import com.test.mylifegoale.utilities.AppConstants;
import com.test.mylifegoale.utilities.Constants;
import com.test.mylifegoale.utilities.ItemOffsetDecoration;

import java.util.ArrayList;

public class AddEditFolderActivity extends BaseActivityBinding {
    public static String EXTRA_IS_DELETEABLE = "EXTRA_IS_DELETEABLE";
    public static String EXTRA_IS_DELETED = "isDeleted";
    public static String EXTRA_IS_EDIT = "isEdit";
    public static String EXTRA_MODEL = "model";
    public static String EXTRA_POSITION = "position";
    private ActivityFolderAddEditBinding binding;
    private AppDatabase db;

    public Dialog dialogFolderList;
    private AlertDialogRecyclerListBinding dialogFolderListBinding;

    public ArrayList<ImageRowModel> imageList;
    private boolean isEdit = false;

    public FolderRowModel model;
    private FolderRowModel oldModel;

    public int selectedFolderPos = 0;
    public ToolbarModel toolbarModel;


    public void setBinding() {
        this.binding = (ActivityFolderAddEditBinding) DataBindingUtil.setContentView(this, R.layout.activity_folder_add_edit);
        this.db = AppDatabase.getAppDatabase(this);
        setModelDetail();
        this.binding.setFolderRowModel(this.model);
        AdConstants ads = new AdConstants();
        ads.loadNativeAd(this, binding.nativeadcontainer);

    }

    private void setModelDetail() {
        if (getIntent() != null && getIntent().hasExtra(EXTRA_MODEL)) {
            this.isEdit = getIntent().hasExtra(EXTRA_IS_EDIT) && getIntent().getBooleanExtra(EXTRA_IS_EDIT, false);
            if (this.isEdit) {
                this.model = (FolderRowModel) getIntent().getParcelableExtra(EXTRA_MODEL);
                this.model.setDefault(false);
            } else {
                this.model = (FolderRowModel) getIntent().getParcelableExtra(EXTRA_MODEL);
                this.model.setId(AppConstants.getUniqueId());
            }
        }
        this.oldModel = new FolderRowModel(this.model);
        this.binding.etAffirmation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                model.setName(binding.etAffirmation.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    public void setToolbar() {
        this.toolbarModel = new ToolbarModel();
        this.toolbarModel.setTitle(this.isEdit ? "Edit" : "Add");
        if (getIntent() == null || !getIntent().hasExtra(EXTRA_IS_DELETEABLE)) {
            this.toolbarModel.setDelete(this.isEdit);
        } else {
            this.toolbarModel.setDelete(false);
        }
        this.toolbarModel.setOtherMenu(true);
        this.binding.includedToolbar.setToolbarModel(this.toolbarModel);
        this.binding.includedToolbar.imgOther.setImageResource(R.drawable.save);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_edit, menu);
        menu.findItem(R.id.delete).setVisible(this.isEdit);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != R.id.delete) {
            return super.onOptionsItemSelected(menuItem);
        }
        deleteItemDialog();
        return true;
    }

    public void deleteItemDialog() {
        AppConstants.showTwoButtonDialog(this.context, getString(R.string.app_name), getString(R.string.delete_msg) + "<br /> <b>" + this.model.getName() + "</b>", true, true, getString(R.string.delete), getString(R.string.cancel), (TwoButtonDialogListener) new TwoButtonDialogListener() {
            public void onCancel() {
            }

            public void onOk() {
                deleteItem();
            }
        });
    }


    public void deleteItem() {
        try {
            this.db.affirmationDao().deleteFolder(this.model.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.db.folderDao().delete(this.model);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        openList(true);
    }


    public void setOnClicks() {
        this.binding.includedToolbar.imgBack.setOnClickListener(this);
        this.binding.includedToolbar.imgDelete.setOnClickListener(this);
        this.binding.includedToolbar.imgOther.setOnClickListener(this);
        this.binding.btnAddEdit.setOnClickListener(this);
        this.binding.cardIcon.setOnClickListener(this);
        this.binding.cardColor.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cardColor:
                showColorPickerDialog();
                return;
            case R.id.cardIcon:
                showIconPicker();
                return;
            case R.id.imgBack:
                onBackPressed();
                return;
            case R.id.imgDelete:
                deleteItemDialog();
                return;
            case R.id.imgOther:
                AddUpdate();
                return;
            default:
                return;
        }
    }


    public void initMethods() {
        folderDialogSetup();
    }

    private void AddUpdate() {
        checkValidation();
    }


    public void checkValidation() {
        if (this.model.getName() == null || this.model.getName().trim().length() <= 0) {
            this.binding.etAffirmation.requestFocus();
            AppConstants.toastShort(this.context, getString(R.string.enter_folder));
            return;
        }
        saveData();
    }

    private void saveData() {
        try {
            if (this.isEdit) {
                this.db.folderDao().update(this.model);
            } else {
                this.model.setSequence(this.db.folderDao().getFolderListCount());
                this.db.folderDao().insert(this.model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        openList(false);
    }

    private void openList(boolean z) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_IS_DELETED, z);
        intent.putExtra(EXTRA_IS_EDIT, getIntent().getBooleanExtra(EXTRA_IS_EDIT, false));
        intent.putExtra(EXTRA_POSITION, getIntent().getIntExtra(EXTRA_POSITION, 0));
        intent.putExtra(EXTRA_MODEL, this.model);
        setResult(-1, intent);
        finish();
    }

    private void showColorPickerDialog() {
        ColorPickerDialogBuilder.with(this.context).setTitle(getString(R.string.select_folder_color)).initialColor(Color.parseColor(this.model.getColorCode())).wheelType(ColorPickerView.WHEEL_TYPE.FLOWER).density(12).setOnColorSelectedListener(new OnColorSelectedListener() {
            public void onColorSelected(int i) {
            }
        }).setPositiveButton((CharSequence) getString(R.string.set), (ColorPickerClickListener) new ColorPickerClickListener() {
            public void onClick(DialogInterface dialogInterface, int i, Integer[] numArr) {
                FolderRowModel access$100 = model;
                access$100.setColorCode("#" + Integer.toHexString(i).toLowerCase());
            }
        }).setNegativeButton((CharSequence) getString(R.string.cancel), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).build().show();
    }

    private void showIconPicker() {
        showDialogFolderList();
    }

    private void folderDialogSetup() {
        fillFolderList();
        setFolderListDialog();
    }

    private void fillFolderList() {
        this.imageList = new ArrayList<>();
        ImageRowModel imageRowModel = new ImageRowModel();
        imageRowModel.setId(Constants.FOLDER_IMAGE_TYPE_Gratitude);
        this.imageList.add(imageRowModel);
        ImageRowModel imageRowModel2 = new ImageRowModel();
        imageRowModel2.setId("Success");
        this.imageList.add(imageRowModel2);
        ImageRowModel imageRowModel3 = new ImageRowModel();
        imageRowModel3.setId(Constants.FOLDER_IMAGE_TYPE_Confidence);
        this.imageList.add(imageRowModel3);
        ImageRowModel imageRowModel4 = new ImageRowModel();
        imageRowModel4.setId(Constants.FOLDER_IMAGE_TYPE_Self_Esteem);
        this.imageList.add(imageRowModel4);
        ImageRowModel imageRowModel5 = new ImageRowModel();
        imageRowModel5.setId(Constants.FOLDER_IMAGE_TYPE_Decision_Making);
        this.imageList.add(imageRowModel5);
        ImageRowModel imageRowModel6 = new ImageRowModel();
        imageRowModel6.setId(Constants.FOLDER_IMAGE_TYPE_Abundance);
        this.imageList.add(imageRowModel6);
        ImageRowModel imageRowModel7 = new ImageRowModel();
        imageRowModel7.setId(Constants.FOLDER_IMAGE_TYPE_Attitude);
        this.imageList.add(imageRowModel7);
        ImageRowModel imageRowModel8 = new ImageRowModel();
        imageRowModel8.setId(Constants.FOLDER_IMAGE_TYPE_Business);
        this.imageList.add(imageRowModel8);
        ImageRowModel imageRowModel9 = new ImageRowModel();
        imageRowModel9.setId(Constants.FOLDER_IMAGE_TYPE_Love);
        this.imageList.add(imageRowModel9);
        ImageRowModel imageRowModel10 = new ImageRowModel();
        imageRowModel10.setId(Constants.FOLDER_IMAGE_TYPE_Family);
        this.imageList.add(imageRowModel10);
        ImageRowModel imageRowModel11 = new ImageRowModel();
        imageRowModel11.setId(Constants.FOLDER_IMAGE_TYPE_Relationship);
        this.imageList.add(imageRowModel11);
        ImageRowModel imageRowModel12 = new ImageRowModel();
        imageRowModel12.setId(Constants.FOLDER_IMAGE_TYPE_Forgiveness);
        this.imageList.add(imageRowModel12);
        ImageRowModel imageRowModel13 = new ImageRowModel();
        imageRowModel13.setId(Constants.FOLDER_IMAGE_TYPE_Law_of_attraction);
        this.imageList.add(imageRowModel13);
        ImageRowModel imageRowModel14 = new ImageRowModel();
        imageRowModel14.setId("Health");
        this.imageList.add(imageRowModel14);
        ImageRowModel imageRowModel15 = new ImageRowModel();
        imageRowModel15.setId(Constants.FOLDER_IMAGE_TYPE_Exercise);
        this.imageList.add(imageRowModel15);
        ImageRowModel imageRowModel16 = new ImageRowModel();
        imageRowModel16.setId(Constants.FOLDER_IMAGE_TYPE_Pregnancy);
        this.imageList.add(imageRowModel16);
        ImageRowModel imageRowModel17 = new ImageRowModel();
        imageRowModel17.setId(Constants.FOLDER_IMAGE_TYPE_Beauty);
        this.imageList.add(imageRowModel17);
        ImageRowModel imageRowModel18 = new ImageRowModel();
        imageRowModel18.setId(Constants.FOLDER_IMAGE_TYPE_Women);
        this.imageList.add(imageRowModel18);
        ImageRowModel imageRowModel19 = new ImageRowModel();
        imageRowModel19.setId(Constants.FOLDER_IMAGE_TYPE_PUBLIC_SPEAKING);
        this.imageList.add(imageRowModel19);
        ImageRowModel imageRowModel20 = new ImageRowModel();
        imageRowModel20.setId(Constants.FOLDER_IMAGE_TYPE_POSITIVE_THINKING);
        this.imageList.add(imageRowModel20);
        ImageRowModel imageRowModel21 = new ImageRowModel();
        imageRowModel21.setId(Constants.FOLDER_IMAGE_TYPE_CREATIVITY);
        this.imageList.add(imageRowModel21);
        ImageRowModel imageRowModel22 = new ImageRowModel();
        imageRowModel22.setId(Constants.FOLDER_IMAGE_TYPE_WEIGHT_LOSS);
        this.imageList.add(imageRowModel22);
        ImageRowModel imageRowModel23 = new ImageRowModel();
        imageRowModel23.setId(Constants.FOLDER_IMAGE_TYPE_Relationship);
        this.imageList.add(imageRowModel23);
        ImageRowModel imageRowModel24 = new ImageRowModel();
        imageRowModel24.setId(Constants.FOLDER_IMAGE_TYPE_WEALTH);
        this.imageList.add(imageRowModel24);
        this.selectedFolderPos = getSelectedPosById();
        this.imageList.get(this.selectedFolderPos).setSelected(true);
    }

    private int getSelectedPosById() {
        for (int i = 0; i < this.imageList.size(); i++) {
            if (this.imageList.get(i).getId().equalsIgnoreCase(this.model.getImageType())) {
                return i;
            }
        }
        return 0;
    }

    public void setFolderListDialog() {
        this.dialogFolderListBinding = (AlertDialogRecyclerListBinding) DataBindingUtil.inflate(LayoutInflater.from(this.context), R.layout.alert_dialog_recycler_list, (ViewGroup) null, false);
        this.dialogFolderList = new Dialog(this.context);
        this.dialogFolderList.setContentView(this.dialogFolderListBinding.getRoot());
        this.dialogFolderList.setCancelable(false);
        this.dialogFolderList.getWindow().setBackgroundDrawableResource(17170445);
        this.dialogFolderList.getWindow().setLayout(-1, -2);
        this.dialogFolderListBinding.txtTitle.setText(R.string.select_folder_type);
        this.dialogFolderListBinding.btnOk.setText(R.string.set);
        this.dialogFolderListBinding.recycler.setLayoutManager(new GridLayoutManager(this.context, 3));
        this.dialogFolderListBinding.recycler.addItemDecoration(new ItemOffsetDecoration(this.context, R.dimen.cardItemOffset));
        this.dialogFolderListBinding.recycler.setAdapter(new ImageAdapter(true, this.imageList, new RecyclerItemClick() {
            public void onClick(int i, int i2) {
                selectedFolderPos = i;
            }
        }));
        this.dialogFolderListBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    dialogFolderList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.dialogFolderListBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                model.setImageType(((ImageRowModel) imageList.get(selectedFolderPos)).getId());
                try {
                    dialogFolderList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showDialogFolderList() {
        try {
            if (this.dialogFolderList != null && !this.dialogFolderList.isShowing()) {
                this.dialogFolderList.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (!this.model.equals(this.oldModel)) {
            saveChanges();
        } else {
            super.onBackPressed();
        }
    }

    public void saveChanges() {
        AppConstants.showTwoButtonDialog(this.context, getString(R.string.app_name), getString(R.string.exit_msg), true, true, getString(R.string.save), getString(R.string.yes), (TwoButtonDialogListener) new TwoButtonDialogListener() {
            public void onOk() {
                checkValidation();
            }

            public void onCancel() {
                finish();
            }
        });
    }
}
