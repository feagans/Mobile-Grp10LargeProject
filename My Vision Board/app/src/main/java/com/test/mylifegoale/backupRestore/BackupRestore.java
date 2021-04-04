package com.test.mylifegoale.backupRestore;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.test.mylifegoale.R;
import com.test.mylifegoale.base.roomDb.AppDatabase;
import com.test.mylifegoale.utilities.AppConstants;
import com.test.mylifegoale.utilities.Constants;

import org.apache.commons.io.FileUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class BackupRestore {
    String METADATA_FILE_PARENT = "appDataFolder";
    String METADATA_FILE_TYPE = "application/zip";
    private Activity activity;

    public Drive driveService;
    FileList fileList = null;
    File fileMetadata;
    java.io.File filePath;
    boolean isSuccessCreate = true;
    boolean isSuccessDelete = true;
    FileContent mediaContent;
    OutputStream outputStream = null;

    public BackupRestore(Activity activity2) {
        this.activity = activity2;
    }

    public void backupRestore(BackupRestoreProgress backupRestoreProgress, boolean z, boolean z2, String str, boolean z3, OnBackupRestore onBackupRestore) {
        if (z) {
            localBackUpAndRestore(backupRestoreProgress, z2, str, z3, onBackupRestore);
        } else {
            driveBackupRestore(backupRestoreProgress, z2, str, z3, onBackupRestore);
        }
    }

    private void localBackUpAndRestore(BackupRestoreProgress backupRestoreProgress, boolean z, String str, boolean z2, OnBackupRestore onBackupRestore) {
        backupRestoreProgress.showDialog();
        AppConstants.deleteTempFile(this.activity);
        if (z) {
            startLocalBackUp(backupRestoreProgress, onBackupRestore);
        } else {
            startLocalRestore(backupRestoreProgress, str, z2, onBackupRestore);
        }
    }

    private void startLocalBackUp(BackupRestoreProgress backupRestoreProgress, OnBackupRestore onBackupRestore) {
        String localZipFilePath = AppConstants.getLocalZipFilePath();
        Activity activity2 = this.activity;
        new ZipUnZipAsync(backupRestoreProgress, activity2, true, getAllFilesForBackup(activity2, AppConstants.getRootPath(activity2)), "", localZipFilePath, onBackupRestore).execute(new Object[0]);
    }

    private ArrayList<java.io.File> getAllFilesForBackup(Activity activity2, String str) {
        ArrayList<java.io.File> arrayList = new ArrayList<>();
        java.io.File[] listFiles = new java.io.File(activity2.getFilesDir(), "ProfileStore").listFiles();
        if (listFiles != null && listFiles.length > 0) {
            for (java.io.File add : listFiles) {
                arrayList.add(add);
            }
        }
        java.io.File[] listFiles2 = new java.io.File(str + "/databases").listFiles();
        if (listFiles2 != null && listFiles2.length > 0) {
            for (int i = 0; i < listFiles2.length; i++) {
                if (!listFiles2[i].isDirectory()) {
                    arrayList.add(listFiles2[i]);
                } else if (!listFiles2[i].getName().contains("Background") && listFiles2[i].listFiles().length > 0) {
                    arrayList.add(listFiles2[i]);
                }
            }
        }
        return arrayList;
    }


    public void startLocalRestore(BackupRestoreProgress backupRestoreProgress, String str, final boolean zs, final OnBackupRestore onBackupRestore) {
        AppConstants.deleteImageBeforeRestoreFile(this.activity);
        new ZipUnZipAsync(backupRestoreProgress, this.activity, false, (ArrayList<java.io.File>) null, str, "", new OnBackupRestore() {
            public void onSuccess(boolean z) {
                onBackupRestore.onSuccess(z);
                localRestore(zs);
            }

            public void getList(ArrayList<RestoreRowModel> arrayList) {
                onBackupRestore.getList(arrayList);
            }
        }).execute(new Object[0]);
    }


    public void localRestore(boolean z) {
        SupportSQLiteDatabase writableDatabase = AppDatabase.getAppDatabase(this.activity).getOpenHelper().getWritableDatabase();
        if (!z) {
            deleteAllTableData(writableDatabase);
        }
        int i = 0;
        writableDatabase.execSQL(String.format("ATTACH DATABASE '%s' AS encrypted;", new Object[]{AppConstants.getTempFileDir(this.activity) + java.io.File.separator + Constants.APP_DB_NAME}));
        try {
            Cursor query = writableDatabase.query("SELECT name FROM encrypted.sqlite_master WHERE name='dbVersion'");
            if (query.moveToNext()) {
                Cursor query2 = writableDatabase.query("SELECT MAX(versionNumber) FROM encrypted.dbVersion");
                if (query2.moveToNext()) {
                    i = query2.getInt(0);
                }
            } else {
                i = 2;
            }
            query.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (i == 2) {
            MigrateFrom2_3(writableDatabase);
        }
        replaceAllTableData(z, writableDatabase);
        writableDatabase.execSQL("DETACH DATABASE encrypted");
    }

    private void MigrateFrom2_3(SupportSQLiteDatabase supportSQLiteDatabase) {
        supportSQLiteDatabase.execSQL("ALTER TABLE encrypted.visionData ADD COLUMN 'ord' INTEGER NOT NULL DEFAULT 0");
        supportSQLiteDatabase.execSQL("UPDATE encrypted.visionData set ord = rowid");
        Log.d("RestoreLog", "ord column added");
    }

    private void deleteAllTableData(SupportSQLiteDatabase supportSQLiteDatabase) {
        supportSQLiteDatabase.execSQL("DELETE FROM visionData");
        supportSQLiteDatabase.execSQL("DELETE FROM categoryData");
        supportSQLiteDatabase.execSQL("DELETE FROM lifePurposeData");
        supportSQLiteDatabase.execSQL("DELETE FROM diaryData");
        supportSQLiteDatabase.execSQL("DELETE FROM folderList");
        supportSQLiteDatabase.execSQL("DELETE FROM affirmationList");
    }

    private void replaceAllTableData(boolean z, SupportSQLiteDatabase supportSQLiteDatabase) {
        replaceAll(supportSQLiteDatabase, z, "visionData");
        replaceAll(supportSQLiteDatabase, z, "categoryData");
        replaceAll(supportSQLiteDatabase, z, "lifePurposeData");
        replaceAll(supportSQLiteDatabase, z, "diaryData");
        replaceAll(supportSQLiteDatabase, z, "folderList");
        replaceAll(supportSQLiteDatabase, z, "affirmationList");
    }

    private void replaceAll(SupportSQLiteDatabase supportSQLiteDatabase, boolean z, String str) {
        if (z) {
            try {
                supportSQLiteDatabase.execSQL("insert into " + str + " select b.* from encrypted." + str + " b  left join " + str + " c on c.id=b.id  where c.id is null ");
            } catch (SQLException e) {
                e.printStackTrace();
                Log.e("replaceAll", "replaceAll: " + e.toString());
            }
        } else {
            try {
                supportSQLiteDatabase.execSQL("insert into " + str + " select * from encrypted." + str);
            } catch (SQLException e2) {
                e2.printStackTrace();
                Log.e("replaceAll", "replaceAll: " + e2.toString());
            }
        }
    }

    private void driveBackupRestore(BackupRestoreProgress backupRestoreProgress, boolean z, String str, boolean z2, OnBackupRestore onBackupRestore) {
        GoogleSignInAccount lastSignedInAccount = GoogleSignIn.getLastSignedInAccount(this.activity);
        if (lastSignedInAccount == null) {
            signIn();
            return;
        }
        setCredentials(lastSignedInAccount);
        startDriveOperation(z, str, z2, backupRestoreProgress, onBackupRestore);
    }

    public void driveBackupList(BackupRestoreProgress backupRestoreProgress, OnBackupRestore onBackupRestore) {
        GoogleSignInAccount lastSignedInAccount = GoogleSignIn.getLastSignedInAccount(this.activity);
        if (lastSignedInAccount == null) {
            signIn();
            return;
        }
        setCredentials(lastSignedInAccount);
        listFilesFromAppFolder(backupRestoreProgress, onBackupRestore);
    }

    private void listFilesFromAppFolder(final BackupRestoreProgress backupRestoreProgress, final OnBackupRestore onBackupRestore) {
        new AsyncTask<Void, Void, Void>() {

            public void onPreExecute() {
                backupRestoreProgress.setMessage("Fetching backups...");
                backupRestoreProgress.showDialog();
                super.onPreExecute();
            }


            public Void doInBackground(Void... voidArr) {
                try {
                    Drive.Files.List list = driveService.files().list();
                    fileList = (FileList) list.setQ("mimeType ='" + METADATA_FILE_TYPE + "'").setSpaces(METADATA_FILE_PARENT).setFields("files(id, name,size,createdTime,modifiedTime)").execute();
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }


            public void onPostExecute(Void voidR) {
                backupRestoreProgress.dismissDialog();
                onBackupRestore.onSuccess(true);
                onBackupRestore.getList(getBackupList());
                super.onPostExecute(voidR);
            }
        }.execute(new Void[0]);
    }


    public ArrayList<RestoreRowModel> getBackupList() {
        ArrayList<RestoreRowModel> arrayList = new ArrayList<>();
        for (int i = 0; i < this.fileList.getFiles().size(); i++) {
            File file2 = this.fileList.getFiles().get(i);
            long longValue = file2.getSize().longValue() / FileUtils.ONE_KB;
            String name = file2.getName();
            String id = file2.getId();
            String formattedDate = AppConstants.getFormattedDate(file2.getModifiedTime().getValue(), Constants.FILE_DATE_FORMAT);
            arrayList.add(new RestoreRowModel(name, id, formattedDate, longValue + "KB", file2.getModifiedTime().getValue()));
        }
        return arrayList;
    }

    private void signIn() {
        GoogleSignInClient googleSignInClient = buildGoogleSignInClient();
        Intent intent = googleSignInClient.getSignInIntent();
        this.activity.startActivityForResult(intent, Constants.REQUEST_CODE_SIGN_IN);
    }

    private GoogleSignInClient buildGoogleSignInClient() {
        return GoogleSignIn.getClient(this.activity,
                new GoogleSignInOptions.Builder(
                        GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestScopes(
                                new Scope("https://www.googleapis.com/auth/drive.appdata"),
                                new Scope[0]).
                        requestEmail().
                        requestProfile().
                        build());
    }


    public void startDriveOperation(boolean z, String str, boolean z2, BackupRestoreProgress backupRestoreProgress, OnBackupRestore onBackupRestore) {
        AppConstants.deleteTempFile(this.activity);
        if (z) {
            startDriveBackup(str, backupRestoreProgress, onBackupRestore);
            return;
        }
        downloadRestore(backupRestoreProgress, str, AppConstants.getRemoteZipFilePath(this.activity), z2, onBackupRestore);
    }

    public void handleSignInResult(Intent intent, boolean z, boolean z2, String str, BackupRestoreProgress backupRestoreProgress, OnBackupRestore onBackupRestore) {
        try {
            final boolean z3 = z2;
            final boolean z4 = z;
            final String str2 = str;
            final BackupRestoreProgress backupRestoreProgress2 = backupRestoreProgress;
            final OnBackupRestore onBackupRestore2 = onBackupRestore;
            GoogleSignIn.getSignedInAccountFromIntent(intent).addOnSuccessListener(new OnSuccessListener<GoogleSignInAccount>() {
                public void onSuccess(GoogleSignInAccount googleSignInAccount) {
                    Log.d("handleSignInResult", "Signed in as " + googleSignInAccount.getEmail());
                    setCredentials(googleSignInAccount);
                    if (!z3) {
                        startDriveOperation(z4, str2, false, backupRestoreProgress2, onBackupRestore2);
                    } else {
                        driveBackupList(backupRestoreProgress2, onBackupRestore2);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                public void onFailure(@NonNull Exception exc) {
                    Log.e("handleSignInResult", exc.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setCredentials(GoogleSignInAccount googleSignInAccount) {
        GoogleAccountCredential usingOAuth2 = GoogleAccountCredential.usingOAuth2(this.activity, Collections.singleton("https://www.googleapis.com/auth/drive.appdata"));
        usingOAuth2.setSelectedAccount(googleSignInAccount.getAccount());
        this.driveService = new Drive.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), usingOAuth2).setApplicationName(this.activity.getString(R.string.app_name)).build();
    }

    private void startDriveBackup(String str, final BackupRestoreProgress backupRestoreProgress, final OnBackupRestore onBackupRestore) {
        backupRestoreProgress.showDialog();
        if (str == null) {
            final String remoteZipFilePath = AppConstants.getRemoteZipFilePath(this.activity);
            Activity activity2 = this.activity;
            new ZipUnZipAsync(backupRestoreProgress, activity2, true, getAllFilesForBackup(activity2, AppConstants.getRootPath(activity2)), "", remoteZipFilePath, new OnBackupRestore() {
                public void getList(ArrayList<RestoreRowModel> arrayList) {
                }

                public void onSuccess(boolean z) {
                    createFileInAppFolder(backupRestoreProgress, remoteZipFilePath, driveService, onBackupRestore);
                }
            }).execute(new Object[0]);
            return;
        }
        createFileInAppFolder(backupRestoreProgress, str, this.driveService, onBackupRestore);
    }


    public void createFileInAppFolder(BackupRestoreProgress backupRestoreProgress, String str, Drive drive, OnBackupRestore onBackupRestore) {
        final BackupRestoreProgress backupRestoreProgress2 = backupRestoreProgress;
        final String str2 = str;
        final Drive drive2 = drive;
        final OnBackupRestore onBackupRestore2 = onBackupRestore;
        new AsyncTask<Void, Void, Void>() {

            public void onPreExecute() {
                backupRestoreProgress2.setMessage("Uploading to drive...");
                backupRestoreProgress2.showDialog();
                fileMetadata = new File();
                fileMetadata.setName(AppConstants.getBackupName());
                fileMetadata.setParents(Collections.singletonList(METADATA_FILE_PARENT));
                filePath = new java.io.File(str2);

                mediaContent = new FileContent(METADATA_FILE_TYPE, filePath);
                super.onPreExecute();
            }


            public Void doInBackground(Void... voidArr) {
                try {
                    File file = (File) drive2.files().create(fileMetadata, mediaContent).setFields("id").execute();

                    return null;
                } catch (IOException e) {
                    isSuccessCreate = false;
                    e.printStackTrace();
                    return null;
                }
            }


            public void onPostExecute(Void voidR) {
                backupRestoreProgress2.dismissDialog();
                onBackupRestore2.onSuccess(isSuccessCreate);
                super.onPostExecute(voidR);
            }
        }.execute(new Void[0]);
    }

    private void downloadRestore(BackupRestoreProgress backupRestoreProgress, String str, String str2, boolean z, OnBackupRestore onBackupRestore) {
        final BackupRestoreProgress backupRestoreProgress2 = backupRestoreProgress;
        final String str3 = str2;
        final String str4 = str;
        final boolean z2 = z;
        final OnBackupRestore onBackupRestore2 = onBackupRestore;
        new AsyncTask<Void, Void, Void>() {

            public void onPreExecute() {
                backupRestoreProgress2.showDialog();
                super.onPreExecute();
            }


            public Void doInBackground(Void... voidArr) {
                try {
                    outputStream = new FileOutputStream(str3);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    driveService.files().get(str4).executeMediaAndDownloadTo(outputStream);
                    return null;
                } catch (IOException e2) {
                    e2.printStackTrace();
                    return null;
                }
            }


            public void onPostExecute(Void voidR) {
                backupRestoreProgress2.dismissDialog();
                if (new java.io.File(str3).exists()) {
                    startLocalRestore(backupRestoreProgress2, str3, z2, onBackupRestore2);
                }
                super.onPostExecute(voidR);
            }
        }.execute(new Void[0]);
    }

    public void deleteFromDrive(final BackupRestoreProgress backupRestoreProgress, final String str, final OnBackupRestore onBackupRestore) {
        new AsyncTask<Void, Void, Void>() {

            public void onPreExecute() {
                backupRestoreProgress.setMessage("Deleting from Drive...");
                backupRestoreProgress.showDialog();
                super.onPreExecute();
            }


            public Void doInBackground(Void... voidArr) {
                try {
                    driveService.files().delete(str).execute();
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    isSuccessDelete = false;
                    return null;
                }
            }


            public void onPostExecute(Void voidR) {
                backupRestoreProgress.dismissDialog();
                onBackupRestore.onSuccess(isSuccessDelete);
                super.onPostExecute(voidR);
            }
        }.execute(new Void[0]);
    }
}
