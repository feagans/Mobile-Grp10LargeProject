package com.test.mylifegoale.utilities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.text.Html;
import android.text.InputFilter;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.codemybrainsout.ratingdialog.RatingDialog;
import com.test.mylifegoale.R;
import com.test.mylifegoale.backupRestore.OnTwoButtonDialogClick;
import com.test.mylifegoale.databinding.AlertDialogEditTextBinding;
import com.test.mylifegoale.databinding.AlertDialogRestoreBinding;
import com.test.mylifegoale.databinding.AlertDialogTwoButtonBinding;
import com.test.mylifegoale.itemClick.EditTextDialogListener;
import com.test.mylifegoale.itemClick.TwoButtonDialogListener;
import com.test.mylifegoale.model.image.ImageRowModel;

import org.apache.commons.io.FileUtils;
import org.apache.http.protocol.HTTP;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class AppConstants {
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy MM-dd", Locale.getDefault());

    public static String getPackageResourcePathAsset() {
        return Constants.PATH_ASSET;
    }

    public static void shareApp(Context context) {
        try {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType(HTTP.PLAIN_TEXT_TYPE);
            intent.putExtra("android.intent.extra.TEXT", "My Vision Board - Visualize your dreams\nBest app to track your lifetime goals, vision of life and purpose.\n- Upload your goal images, set a title and goal deadline\n- Dashboard will help to progress of your goals and vision\n- It helps strengthen your daily positive affirmations.\n- Add your own affirmation categories and affirmations\n\nhttps://play.google.com/store/apps/details?id=" + context.getPackageName());
            context.startActivity(Intent.createChooser(intent, "Share via"));
        } catch (Exception unused) {
        }
    }

    public static void showRattingDialog(final Context context, String str) {
        RatingDialog.Builder ratingBarColor = new RatingDialog.Builder(context).session(1).title(str).threshold(4.0f).icon(context.getResources().getDrawable(R.mipmap.ic_cropped_logo)).titleTextColor(R.color.white).negativeButtonText("Never").positiveButtonTextColor(R.color.white).negativeButtonTextColor(R.color.white).formTitle("Submit Feedback").formHint("Tell us where we can improve").formSubmitText("Submit").formCancelText("Cancel").ratingBarBackgroundColor(R.color.rate_us_bg).ratingBarColor(R.color.ratingBarColor);
        RatingDialog build = ratingBarColor.playstoreUrl("https://play.google.com/store/apps/details?id=" + context.getPackageName()).onRatingBarFormSumbit(new RatingDialog.Builder.RatingDialogFormListener() {
            public void onFormSubmitted(String str) {
                AppPref.setNeverShowRatting(context, true);
            }
        }).build();
        if (AppPref.isNeverShowRatting(context)) {
            toastShort(context, "Already Submitted");
        } else {
            build.show();
        }
    }


    public static String getVersion(Context context) {
        String str = "";
        try {
            str = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return String.valueOf("Version " + str);
    }

    public static File profileStoreFile(Context context) {
        File file = new File(context.getFilesDir(), "ProfileStore");
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    public static File profilePicStoreParent(Context context) {
        String absolutePath = profileStoreFile(context).getAbsolutePath();
        return new File(absolutePath, System.currentTimeMillis() + ".jpg");
    }

    public static String getUniqueId() {
        return UUID.randomUUID().toString();
    }

    public static String getFormattedDate(long j, SimpleDateFormat simpleDateFormat) {
        return simpleDateFormat.format(new Date(j));
    }

    public static Date getFormattedDateNew(long j) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(j);
        instance.set(11, 0);
        instance.set(12, 0);
        instance.set(13, 0);
        instance.set(14, 0);
        return new Date(instance.getTimeInMillis());
    }

    public static void cursorPos(EditText editText) {
        editText.setSelection(editText.getText().length());
    }

    public static String getResIdUsingCategoryType(String str) {
        char c;
        switch (str.hashCode()) {
            case -2137395588:
                if (str.equals("Health")) {
                    c = 13;
                    break;
                }
            case -2041582609:
                if (str.equals(Constants.FOLDER_IMAGE_TYPE_Decision_Making)) {
                    c = 4;
                    break;
                }
            case -1909653710:
                if (str.equals(Constants.FOLDER_IMAGE_TYPE_Law_of_attraction)) {
                    c = 12;
                    break;
                }
            case -1707958323:
                if (str.equals(Constants.FOLDER_IMAGE_TYPE_WEALTH)) {
                    c = 22;
                    break;
                }
            case -1472251222:
                if (str.equals(Constants.FOLDER_IMAGE_TYPE_Confidence)) {
                    c = 2;
                    break;
                }
            case -1329141797:
                if (str.equals(Constants.FOLDER_IMAGE_TYPE_Self_Esteem)) {
                    c = 3;
                    break;
                }
            case -1082186784:
                if (str.equals(Constants.FOLDER_IMAGE_TYPE_Business)) {
                    c = 7;
                    break;
                }
            case -927581800:
                if (str.equals(Constants.FOLDER_IMAGE_TYPE_CREATIVITY)) {
                    c = 20;
                    break;
                }
            case -607328341:
                if (str.equals(Constants.FOLDER_IMAGE_TYPE_WEIGHT_LOSS)) {
                    c = 21;
                    break;
                }
            case -202516509:
                if (str.equals("Success")) {
                    c = 1;
                    break;
                }
            case -167747439:
                if (str.equals(Constants.FOLDER_IMAGE_TYPE_Forgiveness)) {
                    c = 11;
                    break;
                }
            case -97531304:
                if (str.equals(Constants.FOLDER_IMAGE_TYPE_Relationship)) {
                    c = 10;
                    break;
                }
            case 2374546:
                if (str.equals(Constants.FOLDER_IMAGE_TYPE_Love)) {
                    c = 8;
                    break;
                }
            case 83761118:
                if (str.equals(Constants.FOLDER_IMAGE_TYPE_Women)) {
                    c = 17;
                    break;
                }
            case 400730103:
                if (str.equals(Constants.FOLDER_IMAGE_TYPE_POSITIVE_THINKING)) {
                    c = 19;
                    break;
                }
            case 611289290:
                if (str.equals(Constants.FOLDER_IMAGE_TYPE_Attitude)) {
                    c = 6;
                    break;
                }
            case 746393357:
                if (str.equals(Constants.FOLDER_IMAGE_TYPE_Pregnancy)) {
                    c = 15;
                    break;
                }
            case 946701037:
                if (str.equals(Constants.FOLDER_IMAGE_TYPE_Gratitude)) {
                    c = 0;
                    break;
                }
            case 1479570777:
                if (str.equals(Constants.FOLDER_IMAGE_TYPE_Abundance)) {
                    c = 5;
                    break;
                }
            case 1936236199:
                if (str.equals(Constants.FOLDER_IMAGE_TYPE_PUBLIC_SPEAKING)) {
                    c = 18;
                    break;
                }
            case 1985805468:
                if (str.equals(Constants.FOLDER_IMAGE_TYPE_Beauty)) {
                    c = 16;
                    break;
                }
            case 2096973700:
                if (str.equals(Constants.FOLDER_IMAGE_TYPE_Family)) {
                    c = 9;
                    break;
                }
            case 2120967672:
                if (str.equals(Constants.FOLDER_IMAGE_TYPE_Exercise)) {
                    c = 14;
                    break;
                }
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                return getPackageResourcePathAsset() + "folder_gratitude.png";
            case 1:
                return getPackageResourcePathAsset() + "folder_success.png";
            case 2:
                return getPackageResourcePathAsset() + "folder_confidence.png";
            case 3:
                return getPackageResourcePathAsset() + "folder_self_esteem.png";
            case 4:
                return getPackageResourcePathAsset() + "folder_decision_making.png";
            case 5:
                return getPackageResourcePathAsset() + "folder_abundance.png";
            case 6:
                return getPackageResourcePathAsset() + "folder_attitude.png";
            case 7:
                return getPackageResourcePathAsset() + "folder_business.png";
            case 8:
                return getPackageResourcePathAsset() + "folder_love.png";
            case 9:
                return getPackageResourcePathAsset() + "folder_family_group_of_three.png";
            case 10:
                return getPackageResourcePathAsset() + "folder_relationship.png";
            case 11:
                return getPackageResourcePathAsset() + "folder_forgiveness.png";
            case 12:
                return getPackageResourcePathAsset() + "folder_law_of_attraction.png";
            case 13:
                return getPackageResourcePathAsset() + "folder_health.png";
            case 14:
                return getPackageResourcePathAsset() + "folder_exercise.png";
            case 15:
                return getPackageResourcePathAsset() + "folder_pregnancy.png";
            case 16:
                return getPackageResourcePathAsset() + "folder_beauty.png";
            case 17:
                return getPackageResourcePathAsset() + "folder_woman.png";
            case 18:
                return getPackageResourcePathAsset() + "folder_public_speaking.png";
            case 19:
                return getPackageResourcePathAsset() + "folder_positive_thinking.png";
            case 20:
                return getPackageResourcePathAsset() + "folder_creativity.png";
            case 21:
                return getPackageResourcePathAsset() + "folder_weightloss.png";
            case 22:
                return getPackageResourcePathAsset() + "folder_wealth.png";
            default:
                return getPackageResourcePathAsset() + "folder_confidence.png";
        }
    }

    @SuppressLint("ResourceType")
    public static void showTwoButtonDialog(Context context, String str, String str2, boolean z, boolean z2, String str3, String str4, final TwoButtonDialogListener twoButtonDialogListener) {
        int i = 0;
        AlertDialogTwoButtonBinding alertDialogTwoButtonBinding = (AlertDialogTwoButtonBinding) DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.alert_dialog_two_button, (ViewGroup) null, false);
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(alertDialogTwoButtonBinding.getRoot());
        dialog.setCancelable(z);
        dialog.getWindow().setBackgroundDrawableResource(17170445);
        alertDialogTwoButtonBinding.txtTitle.setText(str);
        alertDialogTwoButtonBinding.txtDec.setText(Html.fromHtml(str2));
        TextView textView = alertDialogTwoButtonBinding.btnCancel;
        if (!z2) {
            i = 8;
        }
        textView.setVisibility(i);
        alertDialogTwoButtonBinding.btnCancel.setText(str4);
        alertDialogTwoButtonBinding.btnOk.setText(str3);
        alertDialogTwoButtonBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                twoButtonDialogListener.onCancel();
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        alertDialogTwoButtonBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                twoButtonDialogListener.onOk();
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

    public static int getDPToPixel(Context context, int i) {
        return (int) TypedValue.applyDimension(1, (float) i, context.getResources().getDisplayMetrics());
    }

    public static String getPrivatePathImage(Context context, String str) {
        File file = new File(context.getDatabasePath(Constants.APP_DB_NAME).getParent() + Constants.PATH_IMAGE);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file + "/" + str + ".jpg";
    }

    public static String getPrivatePathSound(Context context, String str) {
        File file = new File(context.getDatabasePath(Constants.APP_DB_NAME).getParent() + Constants.PATH_SOUND);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file + "/" + str + ".mp3";
    }

    public static String getPrivatePathBGMusic(Context context, String str) {
        File file = new File(context.getDatabasePath(Constants.APP_DB_NAME).getParent() + Constants.PATH_BG_MUSIC);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file + "/" + str + ".mp3";
    }

    public static int getRandomWithBound(int i) {
        return new Random().nextInt(i);
    }

    public static ArrayList<ImageRowModel> getListBackgroundImage() {
        ArrayList<ImageRowModel> arrayList = new ArrayList<>();
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "1.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "2.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "3.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "4.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "5.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "6.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "7.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "8.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "9.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "10.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "11.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "12.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "13.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "14.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "15.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "16.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "17.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "18.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "19.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "20.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "21.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "22.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "23.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "24.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "25.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "26.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "27.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "28.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "29.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "30.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "31.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "32.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "33.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "34.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "35.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "36.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "37.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "38.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "39.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "40.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "41.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "42.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "43.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "44.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "45.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "46.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "47.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "48.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "49.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "50.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "51.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "52.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "53.webp"));
        arrayList.add(new ImageRowModel(getPackageResourcePathAsset() + "54.webp"));
        return arrayList;
    }

    public static void setEditTextSelection(final EditText editText) {
        if (editText != null) {
            editText.post(new Runnable() {
                public void run() {
                    editText.setSelection(editText.getText().length());
                }
            });
        }
    }

    public static void hideKeyboard(Context context, View view) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String getPackageResourcePath(Context context) {
        return Constants.PATH_RESOURCE + context.getPackageName() + "/";
    }

    @SuppressLint("ResourceType")
    public static void showEditTextDialog(Context context, boolean z, boolean z2, boolean z3, int i, String str, String str2, String str3, String str4, EditTextDialogListener editTextDialogListener) {
        int i2 = i;
        String str5 = str;
        String str6 = str2;
        String str7 = str3;
        AlertDialogEditTextBinding alertDialogEditTextBinding = (AlertDialogEditTextBinding) DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.alert_dialog_edit_text, (ViewGroup) null, false);
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(alertDialogEditTextBinding.getRoot());
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(17170445);
        if (z2 && z3) {
            alertDialogEditTextBinding.etValue.setInputType(8194);
        } else if (z2) {
            alertDialogEditTextBinding.etValue.setInputType(2);
        }
        if (i2 != -1) {
            alertDialogEditTextBinding.etValue.setFilters(new InputFilter[]{new InputFilter.LengthFilter(i2)});
        }
        if (str5 != null && str.length() > 0) {
            alertDialogEditTextBinding.txtTitle.setText(str5);
        }
        if (str6 != null && str2.length() > 0) {
            EditText editText = alertDialogEditTextBinding.etValue;
            editText.setHint("Enter " + str6);
        }
        if (str7 != null && str3.length() > 0) {
            alertDialogEditTextBinding.etValue.setText(str7);
        }
        if (z) {
            alertDialogEditTextBinding.etValue.setSelectAllOnFocus(true);
        } else {
            setEditTextSelection(alertDialogEditTextBinding.etValue);
        }
        alertDialogEditTextBinding.btnOk.setText(str4);
        final AlertDialogEditTextBinding alertDialogEditTextBinding2 = alertDialogEditTextBinding;
        final EditTextDialogListener editTextDialogListener2 = editTextDialogListener;
        final Dialog dialog2 = dialog;
        final Context context3 = context;
        final String str8 = str2;
        alertDialogEditTextBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (alertDialogEditTextBinding2.etValue.getText() == null || alertDialogEditTextBinding2.etValue.getText().toString().trim().length() <= 0) {
                    AppConstants.hideKeyboard(context3, alertDialogEditTextBinding2.etValue);
                    Context context = context3;
                    AppConstants.toastShort(context, "Please enter valid " + str8);
                    return;
                }
                editTextDialogListener2.onOk(alertDialogEditTextBinding2.etValue.getText().toString());
                try {
                    dialog2.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        alertDialogEditTextBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
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

    public static void toastShort(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    public static void refreshFile(Context context, File file) {
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(Uri.fromFile(file));
        context.sendBroadcast(intent);
    }

    public static int getDefaultSong(String str) {
        char c;
        switch (str.hashCode()) {
            case 109620668:
                if (str.equals("song1")) {
                    c = 0;
                    break;
                }
            case 109620669:
                if (str.equals("song2")) {
                    c = 1;
                    break;
                }
            case 109620670:
                if (str.equals("song3")) {
                    c = 2;
                    break;
                }
            case 109620671:
                if (str.equals("song4")) {
                    c = 3;
                    break;
                }
            case 109620672:
                if (str.equals("song5")) {
                    c = 4;
                    break;
                }
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                return R.raw.deepheal;
            case 1:
                return R.raw.meditation_music;
            case 2:
                return R.raw.naturesound;
            case 3:
                return R.raw.relaxmusic_default;
            case 4:
                return R.raw.soothingmusic;
            default:
                return R.raw.relaxmusic_default;
        }
    }

    public static String getPrivatePathBGVoice(Context context, String str) {
        File file = new File(context.getDatabasePath(Constants.APP_DB_NAME).getParent() + Constants.PATH_BG_VOICE);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file + "/" + str + ".mp3";
    }

    public static String getDarkColor(String str) {
        char c;
        switch (str.hashCode()) {
            case -1812166394:
                if (str.equals(Constants.FOLDER_COLOR_TYPE_Beauty)) {
                    c = 16;
                    break;
                }
            case -1675238226:
                if (str.equals(Constants.FOLDER_COLOR_TYPE_Family)) {
                    c = 9;
                    break;
                }
            case -1641359972:
                if (str.equals(Constants.FOLDER_COLOR_TYPE_Self_Esteem)) {
                    c = 3;
                    break;
                }
            case -1640990948:
                if (str.equals(Constants.FOLDER_COLOR_TYPE_Exercise)) {
                    c = 14;
                    break;
                }
            case -1616967045:
                if (str.equals(Constants.FOLDER_COLOR_TYPE_Health)) {
                    c = 13;
                    break;
                }
            case -1610867565:
                if (str.equals(Constants.FOLDER_COLOR_TYPE_Relationship)) {
                    c = 10;
                    break;
                }
            case -1600653080:
                if (str.equals(Constants.FOLDER_COLOR_TYPE_Forgiveness)) {
                    c = 11;
                    break;
                }
            case -1599372001:
                if (str.equals(Constants.FOLDER_COLOR_TYPE_Attitude)) {
                    c = 6;
                    break;
                }
            case -1568952515:
                if (str.equals("#9ebef1")) {
                    c = 2;
                    break;
                }
            case -472734546:
                if (str.equals(Constants.FOLDER_COLOR_TYPE_Law_of_attraction)) {
                    c = 12;
                    break;
                }
            case -472720020:
                if (str.equals(Constants.FOLDER_COLOR_TYPE_Gratitude)) {
                    c = 0;
                    break;
                }
            case -414565245:
                if (str.equals(Constants.FOLDER_COLOR_TYPE_Pregnancy)) {
                    c = 15;
                    break;
                }
            case -408524399:
                if (str.equals(Constants.FOLDER_COLOR_TYPE_Women)) {
                    c = 17;
                    break;
                }
            case -397084128:
                if (str.equals(Constants.FOLDER_COLOR_TYPE_Decision_Making)) {
                    c = 4;
                    break;
                }
            case -325266734:
                if (str.equals(Constants.FOLDER_COLOR_TYPE_Business)) {
                    c = 7;
                    break;
                }
            case -323453685:
                if (str.equals(Constants.FOLDER_COLOR_TYPE_Abundance)) {
                    c = 5;
                    break;
                }
            case -281638654:
                if (str.equals(Constants.FOLDER_COLOR_TYPE_Success)) {
                    c = 1;
                    break;
                }
            case -279721474:
                if (str.equals(Constants.FOLDER_COLOR_TYPE_Love)) {
                    c = 8;
                    break;
                }
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                return "#5ca553";
            case 1:
                return "#e27851";
            case 2:
                return "#6f8fca";
            case 3:
                return "#5454da";
            case 4:
                return "#875fd9";
            case 5:
                return "#de6a6b";
            case 6:
                return "#4ba5c7";
            case 7:
                return "#d05aa4";
            case 8:
                return "#c05237";
            case 9:
                return "#47667a";
            case 10:
                return "#73549a";
            case 11:
                return "#5f678f";
            case 12:
                return "#738b4d";
            case 13:
                return "#59b5a8";
            case 14:
                return "#4a6f9d";
            case 15:
                return "#6c7137";
            case 16:
                return "#17535e";
            case 17:
                return "#a84c4c";
            default:
                return Constants.FOLDER_COLOR_TYPE_Success;
        }
    }

    @SuppressLint("ResourceType")
    public static void showTwoButtonDialog(Context context, String str, String str2, boolean z, boolean z2, String str3, String str4, final OnTwoButtonDialogClick onTwoButtonDialogClick) {
        int i = 0;
        AlertDialogTwoButtonBinding alertDialogTwoButtonBinding = (AlertDialogTwoButtonBinding) DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.alert_dialog_two_button, (ViewGroup) null, false);
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(alertDialogTwoButtonBinding.getRoot());
        dialog.setCancelable(z);
        dialog.getWindow().setBackgroundDrawableResource(17170445);
        alertDialogTwoButtonBinding.txtTitle.setText(str);
        alertDialogTwoButtonBinding.txtDec.setText(Html.fromHtml(str2));
        TextView textView = alertDialogTwoButtonBinding.btnCancel;
        if (!z2) {
            i = 8;
        }
        textView.setVisibility(i);
        alertDialogTwoButtonBinding.btnCancel.setText(str4);
        alertDialogTwoButtonBinding.btnOk.setText(str3);
        alertDialogTwoButtonBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onTwoButtonDialogClick.onCancel();
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        alertDialogTwoButtonBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onTwoButtonDialogClick.onOk();
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

    @SuppressLint("ResourceType")
    public static void showRestoreDialog(Context context, String str2, boolean z, boolean z2, final OnTwoButtonDialogClick onTwoButtonDialogClick) {
        int i = 0;
        AlertDialogRestoreBinding alertDialogRestoreBinding = (AlertDialogRestoreBinding) DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.alert_dialog_restore, (ViewGroup) null, false);
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(alertDialogRestoreBinding.getRoot());
        dialog.setCancelable(z);
        dialog.getWindow().setBackgroundDrawableResource(17170445);
        alertDialogRestoreBinding.txtDec.setText(Html.fromHtml(str2));
        TextView textView = alertDialogRestoreBinding.btnCancel;
        if (!z2) {
            i = 8;
        }
        textView.setVisibility(i);
        alertDialogRestoreBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        alertDialogRestoreBinding.btnDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onTwoButtonDialogClick.onOk();
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        alertDialogRestoreBinding.btnMerge.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onTwoButtonDialogClick.onCancel();
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

    public static void deleteTempFile(Context context) {
        try {
            FileUtils.deleteDirectory(new File(getRootPath(context) + "/temp"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getRootPath(Context context) {
        return new File(context.getDatabasePath(Constants.APP_DB_NAME).getParent()).getParent();
    }

    public static String getLocalFileDir() {
        File file = new File(Environment.getExternalStorageDirectory() + "/" + Constants.DB_BACKUP_DIRECTORY);
        if (!file.exists()) {
            file.mkdir();
        }
        return file.getAbsolutePath();
    }

    public static String getTempFileDir(Context context) {
        File file = new File(getRootPath(context) + "/temp");
        if (!file.exists()) {
            file.mkdir();
        }
        return file.getAbsolutePath();
    }

    public static String getLocalZipFilePath() {
        return getLocalFileDir() + File.separator + getBackupName();
    }

    public static String getRemoteZipFilePath(Context context) {
        return getTempFileDir(context) + File.separator + getBackupName();
    }

    public static String getBackupName() {
        return "Backup_" + getFormattedDate(System.currentTimeMillis(), Constants.FILE_DATE_FORMAT) + ".zip";
    }

    public static void deleteImageBeforeRestoreFile(Context context) {
        try {
            FileUtils.deleteDirectory(profileStoreFile(context));
            FileUtils.deleteDirectory(new File(context.getDatabasePath(Constants.APP_DB_NAME).getParent() + Constants.PATH_IMAGE));
            FileUtils.deleteDirectory(new File(context.getDatabasePath(Constants.APP_DB_NAME).getParent() + Constants.PATH_SOUND));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
