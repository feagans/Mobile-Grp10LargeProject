package com.test.mylifegoale.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.test.mylifegoale.R;
import com.test.mylifegoale.adapters.FolderAdapter;
import com.test.mylifegoale.adapters.ImageAdapter;
import com.test.mylifegoale.adapters.SelectionAdapter;
import com.test.mylifegoale.base.roomDb.AppDatabase;
import com.test.mylifegoale.baseClass.BaseActivityBinding;
import com.test.mylifegoale.databinding.ActivityAffirmationAddEditBinding;
import com.test.mylifegoale.databinding.AlertDialogRecyclerListBinding;
import com.test.mylifegoale.databinding.AlertDialogSoundRecorderBinding;
import com.test.mylifegoale.itemClick.CopyFileListener;
import com.test.mylifegoale.itemClick.RecyclerItemClick;
import com.test.mylifegoale.itemClick.TwoButtonDialogListener;
import com.test.mylifegoale.model.AffirmationRowModel;
import com.test.mylifegoale.model.FolderRowModel;
import com.test.mylifegoale.model.image.ImageRowModel;
import com.test.mylifegoale.model.selection.SelectionRowModel;
import com.test.mylifegoale.model.toolbar.ToolbarModel;
import com.test.mylifegoale.utilities.AdConstants;
import com.test.mylifegoale.utilities.AppConstants;
import com.test.mylifegoale.utilities.AppPref;
import com.test.mylifegoale.utilities.Constants;
import com.test.mylifegoale.utilities.ItemOffsetDecoration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class AddEditAffirmationActivity extends BaseActivityBinding implements EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {
    public static String EXTRA_IS_DELETED = "isDeleted";
    public static String EXTRA_IS_EDIT = "isEdit";
    public static String EXTRA_MODEL = "model";
    public static String EXTRA_POSITION = "position";
    private ActivityAffirmationAddEditBinding binding;

    public AppDatabase db;

    public Dialog dialogFolderList;
    private AlertDialogRecyclerListBinding dialogFolderListBinding;

    public Dialog dialogImageList;
    private AlertDialogRecyclerListBinding dialogImageListBinding;

    public Dialog dialogImageTypeList;
    private AlertDialogRecyclerListBinding dialogImageTypeListBinding;

    public ArrayList<FolderRowModel> folderList;

    public ArrayList<ImageRowModel> imageList;

    public ArrayList<SelectionRowModel> imageTypeList;
    private boolean isEdit = false;
    boolean isPause = false;
    private MediaPlayer mediaPlayer;
    private MediaRecorder mediaRecorder;

    public AffirmationRowModel model;
    private AffirmationRowModel oldModel;

    public int selectedFolderPos = 0;

    public int selectedImagePos = 0;

    public int selectedImageTypePos = 0;
    public ToolbarModel toolbarModel;

    @Override
    public void onRationaleAccepted(int i) {
        //add
    }

    @Override
    public void onRationaleDenied(int i) {
        //add
    }

    public float volumeForMusic(int i) {
        return ((float) i) / 100.0f;
    }


    public void setBinding() {
        this.binding = (ActivityAffirmationAddEditBinding) DataBindingUtil.setContentView(this, R.layout.activity_affirmation_add_edit);
        this.db = AppDatabase.getAppDatabase(this);
        setModelDetail();
        this.binding.setAffirmationRowModel(this.model);
        AdConstants ads = new AdConstants();
        ads.loadNativeAd(this, binding.nativeadcontainer);

    }

    private void setModelDetail() {
        if (getIntent() != null && getIntent().hasExtra(EXTRA_MODEL)) {
            boolean z = false;
            if (getIntent().hasExtra(EXTRA_IS_EDIT) && getIntent().getBooleanExtra(EXTRA_IS_EDIT, false)) {
                z = true;
            }
            this.isEdit = z;
            if (this.isEdit) {
                this.model = (AffirmationRowModel) getIntent().getParcelableExtra(EXTRA_MODEL);
            } else {
                this.model = (AffirmationRowModel) getIntent().getParcelableExtra(EXTRA_MODEL);
                this.model.setId(AppConstants.getUniqueId());
            }
        }
        this.oldModel = new AffirmationRowModel(this.model);
        binding.etAffirmation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //add
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                model.setQuoteText(binding.etAffirmation.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //add
            }
        });
    }


    public void setToolbar() {
        this.toolbarModel = new ToolbarModel();
        this.toolbarModel.setTitle(this.isEdit ? "Edit" : "Add");
        this.toolbarModel.setDelete(this.isEdit);
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
        deleteItem();
        return true;
    }

    public void deleteItem() {
        pause();
        AppConstants.showTwoButtonDialog(this.context, getString(R.string.app_name), getString(R.string.delete_msg) + "<br /> <b>This Affirmation</b>", true, true, getString(R.string.delete), getString(R.string.cancel), (TwoButtonDialogListener) new TwoButtonDialogListener() {
            public void onCancel() {
            }

            public void onOk() {
                try {
                    db.affirmationDao().delete(model);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                openItemList(true);
            }
        });
    }


    public void setOnClicks() {
        this.binding.includedToolbar.imgBack.setOnClickListener(this);
        this.binding.includedToolbar.imgDelete.setOnClickListener(this);
        this.binding.includedToolbar.imgOther.setOnClickListener(this);
        this.binding.btnAddEdit.setOnClickListener(this);
        this.binding.imgFolder.setOnClickListener(this);
        this.binding.cardBackground.setOnClickListener(this);
        this.binding.imgDeleteBackground.setOnClickListener(this);
        this.binding.imgAddVoice.setOnClickListener(this);
        this.binding.imgVoice.setOnClickListener(this);
        this.binding.imgDeleteVoice.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddEdit:
                addupdate();
                return;
            case R.id.cardBackground:
                showDialogImageTypeList();
                return;
            case R.id.imgAddVoice:
                recordAudio();
                return;
            case R.id.imgBack:
                onBackPressed();
                return;
            case R.id.imgDelete:
                deleteItem();
                return;
            case R.id.imgDeleteBackground:
                new File(this.model.getImageUrl()).delete();
                this.model.setImageUrl("");
                noDataView();
                return;
            case R.id.imgDeleteVoice:
                pause();
                new File(this.model.getVoiceUrl()).delete();
                this.model.setVoiceUrl("");
                noDataView();
                return;
            case R.id.imgFolder:
                showDialogFolderList();
                return;
            case R.id.imgOther:
                addupdate();
                return;
            case R.id.imgVoice:
                playPause();
                return;
            default:
                return;
        }
    }


    public void noDataView() {
        int i = 0;
        this.binding.linBackgroundData.setVisibility(this.model.isImageFound() ? View.VISIBLE : View.GONE);
        this.binding.linBackgroundNoData.setVisibility(!this.model.isImageFound() ? View.VISIBLE : View.GONE);
        this.binding.linVoiceData.setVisibility(this.model.isVoiceFound() ? View.VISIBLE : View.GONE);
        LinearLayout linearLayout = this.binding.linVoiceNoData;
        if (this.model.isVoiceFound()) {
            i = 8;
        }
        linearLayout.setVisibility(i);
    }


    public void initMethods() {
        folderDialogSetup();
        imageTypeDialogSetup();
        imageDialogSetup();
        setSwitchIcon(!AppPref.isEnableBackgroundVoice(this.context));
    }

    private void setSwitchIcon(boolean z) {
        Resources resources;
        int i;
        this.binding.cardVoice.setEnabled(z);
        this.binding.imgVoice.setEnabled(z);
        this.binding.imgDeleteVoice.setEnabled(z);
        this.binding.imgAddVoice.setEnabled(z);
        this.binding.linVoiceNote.setVisibility(!z ? View.VISIBLE : View.GONE);
        CardView cardView = this.binding.cardVoice;
        if (z) {
            resources = this.context.getResources();
            i = R.color.white;
        } else {
            resources = this.context.getResources();
            i = R.color.disableColor;
        }
        cardView.setBackgroundColor(resources.getColor(i));
    }

    private void addupdate() {
        checkValidation();
    }


    public void checkValidation() {
        if (this.model.getQuoteText() == null || this.model.getQuoteText().trim().length() <= 0) {
            this.binding.etAffirmation.requestFocus();
            AppConstants.toastShort(this.context, getString(R.string.enter_affirmation));
            return;
        }
        pause();
        saveData();
    }

    private void saveData() {
        try {
            if (this.isEdit) {
                this.db.affirmationDao().update(this.model);
            } else {
                this.model.setSequenceFolder(this.db.affirmationDao().getFolderListCount(this.model.getFolderId()));
                this.model.setSequence(this.db.affirmationDao().getFolderListActiveCount());
                this.db.affirmationDao().insert(this.model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        openItemList(false);
    }


    public void openItemList(boolean z) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_IS_DELETED, z);
        intent.putExtra(EXTRA_IS_EDIT, getIntent().getBooleanExtra(EXTRA_IS_EDIT, false));
        intent.putExtra(EXTRA_POSITION, getIntent().getIntExtra(EXTRA_POSITION, 0));
        intent.putExtra(EXTRA_MODEL, this.model);
        setResult(-1, intent);
        finish();
    }

    private void folderDialogSetup() {
        fillFolderList();
        setFolderListDialog();
    }

    private void fillFolderList() {
        this.folderList = new ArrayList<>();
        try {
            this.folderList.addAll(this.db.folderDao().getAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.selectedFolderPos = getSelectedPosById();
        this.folderList.get(this.selectedFolderPos).setSelected(true);
    }

    private int getSelectedPosById() {
        for (int i = 0; i < this.folderList.size(); i++) {
            if (this.folderList.get(i).getId().equalsIgnoreCase(this.model.getFolderId())) {
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
        this.dialogFolderListBinding.recycler.setLayoutManager(new LinearLayoutManager(this.context));
        this.dialogFolderListBinding.recycler.setAdapter(new FolderAdapter(4, this.context, this.folderList, new RecyclerItemClick() {
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
                model.setFolderId(((FolderRowModel) folderList.get(selectedFolderPos)).getId());
                model.setFolderRowModel((FolderRowModel) folderList.get(selectedFolderPos));
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

    private void imageTypeDialogSetup() {
        fillImageTypeList();
        setImageTypeListDialog();
    }

    private void fillImageTypeList() {
        this.imageTypeList = new ArrayList<>();
        this.imageTypeList.add(new SelectionRowModel(getString(R.string.select_from_application)));
        this.imageTypeList.add(new SelectionRowModel(getString(R.string.select_from_gallery)));
    }

    public void setImageTypeListDialog() {
        this.dialogImageTypeListBinding = (AlertDialogRecyclerListBinding) DataBindingUtil.inflate(LayoutInflater.from(this.context), R.layout.alert_dialog_recycler_list, (ViewGroup) null, false);
        this.dialogImageTypeList = new Dialog(this.context);
        this.dialogImageTypeList.setContentView(this.dialogImageTypeListBinding.getRoot());
        this.dialogImageTypeList.setCancelable(true);
        this.dialogImageTypeList.getWindow().setBackgroundDrawableResource(17170445);
        this.dialogImageTypeList.getWindow().setLayout(-1, -2);
        this.dialogImageTypeListBinding.txtTitle.setText(R.string.select_ImageType_type);
        this.dialogImageTypeListBinding.btnOk.setText(R.string.set);
        this.dialogImageTypeListBinding.linButton.setVisibility(View.GONE);
        this.dialogImageTypeListBinding.recycler.setLayoutManager(new LinearLayoutManager(this.context));
        this.dialogImageTypeListBinding.recycler.setAdapter(new SelectionAdapter(false, this.imageTypeList, new RecyclerItemClick() {
            public void onClick(int i, int i2) {
                selectedImageTypePos = i;
                if (((SelectionRowModel) imageTypeList.get(selectedImageTypePos)).getLabel().equalsIgnoreCase(getString(R.string.select_from_application))) {
                    showDialogImageList();
                } else {
                    selectImageFromGallery();
                }
                try {
                    dialogImageTypeList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
        this.dialogImageTypeListBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    dialogImageTypeList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.dialogImageTypeListBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            }
        });
    }

    private void showDialogImageTypeList() {
        try {
            if (this.dialogImageTypeList != null && !this.dialogImageTypeList.isShowing()) {
                this.dialogImageTypeList.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void selectImageFromGallery() {
        if (isHasPermissions(this.context, "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE")) {
            openGallery();
        } else {
            requestPermissions(this.context, getString(R.string.rationale_image), Constants.REQUEST_PERM_FILE, "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE");
        }
    }

    private void openGallery() {
        EasyImage.openGallery((Activity) this, Constants.REQUEST_PICK_IMAGE);
    }

    private void imageDialogSetup() {
        this.imageList = new ArrayList<>();
        setImageListDialog();
    }

    private void fillImageList() {
        this.imageList.clear();
        this.imageList.addAll(AppConstants.getListBackgroundImage());
        this.selectedImagePos = getSelectedImagePosById();
        this.imageList.get(this.selectedImagePos).setSelected(true);
    }

    private int getSelectedImagePosById() {
        for (int i = 0; i < this.imageList.size(); i++) {
            if (this.imageList.get(i).getImageUrl().equalsIgnoreCase(this.model.getImageUrl())) {
                return i;
            }
        }
        return 0;
    }

    public void setImageListDialog() {
        this.dialogImageListBinding = (AlertDialogRecyclerListBinding) DataBindingUtil.inflate(LayoutInflater.from(this.context), R.layout.alert_dialog_recycler_list, (ViewGroup) null, false);
        this.dialogImageList = new Dialog(this.context);
        this.dialogImageList.setContentView(this.dialogImageListBinding.getRoot());
        this.dialogImageList.setCancelable(false);
        this.dialogImageList.getWindow().setBackgroundDrawableResource(17170445);
        this.dialogImageList.getWindow().setLayout(-1, -2);
        this.dialogImageListBinding.txtTitle.setText(R.string.select_image_type);
        this.dialogImageListBinding.btnOk.setText(R.string.set);
        this.dialogImageListBinding.recycler.setLayoutManager(new GridLayoutManager(this.context, 2));
        this.dialogImageListBinding.recycler.addItemDecoration(new ItemOffsetDecoration(this.context, R.dimen.cardItemOffset));
        this.dialogImageListBinding.recycler.setAdapter(new ImageAdapter(false, this.imageList, new RecyclerItemClick() {
            public void onClick(int i, int i2) {
                selectedImagePos = i;
            }
        }));
        this.dialogImageListBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    dialogImageList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.dialogImageListBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                model.setImageUrl(((ImageRowModel) imageList.get(selectedImagePos)).getImageUrl());
                try {
                    dialogImageList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void showDialogImageList() {
        fillImageList();
        try {
            if (this.dialogImageList != null && !this.dialogImageList.isShowing()) {
                this.dialogImageList.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void recordAudio() {
        if (isHasPermissions(this.context, "android.permission.RECORD_AUDIO")) {
            showSoundRecordDialog(AppConstants.getPrivatePathSound(this.context, this.model.getId()));
        } else {
            requestPermissions(this.context, getString(R.string.rationale_audio), Constants.REQUEST_PERM_AUDIO, "android.permission.RECORD_AUDIO");
        }
    }

    public void showSoundRecordDialog(final String str) {
        final AlertDialogSoundRecorderBinding alertDialogSoundRecorderBinding = (AlertDialogSoundRecorderBinding) DataBindingUtil.inflate(LayoutInflater.from(this.context), R.layout.alert_dialog_sound_recorder, (ViewGroup) null, false);
        final Dialog dialog = new Dialog(this.context);
        dialog.setContentView(alertDialogSoundRecorderBinding.getRoot());
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(17170445);
        dialog.getWindow().setLayout(-1, -2);
        alertDialogSoundRecorderBinding.affirmationLayout.setVisibility(View.VISIBLE);
        alertDialogSoundRecorderBinding.affirmationVisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                alertDialogSoundRecorderBinding.affirmationText.setVisibility(z ? View.VISIBLE : View.GONE);
            }
        });
        alertDialogSoundRecorderBinding.affirmationText.setText(this.binding.etAffirmation.getText().toString());
        alertDialogSoundRecorderBinding.txtTitle.setText(R.string.record_your_voice);
        mediaRecorderSetup(str);
        mediaRecorderStart(alertDialogSoundRecorderBinding);
        alertDialogSoundRecorderBinding.imgReset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mediaRecorderReset(alertDialogSoundRecorderBinding, str);
            }
        });
        alertDialogSoundRecorderBinding.imgPlayPause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!isPause) {
                    mediaRecorderStop(alertDialogSoundRecorderBinding);
                } else {
                    mediaRecorderStart(alertDialogSoundRecorderBinding);
                }
            }
        });
        alertDialogSoundRecorderBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mediaRecorderStop(alertDialogSoundRecorderBinding);
                new File(str).delete();
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        alertDialogSoundRecorderBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mediaRecorderStop(alertDialogSoundRecorderBinding);
                model.setVoiceUrl(str);
                noDataView();
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            if (!dialog.isShowing()) {
                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mediaRecorderSetup(String str) {
        this.mediaRecorder = new MediaRecorder();
        this.mediaRecorder.setAudioSource(1);
        this.mediaRecorder.setOutputFormat(1);
        this.mediaRecorder.setAudioEncoder(3);
        this.mediaRecorder.setOutputFile(str);
    }


    public void mediaRecorderStart(AlertDialogSoundRecorderBinding alertDialogSoundRecorderBinding) {
        try {
            this.mediaRecorder.prepare();
            this.mediaRecorder.start();
            this.isPause = false;
            alertDialogSoundRecorderBinding.chronometer.start();
            alertDialogSoundRecorderBinding.imgPlayPause.setImageResource(R.drawable.pause);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }


    public void mediaRecorderReset(AlertDialogSoundRecorderBinding alertDialogSoundRecorderBinding, String str) {
        try {
            mediaRecorderStop(alertDialogSoundRecorderBinding);
            new File(str).delete();
            mediaRecorderSetup(str);
            mediaRecorderStart(alertDialogSoundRecorderBinding);
            alertDialogSoundRecorderBinding.chronometer.setBase(SystemClock.elapsedRealtime());
            alertDialogSoundRecorderBinding.chronometer.stop();
            alertDialogSoundRecorderBinding.chronometer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void mediaRecorderStop(AlertDialogSoundRecorderBinding alertDialogSoundRecorderBinding) {
        try {
            this.isPause = true;
            alertDialogSoundRecorderBinding.chronometer.stop();
            this.mediaRecorder.stop();
            alertDialogSoundRecorderBinding.imgPlayPause.setImageResource(R.drawable.play);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playPause() {
        if (!this.model.isPause()) {
            playVoice();
        } else {
            pause();
        }
    }

    private void playVoice() {
        if (this.model.isVoiceFound()) {
            play(this.model.getVoiceUrl(), 100, false);
        }
    }

    private void play(String str, int i, boolean z) {
        try {
            this.mediaPlayer = new MediaPlayer();
            if (Build.VERSION.SDK_INT >= 21) {
                this.mediaPlayer = MediaPlayer.create(this.context, FileProvider.getUriForFile(this.context.getApplicationContext(), "com.test.mylifegoale.provider", new File(str)));
            } else {
                this.mediaPlayer = MediaPlayer.create(this.context, Uri.parse(str));
            }
            this.mediaPlayer.setVolume(volumeForMusic(i), volumeForMusic(i));
            this.mediaPlayer.setLooping(z);
            this.mediaPlayer.start();
            this.model.setPause(true);
            this.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mediaPlayer) {
                    model.setPause(false);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(40);
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        }
    }

    private void pause() {
        MediaPlayer mediaPlayer2 = this.mediaPlayer;
        if (mediaPlayer2 != null) {
            try {
                mediaPlayer2.stop();
                try {
                    this.mediaPlayer.release();
                    this.mediaPlayer = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                this.mediaPlayer.release();
                this.mediaPlayer = null;
            } catch (Throwable th) {
                try {
                    this.mediaPlayer.release();
                    this.mediaPlayer = null;
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
                throw th;
            }
            this.model.setPause(false);
        }
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
        switch (i) {
            case Constants.REQUEST_PERM_FILE:
                openGallery();
                return;
            case Constants.REQUEST_PERM_AUDIO:
                showSoundRecordDialog(AppConstants.getPrivatePathSound(this.context, this.model.getId()));
                return;
            default:
                return;
        }
    }

    public void onPermissionsDenied(int i, @NonNull List<String> list) {
        if (EasyPermissions.somePermissionPermanentlyDenied((Activity) this, list)) {
            new AppSettingsDialog.Builder((Activity) this).build().show();
        }
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        EasyImage.handleActivityResult(i, i2, intent, this, new DefaultCallback() {
            public void onImagePicked(File file, EasyImage.ImageSource imageSource, int i) {
                try {
                    copyFile(new File(file.getAbsolutePath()), new File(AppConstants.getPrivatePathImage(context, AppConstants.getFormattedDate(Calendar.getInstance().getTimeInMillis(), Constants.SIMPLE_DATE_FORMAT_DATE_TIME))), new CopyFileListener() {
                        public void onCopied(String str) {
                            if (model.getImageUrl() != null && !model.getImageUrl().isEmpty()) {
                                File file = new File(model.getImageUrl());
                                if (file.exists()) {
                                    file.delete();
                                }
                            }
                            model.setImageUrl(str);
                            noDataView();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void copyFile(File file, File file2, CopyFileListener copyFileListener) {
        FileChannel fileChannel;
        if (file.exists()) {
            FileChannel fileChannel2 = null;
            try {
                fileChannel = new FileInputStream(file).getChannel();
            } catch (FileNotFoundException e) {

                e.printStackTrace();
                fileChannel = null;
            }
            try {
                fileChannel2 = new FileOutputStream(file2).getChannel();
            } catch (FileNotFoundException e2) {
                e2.printStackTrace();
            }
            if (!(fileChannel2 == null || fileChannel == null)) {
                try {
                    fileChannel2.transferFrom(fileChannel, 0, fileChannel.size());
                    copyFileListener.onCopied(file2.getAbsolutePath());
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
            if (fileChannel != null) {
                try {
                    fileChannel.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
            if (fileChannel2 != null) {
                try {
                    fileChannel2.close();
                } catch (IOException e5) {
                    e5.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        pause();
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
