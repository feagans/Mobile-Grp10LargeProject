package com.test.mylifegoale.backupRestore;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.test.mylifegoale.utilities.AppConstants;
import com.test.mylifegoale.utilities.Constants;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;

public class ZipUnZipAsync extends AsyncTask<Object, Object, Boolean> {
    Activity activity;
    BackupRestoreProgress dialog;
    ArrayList<File> fileListForZip;
    String fileToRestore;
    boolean isZip;
    OnBackupRestore onBackupRestore;
    String pass = "aaa";
    String tempZipFilePath;


    public ZipUnZipAsync(BackupRestoreProgress backupRestoreProgress, Activity activity2, boolean z, ArrayList<File> arrayList, String str, String str2, OnBackupRestore onBackupRestore2) {
        this.activity = activity2;
        this.isZip = z;
        this.dialog = backupRestoreProgress;
        this.tempZipFilePath = str2;
        this.fileToRestore = str;
        this.fileListForZip = arrayList;
        this.onBackupRestore = onBackupRestore2;
    }

    public void onPreExecute() {
        super.onPreExecute();
        this.dialog.setMessage(this.isZip ? "Creating Zip..." : "Extracting Zip...");
        this.dialog.showDialog();
    }

    public Boolean doInBackground(Object... objArr) {
        if (this.isZip) {
            return Boolean.valueOf(encryptedZip(this.fileListForZip, this.tempZipFilePath));
        }
        return Boolean.valueOf(decryptedZip(this.fileToRestore));
    }

    public void onPostExecute(Boolean bool) {
        super.onPostExecute(bool);
        this.dialog.dismissDialog();
        this.onBackupRestore.onSuccess(bool.booleanValue());
    }

    public boolean encryptedZip(ArrayList<File> arrayList, String str) {
        try {
            ZipFile zipFile = new ZipFile(str);
            ZipParameters zipParameters = new ZipParameters();
            zipParameters.setCompressionMethod(8);
            zipParameters.setCompressionLevel(5);
            zipParameters.setEncryptFiles(true);
            zipParameters.setEncryptionMethod(99);
            zipParameters.setAesKeyStrength(3);
            zipParameters.setPassword(this.pass);
            for (int i = 0; i < arrayList.size(); i++) {
                Log.i("decryptedZip", "encryptedZip: " + arrayList.get(i).isDirectory());
                if (!arrayList.get(i).isDirectory()) {
                    Log.i("decryptedZip", "encryptedZip:if " + arrayList.get(i).getAbsolutePath());
                    zipFile.addFile(arrayList.get(i), zipParameters);
                } else {
                    Log.i("decryptedZip", "encryptedZip:else " + arrayList.get(i).getAbsolutePath());
                    zipFile.addFolder(arrayList.get(i), zipParameters);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean decryptedZip(String str) {
        AppConstants.getRootPath(this.activity);
        try {
            ZipFile zipFile = new ZipFile(str);
            if (zipFile.isEncrypted()) {
                zipFile.setPassword(this.pass);
            }
            zipFile.extractAll(AppConstants.getTempFileDir(this.activity) + File.separator);
            File[] listFiles = new File(AppConstants.getTempFileDir(this.activity)).listFiles();
            if (listFiles.length > 0) {
                for (File file : listFiles) {
                    Log.i("decryptedZip", "decryptedZip: " + file.getAbsolutePath());
                    if (file.isDirectory()) {
                        if (!file.getName().contains("Background")) {
                            FileUtils.copyDirectory(new File(file.getAbsolutePath()), getDatabaseParent(file.getName()));
                        }
                    } else if (FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("jpg")) {
                        FileUtils.copyFile(new File(file.getAbsolutePath()), new File(AppConstants.profileStoreFile(this.activity) + File.separator + file.getName()));
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    private File getDatabaseParent(String str) {
        File file = new File(this.activity.getDatabasePath(Constants.APP_DB_NAME).getParent() + File.separator + str);
        if (file.exists()) {
            file.mkdir();
        }
        return file;
    }

}
