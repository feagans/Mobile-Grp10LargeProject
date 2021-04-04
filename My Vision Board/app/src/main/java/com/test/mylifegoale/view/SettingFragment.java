package com.test.mylifegoale.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.mylifegoale.R;
import com.test.mylifegoale.adapters.FileAdapter;
import com.test.mylifegoale.adapters.SelectionAdapter;
import com.test.mylifegoale.backupRestore.BackupRestore;
import com.test.mylifegoale.backupRestore.BackupRestoreProgress;
import com.test.mylifegoale.backupRestore.OnBackupRestore;
import com.test.mylifegoale.backupRestore.RestoreDriveListActivity;
import com.test.mylifegoale.backupRestore.RestoreListActivity;
import com.test.mylifegoale.backupRestore.RestoreRowModel;
import com.test.mylifegoale.base.BaseActivity;
import com.test.mylifegoale.dailyAlarm.AlarmUtil;
import com.test.mylifegoale.databinding.AlertDialogBackupBinding;
import com.test.mylifegoale.databinding.AlertDialogRecyclerListBinding;
import com.test.mylifegoale.databinding.AlertDialogSoundRecorderBinding;
import com.test.mylifegoale.databinding.FragmentSettingBinding;
import com.test.mylifegoale.itemClick.EditTextDialogListener;
import com.test.mylifegoale.itemClick.RecycleItemClick;
import com.test.mylifegoale.itemClick.RecyclerItemClick;
import com.test.mylifegoale.model.DirectoryModel;
import com.test.mylifegoale.model.selection.SelectionRowModel;
import com.test.mylifegoale.utilities.AppConstants;
import com.test.mylifegoale.utilities.AppPref;
import com.test.mylifegoale.utilities.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class SettingFragment extends BaseActivity implements EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {
    public static String FILEPATH = Environment.getExternalStorageDirectory().getPath();
    ImageView back;
    LinearLayout backlayout;
    private BackupRestore backupRestore;

    public ArrayList<SelectionRowModel> bgMusicTypeList;
    private FragmentSettingBinding binding;

    public Calendar calendar;
    Context context;
    Dialog dialog;

    public Dialog dialogBackup;

    public AlertDialogBackupBinding dialogBackupBinding;

    public Dialog dialogBgMusicTypeList;
    private AlertDialogRecyclerListBinding dialogBgMusicTypeListBinding;

    public Dialog dialogMusicList;
    private AlertDialogRecyclerListBinding dialogMusicListBinding;
    DirectoryModel directoryModel;
    ArrayList<DirectoryModel> directoryModelArrayList;
    FileAdapter fileAdapter;
    boolean isPause = false;
    boolean isPauseVoice = false;
    RecyclerView lview1;
    private MediaPlayer mediaPlayer;
    private MediaRecorder mediaRecorder;

    public ArrayList<SelectionRowModel> musicList;
    private BackupRestoreProgress progressDialog;

    public int selectedBgMusicPos = 0;

    public int selectedMusicPos = 0;

    public void onRationaleAccepted(int i) {
    }

    public void onRationaleDenied(int i) {
    }

    public float volumeForMusic(int i) {
        return ((float) i) / 100.0f;
    }

    public void setBinding() {
        this.binding = (FragmentSettingBinding) DataBindingUtil.setContentView(this, R.layout.fragment_setting);
    }

    public void init() {
        this.context = this;
        this.backupRestore = new BackupRestore(this);
        this.progressDialog = new BackupRestoreProgress(this);
        setOnClicks();
        bgMusicTypeDialogSetup();
        musicListDialogSetup();
        setDetails();
        noVoiceDataView();
        setBackupDialog();
    }

    public void setToolbar() {
        setToolbarTitle(getString(R.string.action_settings));
        setToolbarBack(true);
    }


    public void setOnClicks() {
        this.binding.linReminder.setOnClickListener(this);
        this.binding.linReminderTime.setOnClickListener(this);
        this.binding.linEnableBackgroundMusic.setOnClickListener(this);
        this.binding.linBackgroundMusic.setOnClickListener(this);
        this.binding.linEnableAutoPlaying.setOnClickListener(this);
        this.binding.linAutoPlayInterval.setOnClickListener(this);
        this.binding.cardLoopPlaying.setOnClickListener(this);
        this.binding.cardPlayVoiceFile.setOnClickListener(this);
        this.binding.cardPlayAllInFolder.setOnClickListener(this);
        this.binding.cardPlayInRandomOrder.setOnClickListener(this);
        this.binding.cardSleepTimer.setOnClickListener(this);
        this.binding.linScreenControls.setOnClickListener(this);
        this.binding.linShowDownloadButton.setOnClickListener(this);
        this.binding.linEnableBackgroundMusic.setOnClickListener(this);
        this.binding.linEnableBackgroundVoice.setOnClickListener(this);
        this.binding.imgAddVoice.setOnClickListener(this);
        this.binding.imgVoice.setOnClickListener(this);
        this.binding.imgDeleteVoice.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cardLoopPlaying:
                Context context2 = this.context;
                AppPref.setLoopPlaying(context2, !AppPref.isLoopPlaying(context2));
                setSwitchIcon(this.binding.imgLoopPlaying, AppPref.isLoopPlaying(this.context));
                return;
            case R.id.cardPlayAllInFolder:
                Context context3 = this.context;
                AppPref.setPlayAllInFolder(context3, !AppPref.isPlayAllInFolder(context3));
                setSwitchIcon(this.binding.imgPlayAllInFolder, AppPref.isPlayAllInFolder(this.context));
                return;
            case R.id.cardPlayInRandomOrder:
                Context context4 = this.context;
                AppPref.setPlayInRandomOrder(context4, !AppPref.isPlayInRandomOrder(context4));
                setSwitchIcon(this.binding.imgPlayInRandomOrder, AppPref.isPlayInRandomOrder(this.context));
                return;
            case R.id.cardPlayVoiceFile:
                Context context5 = this.context;
                AppPref.setPlayVoiceFile(context5, !AppPref.isPlayVoiceFile(context5));
                setSwitchIcon(this.binding.imgPlayVoiceFile, AppPref.isPlayVoiceFile(this.context));
                return;
            case R.id.cardSleepTimer:
                showSetSleepTimerDialog();
                return;
            case R.id.driveRestore:
                startActivityForResult(new Intent(this.context, RestoreDriveListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), 1002);
                return;
            case R.id.imgAddVoice:
                recordAudio();
                return;
            case R.id.imgDeleteVoice:
                pause();
                new File(AppPref.getBackgroundVoiceUrl(this.context)).delete();
                AppPref.setBackgroundVoiceUrl(this.context, "");
                noVoiceDataView();
                return;
            case R.id.imgVoice:
                playPause();
                return;
            case R.id.linAutoPlayInterval:
                showSetIntervalDialog();
                return;
            case R.id.linBackgroundMusic:
                showDialogBgMusicTypeList();
                return;
            case R.id.linEnableAutoPlaying:
                Context context6 = this.context;
                AppPref.setEnableAutoPlaying(context6, !AppPref.isEnableAutoPlaying(context6));
                setSwitchIcon(this.binding.imgEnableAutoPlaying, AppPref.isEnableAutoPlaying(this.context));
                return;
            case R.id.linEnableBackgroundMusic:
                Context context7 = this.context;
                AppPref.setEnableBackgroundMusic(context7, !AppPref.isEnableBackgroundMusic(context7));
                setSwitchIcon(this.binding.imgEnableBackgroundMusic, AppPref.isEnableBackgroundMusic(this.context));
                return;
            case R.id.linEnableBackgroundVoice:
                Context context8 = this.context;
                AppPref.setEnableBackgroundVoice(context8, !AppPref.isEnableBackgroundVoice(context8));
                setSwitchIcon(this.binding.imgEnableBackgroundVoice, AppPref.isEnableBackgroundVoice(this.context));
                return;
            case R.id.linReminder:
                Context context9 = this.context;
                AppPref.setDailyReminder(context9, !AppPref.isDailyReminder(context9));
                setSwitchIcon(this.binding.imgReminder, AppPref.isDailyReminder(this.context));
                if (AppPref.isDailyReminder(this.context)) {
                    AlarmUtil.remind24(this.context);
                    AlarmUtil.remind3hour(this.context);
                    AlarmUtil.setAlarm(this.context);
                    return;
                }
                return;
            case R.id.linReminderTime:
                showTimePickerDialog();
                return;
            case R.id.linScreenControls:
                Context context10 = this.context;
                AppPref.setScreenControls(context10, !AppPref.isScreenControls(context10));
                setSwitchIcon(this.binding.imgScreenControls, AppPref.isScreenControls(this.context));
                return;
            case R.id.linShowDownloadButton:
                Context context11 = this.context;
                AppPref.setShowDownloadButton(context11, !AppPref.isShowDownloadButton(context11));
                setSwitchIcon(this.binding.imgShowDownloadButton, AppPref.isShowDownloadButton(this.context));
                return;
            case R.id.localRestore:
                startActivityForResult(new Intent(this.context, RestoreListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), 1002);
                return;
            case R.id.takeBackup:
                checkPermAndBackup();
                return;
            default:
                return;
        }
    }

    private void handleSignIn(Intent intent) {
        this.backupRestore.handleSignInResult(intent, true, false, (String) null, this.progressDialog, new OnBackupRestore() {
            public void getList(ArrayList<RestoreRowModel> arrayList) {
            }

            public void onSuccess(boolean z) {
            }
        });
    }

    public void stopmusic() {
        MediaPlayer mediaPlayer2 = this.mediaPlayer;
        if (mediaPlayer2 != null) {
            try {
                mediaPlayer2.stop();
                try {
                    this.mediaPlayer.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                this.mediaPlayer.release();
            } catch (Throwable th) {
                try {
                    this.mediaPlayer.release();
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
                throw th;
            }
        }
    }

    private void showSetIntervalDialog() {
        Context context2 = this.context;
        String string = getString(R.string.enter_interval_time);
        String string2 = getString(R.string.interval_time);
        AppConstants.showEditTextDialog(context2, true, true, false, 10, string, string2, AppPref.getAutoPlayInterval(this.context) + "", getString(R.string.set), new EditTextDialogListener() {
            public void onOk(String str) {
                Long l;
                try {
                    l = Long.valueOf(str);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    l = Long.valueOf(AppPref.getAutoPlayInterval(context));
                }
                AppPref.setAutoPlayInterval(context, l.longValue());
            }
        });
    }

    private void showSetSleepTimerDialog() {
        Context context2 = this.context;
        String string = getString(R.string.enter_sleep_timer);
        String string2 = getString(R.string.sleep_timer);
        AppConstants.showEditTextDialog(context2, true, true, false, 10, string, string2, AppPref.getSleepTimer(this.context) + "", getString(R.string.set), new EditTextDialogListener() {
            public void onOk(String str) {
                Long l;
                try {
                    l = Long.valueOf(str);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    l = Long.valueOf(AppPref.getSleepTimer(context));
                }
                AppPref.setSleepTimer(context, l.longValue());
            }
        });
    }

    private void bgMusicTypeDialogSetup() {
        fillBgMusicTypeList();
        setBgMusicTypeListDialog();
    }

    private void fillBgMusicTypeList() {
        this.bgMusicTypeList = new ArrayList<>();
        this.bgMusicTypeList.add(new SelectionRowModel(getString(R.string.select_from_application)));
        this.bgMusicTypeList.add(new SelectionRowModel(getString(R.string.select_from_phone)));
    }

    public void setBgMusicTypeListDialog() {
        this.dialogBgMusicTypeListBinding = (AlertDialogRecyclerListBinding) DataBindingUtil.inflate(LayoutInflater.from(this.context), R.layout.alert_dialog_recycler_list, (ViewGroup) null, false);
        this.dialogBgMusicTypeList = new Dialog(this.context);
        this.dialogBgMusicTypeList.setContentView(this.dialogBgMusicTypeListBinding.getRoot());
        this.dialogBgMusicTypeList.setCancelable(true);
        this.dialogBgMusicTypeList.getWindow().setBackgroundDrawableResource(17170445);
        this.dialogBgMusicTypeList.getWindow().setLayout(-1, -2);
        this.dialogBgMusicTypeListBinding.txtTitle.setText(R.string.select_background_music);
        this.dialogBgMusicTypeListBinding.btnOk.setText(R.string.set);
        this.dialogBgMusicTypeListBinding.linButton.setVisibility(View.GONE);
        this.dialogBgMusicTypeListBinding.recycler.setLayoutManager(new LinearLayoutManager(this.context));
        this.dialogBgMusicTypeListBinding.recycler.setAdapter(new SelectionAdapter(false, this.bgMusicTypeList, new RecyclerItemClick() {
            public void onClick(int i, int i2) {
                selectedBgMusicPos = i;
                if (((SelectionRowModel) bgMusicTypeList.get(selectedBgMusicPos)).getLabel().equalsIgnoreCase(getString(R.string.select_from_application))) {
                    showDialogMusicList();
                } else if (((SelectionRowModel) bgMusicTypeList.get(selectedBgMusicPos)).getLabel().equalsIgnoreCase(getString(R.string.select_from_phone))) {
                    selectMusicFromPhone();
                }
                try {
                    dialogBgMusicTypeList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
        this.dialogBgMusicTypeListBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    dialogBgMusicTypeList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.dialogBgMusicTypeListBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            }
        });
    }

    private void showDialogBgMusicTypeList() {
        try {
            if (this.dialogBgMusicTypeList != null && !this.dialogBgMusicTypeList.isShowing()) {
                this.dialogBgMusicTypeList.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void musicListDialogSetup() {
        setMusicListDialog();
    }

    private void fillMusicList() {
        this.musicList.clear();
        this.musicList.add(new SelectionRowModel("Deep heal", "song1"));
        this.musicList.add(new SelectionRowModel("Meditation music", "song2"));
        this.musicList.add(new SelectionRowModel("Nature sound", "song3"));
        this.musicList.add(new SelectionRowModel("Relax music (Default)", "song4"));
        this.musicList.add(new SelectionRowModel("Soothing music", "song5"));
        this.selectedMusicPos = getPositionByValue();
    }

    private int getPositionByValue() {
        for (int i = 0; i < this.musicList.size(); i++) {
            if (this.musicList.get(i).getValue().equalsIgnoreCase(AppPref.getBackgroundMusicUrl(this.context))) {
                return i;
            }
        }
        return 3;
    }

    public void setMusicListDialog() {
        this.musicList = new ArrayList<>();
        this.dialogMusicListBinding = (AlertDialogRecyclerListBinding) DataBindingUtil.inflate(LayoutInflater.from(this.context), R.layout.alert_dialog_recycler_list, (ViewGroup) null, false);
        this.dialogMusicList = new Dialog(this.context);
        this.dialogMusicList.setContentView(this.dialogMusicListBinding.getRoot());
        this.dialogMusicList.setCancelable(false);
        this.dialogMusicList.getWindow().setBackgroundDrawableResource(17170445);
        this.dialogMusicList.getWindow().setLayout(-1, -2);
        this.dialogMusicListBinding.txtTitle.setText(R.string.background_music);
        this.dialogMusicListBinding.btnOk.setText(R.string.set);
        this.dialogMusicListBinding.recycler.setLayoutManager(new LinearLayoutManager(this.context));
        this.dialogMusicListBinding.recycler.setAdapter(new SelectionAdapter(true, this.musicList, new RecyclerItemClick() {
            public void onClick(int i, int i2) {
                selectedMusicPos = i;
                SettingFragment settingFragment = SettingFragment.this;
                settingFragment.playVoice(((SelectionRowModel) settingFragment.musicList.get(selectedMusicPos)).getValue());
            }
        }));
        this.dialogMusicListBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pause();
                try {
                    dialogMusicList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.dialogMusicListBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pause();
                AppPref.setBackgroundMusicUrl(context, ((SelectionRowModel) musicList.get(selectedMusicPos)).getValue());
                try {
                    dialogMusicList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        stopmusic();
        super.onBackPressed();
    }


    public void playVoice(String str) {
        pause();
        play(str, 100, false);
    }

    private void play(String str, int i, boolean z) {
        try {
            this.mediaPlayer = new MediaPlayer();
            if (!new File(str).exists() || str.contains(Constants.PATH_RESOURCE)) {
                this.mediaPlayer = MediaPlayer.create(this.context, AppConstants.getDefaultSong(str));
            } else if (Build.VERSION.SDK_INT >= 21) {
                this.mediaPlayer = MediaPlayer.create(this.context, FileProvider.getUriForFile(this.context, "com.test.mylifegoale.provider", new File(str)));
            } else {
                this.mediaPlayer = MediaPlayer.create(this.context, Uri.parse(str));
            }
            this.mediaPlayer.setVolume(volumeForMusic(i), volumeForMusic(i));
            this.mediaPlayer.setLooping(z);
            this.mediaPlayer.start();
            this.isPauseVoice = true;
            setImgVoice(this.isPauseVoice);
            this.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mediaPlayer) {
                    SettingFragment settingFragment = SettingFragment.this;
                    settingFragment.isPauseVoice = false;
                    settingFragment.setImgVoice(settingFragment.isPauseVoice);
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


    public void setImgVoice(boolean z) {
        this.binding.imgVoice.setImageResource(!z ? R.drawable.play : R.drawable.pause);
    }


    public void pause() {
        try {
            if (this.mediaPlayer != null) {
                this.mediaPlayer.stop();
                this.mediaPlayer.release();
                this.mediaPlayer = null;
                this.isPauseVoice = false;
                setImgVoice(this.isPauseVoice);
            }
            this.isPauseVoice = false;
            setImgVoice(this.isPauseVoice);
            return;
        } catch (Exception e4) {
            e4.printStackTrace();
        }
    }


    public void showDialogMusicList() {
        setSelection();
        try {
            if (this.dialogMusicList != null && !this.dialogMusicList.isShowing()) {
                this.dialogMusicList.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSelection() {
        fillMusicList();
        this.musicList.get(this.selectedMusicPos).setSelected(true);
        this.dialogMusicListBinding.recycler.getAdapter().notifyDataSetChanged();
    }

    private void recordAudio() {
        if (isHasPermissions(this.context, "android.permission.RECORD_AUDIO")) {
            showSoundRecordDialog(AppConstants.getPrivatePathBGVoice(this.context, AppConstants.getFormattedDate(Calendar.getInstance().getTimeInMillis(), Constants.SIMPLE_DATE_FORMAT_DATE_TIME)));
        } else {
            requestPermissions(getString(R.string.rationale_audio), Constants.REQUEST_PERM_AUDIO, "android.permission.RECORD_AUDIO");
        }
    }

    public void showSoundRecordDialog(final String str) {
        final AlertDialogSoundRecorderBinding alertDialogSoundRecorderBinding = (AlertDialogSoundRecorderBinding) DataBindingUtil.inflate(LayoutInflater.from(this.context), R.layout.alert_dialog_sound_recorder, (ViewGroup) null, false);
        final Dialog dialog2 = new Dialog(this.context);
        dialog2.setContentView(alertDialogSoundRecorderBinding.getRoot());
        dialog2.setCancelable(false);
        dialog2.getWindow().setBackgroundDrawableResource(17170445);
        dialog2.getWindow().setLayout(-1, -2);
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
                    dialog2.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        alertDialogSoundRecorderBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mediaRecorderStop(alertDialogSoundRecorderBinding);
                AppPref.setBackgroundVoiceUrl(context, str);
                noVoiceDataView();
                try {
                    dialog2.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            if (!dialog2.isShowing()) {
                dialog2.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void noVoiceDataView() {
        boolean isVoiceFound = isVoiceFound();
        int i = 0;
        this.binding.linVoiceData.setVisibility(isVoiceFound ? View.VISIBLE : View.GONE);
        LinearLayout linearLayout = this.binding.linVoiceNoData;
        if (isVoiceFound) {
            i = 8;
        }
        linearLayout.setVisibility(i);
        if (isVoiceFound) {
            this.binding.imgVoice.setImageResource(R.drawable.play);
        }
    }

    public boolean isVoiceFound() {
        String backgroundVoiceUrl = AppPref.getBackgroundVoiceUrl(this.context);
        return backgroundVoiceUrl != null && backgroundVoiceUrl.trim().length() > 0 && new File(backgroundVoiceUrl).exists();
    }

    public void mediaRecorderSetup(String str) {
        this.mediaRecorder = new MediaRecorder();
        this.mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
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
        if (!this.isPauseVoice) {
            playVoice(AppPref.getBackgroundVoiceUrl(this.context));
        } else {
            pause();
        }
    }

    private void setDetails() {
        this.calendar = Calendar.getInstance();
        this.calendar.setTimeInMillis(AppPref.getDailyReminderTime(this.context));
        this.calendar.set(13, 0);
        this.calendar.set(14, 0);
        setSwitchIcon(this.binding.imgReminder, AppPref.isDailyReminder(this.context));
        setSwitchIcon(this.binding.imgEnableBackgroundMusic, AppPref.isEnableBackgroundMusic(this.context));
        setSwitchIcon(this.binding.imgEnableAutoPlaying, AppPref.isEnableAutoPlaying(this.context));
        setSwitchIcon(this.binding.imgLoopPlaying, AppPref.isLoopPlaying(this.context));
        setSwitchIcon(this.binding.imgPlayVoiceFile, AppPref.isPlayVoiceFile(this.context));
        setSwitchIcon(this.binding.imgPlayAllInFolder, AppPref.isPlayAllInFolder(this.context));
        setSwitchIcon(this.binding.imgPlayInRandomOrder, AppPref.isPlayInRandomOrder(this.context));
        setSwitchIcon(this.binding.imgScreenControls, AppPref.isScreenControls(this.context));
        setSwitchIcon(this.binding.imgShowDownloadButton, AppPref.isShowDownloadButton(this.context));
        setSwitchIcon(this.binding.imgEnableBackgroundVoice, AppPref.isEnableBackgroundVoice(this.context));
    }

    private void setSwitchIcon(ImageView imageView, boolean z) {
        imageView.setImageResource(z ? R.drawable.switch_on : R.drawable.switch_off);
        if (imageView == this.binding.imgReminder) {
            this.binding.linReminderTime.setEnabled(z);
            this.binding.linReminderTime.setBackgroundColor(z ? this.context.getResources().getColor(R.color.white) : this.context.getResources().getColor(R.color.disableColor));
        } else if (imageView == this.binding.imgEnableBackgroundMusic) {
            this.binding.linBackgroundMusic.setEnabled(z);
            this.binding.linBackgroundMusic.setBackgroundColor(z ? this.context.getResources().getColor(R.color.white) : this.context.getResources().getColor(R.color.disableColor));
        } else if (imageView == this.binding.imgScreenControls) {
            this.binding.linShowDownloadButton.setEnabled(z);
            this.binding.linShowDownloadButton.setBackgroundColor(z ? this.context.getResources().getColor(R.color.white) : this.context.getResources().getColor(R.color.disableColor));
        } else if (imageView == this.binding.imgEnableAutoPlaying) {
            this.binding.linAutoPlayInterval.setEnabled(z);
            this.binding.linAutoPlayInterval.setBackgroundColor(z ? this.context.getResources().getColor(R.color.white) : this.context.getResources().getColor(R.color.disableColor));
        } else if (imageView == this.binding.imgEnableBackgroundVoice) {
            this.binding.linBackgroundVoice.setEnabled(z);
            this.binding.imgVoice.setEnabled(z);
            this.binding.imgDeleteVoice.setEnabled(z);
            this.binding.imgAddVoice.setEnabled(z);
            this.binding.linBackgroundVoice.setBackgroundColor(z ? this.context.getResources().getColor(R.color.white) : this.context.getResources().getColor(R.color.disableColor));
        }
    }

    private void showTimePickerDialog() {
        new TimePickerDialog(this.context, R.style.AppThemeDialogActionBar, new TimePickerDialog.OnTimeSetListener() {
            @SuppressLint({"NewApi"})
            public void onTimeSet(TimePicker timePicker, int i, int i2) {
                calendar.set(11, i);
                calendar.set(12, i2);
                AppPref.setDailyReminderTime(context, calendar.getTimeInMillis());
                if (AppPref.isDailyReminder(context)) {
                    AlarmUtil.remind24(context);
                    AlarmUtil.remind3hour(context);
                    AlarmUtil.setAlarm(context);
                }
            }
        }, this.calendar.get(11), this.calendar.get(12), false).show();
    }

    public void selectMusicFromPhone() {
        if (isHasPermissions(this.context, "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE")) {
            openFiles();
        } else {
            requestPermissions(getString(R.string.rationale_audio), Constants.REQUEST_PERM_FILE, "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE");
        }
    }

    private void openFiles() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
            View inflate = getLayoutInflater().inflate(R.layout.dialog_open_file, (ViewGroup) null);
            builder.setView(inflate);
            this.backlayout = (LinearLayout) inflate.findViewById(R.id.backlayout);
            this.lview1 = (RecyclerView) inflate.findViewById(R.id.lview1);
            this.back = (ImageView) inflate.findViewById(R.id.back);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.context);
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            this.lview1.setLayoutManager(linearLayoutManager);
            this.fileAdapter = new FileAdapter(this.context, this, new RecycleItemClick() {
                public void onClick(int i) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    SettingFragment settingFragment = SettingFragment.this;
                    settingFragment.sameFileCopy(new File(settingFragment.directoryModelArrayList.get(i).getPath()), new File(AppConstants.getPrivatePathBGMusic(context, "Affirmation_background_song")));
                }
            });
            populateRecyclerViewValuesFirstTime();
            this.dialog = builder.create();
            this.dialog.show();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int i2 = displayMetrics.heightPixels;
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(this.dialog.getWindow().getAttributes());
            layoutParams.height = (int) (((float) i2) * 0.8f);
            this.dialog.getWindow().setAttributes(layoutParams);
            this.back.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    setupBackView();
                }
            });
        } catch (Exception unused) {
        }
    }


    @SuppressLint({"StaticFieldLeak"})
    public void sameFileCopy(final File file, final File file2) {
        new AsyncTask<Void, Void, Void>() {
            ProgressDialog dialog;

            public Void doInBackground(Void... voidArr) {
                try {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    FileOutputStream fileOutputStream = new FileOutputStream(file2);
                    byte[] bArr = new byte[1024];
                    while (true) {
                        int read = fileInputStream.read(bArr);
                        if (read != -1) {
                            fileOutputStream.write(bArr, 0, read);
                        } else {
                            fileOutputStream.flush();
                            fileInputStream.close();
                            fileOutputStream.close();
                            return null;
                        }
                    }
                } catch (FileNotFoundException e) {
                } catch (IOException e2) {
                    e2.printStackTrace();
                    return null;
                }
                return null;

            }


            public void onPreExecute() {
                try {
                    this.dialog = new ProgressDialog(context);
                    this.dialog.setMessage("Copying Song.. Please Wait.");
                    this.dialog.show();
                } catch (Exception unused) {
                }
                super.onPreExecute();
            }


            public void onPostExecute(Void voidR) {
                try {
                    AppPref.setBackgroundMusicUrl(context, file2.getPath());
                    this.dialog.dismiss();
                } catch (Exception unused) {
                }
                super.onPostExecute(voidR);
            }
        }.execute(new Void[0]);
    }


    public void setupBackView() {
        FileAdapter fileAdapter2 = this.fileAdapter;
        if (fileAdapter2 != null && fileAdapter2.goBack()) {
            moveTaskToBack(true);
        }
    }

    public void populateRecyclerViewValuesFirstTime() {
        this.directoryModelArrayList = new ArrayList<>();
        String str = FILEPATH;
        this.directoryModel = new DirectoryModel(str, str);
        this.directoryModelArrayList.add(this.directoryModel);
        String externalStoragePath = getExternalStoragePath();
        if (externalStoragePath != null) {
            this.directoryModel = new DirectoryModel(externalStoragePath, externalStoragePath);
            this.directoryModelArrayList.add(this.directoryModel);
        }
        this.backlayout.setVisibility(View.GONE);
        setAdapter();
    }

    public void populateRecyclerViewValues(final String str) {
        runOnUiThread(new Runnable() {
            public void run() {
                if (str.equals("root")) {
                    backlayout.setVisibility(View.GONE);
                } else {
                    backlayout.setVisibility(View.VISIBLE);
                }
                directoryModelArrayList = new ArrayList<>();
                File[] listFiles = new File(str).listFiles();
                if (listFiles != null) {
                    for (int i = 0; i < listFiles.length; i++) {
                        if (listFiles[i].isDirectory()) {
                            directoryModel = new DirectoryModel(listFiles[i].getPath(), listFiles[i].getName());
                            directoryModelArrayList.add(directoryModel);
                        }
                        if (listFiles[i].isFile() && listFiles[i].getName().endsWith(".mp3")) {
                            directoryModel = new DirectoryModel(listFiles[i].getPath(), listFiles[i].getName());
                            directoryModelArrayList.add(directoryModel);
                        }
                    }
                }
                setAdapter();
            }
        });
    }


    public void setAdapter() {
        ArrayList<DirectoryModel> arrayList = this.directoryModelArrayList;
        if (arrayList != null) {
            this.fileAdapter.setListContent(arrayList);
            this.lview1.setAdapter(this.fileAdapter);
        }
    }

    public static String getExternalStoragePath() {
        String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String[] split = absolutePath.split("/");
        String str = "/";
        int length = split.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            String str2 = split[i];
            if (str2.trim().length() > 0) {
                str = str.concat(str2);
                break;
            }
            i++;
        }
        File file = new File(str);
        if (!file.exists()) {
            return null;
        }
        for (File file2 : file.listFiles()) {
            String absolutePath2 = file2.getAbsolutePath();
            if (!absolutePath2.equals(absolutePath)) {
                if (absolutePath2.toLowerCase().contains("sdcard")) {
                    return absolutePath2;
                }
                if (Build.VERSION.SDK_INT >= 21) {
                    try {
                        if (Environment.isExternalStorageRemovable(file2)) {
                            return absolutePath2;
                        }
                    } catch (Exception unused) {
                        continue;
                    }
                } else {
                    continue;
                }
            }
        }
        return null;
    }

    public void setBackupDialog() {
        this.dialogBackupBinding = (AlertDialogBackupBinding) DataBindingUtil.inflate(LayoutInflater.from(this.context), R.layout.alert_dialog_backup, (ViewGroup) null, false);
        this.dialogBackup = new Dialog(this.context);
        this.dialogBackup.setContentView(this.dialogBackupBinding.getRoot());
        this.dialogBackup.getWindow().setBackgroundDrawableResource(17170445);
        this.dialogBackup.getWindow().setLayout(-1, -2);
        this.dialogBackupBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                backupData(dialogBackupBinding.radioLocal.isChecked());
                try {
                    dialogBackup.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.dialogBackupBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    dialogBackup.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void backupData(boolean z) {
        this.backupRestore.backupRestore(this.progressDialog, z, true, (String) null, false, new OnBackupRestore() {
            public void getList(ArrayList<RestoreRowModel> arrayList) {
            }

            public void onSuccess(boolean z) {
                if (z) {
                    AppConstants.toastShort(context, context.getString(R.string.export_successfully));
                } else {
                    AppConstants.toastShort(context, context.getString(R.string.failed_to_import));
                }
            }
        });
    }

    private void checkPermAndBackup() {
        if (isHasPermissions(this, "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE")) {
            backupData();
        } else {
            requestPermissions(getString(R.string.rationale_export), Constants.REQUEST_PERM_BACKUPFILE, "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE");
        }
    }

    private void backupData() {
        showBackupDialog();
    }

    private void showBackupDialog() {
        try {
            if (this.dialogBackup != null && !this.dialogBackup.isShowing()) {
                this.dialogBackup.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isHasPermissions(Context context2, String... strArr) {
        return EasyPermissions.hasPermissions(context2, strArr);
    }

    private void requestPermissions(String str, int i, String... strArr) {
        EasyPermissions.requestPermissions((Activity) this, str, i, strArr);
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        EasyPermissions.onRequestPermissionsResult(i, strArr, iArr, this);
    }

    public void onPermissionsGranted(int i, @NonNull List<String> list) {
        if (i == 1051) {
            openFiles();
        } else if (i == 1055) {
            backupData();
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
        if (i2 != -1) {
            return;
        }
        if (i == 1002) {
            setResult(-1);
        } else if (i == 1005) {
            handleSignIn(intent);
        }
    }

}
