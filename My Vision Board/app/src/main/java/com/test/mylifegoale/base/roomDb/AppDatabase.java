package com.test.mylifegoale.base.roomDb;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.test.mylifegoale.base.dao.AffirmationDao;
import com.test.mylifegoale.base.dao.CategoryDAO;
import com.test.mylifegoale.base.dao.DbVersionDAO;
import com.test.mylifegoale.base.dao.DiaryDataDAO;
import com.test.mylifegoale.base.dao.FolderDao;
import com.test.mylifegoale.base.dao.LifePurposeDAO;
import com.test.mylifegoale.base.dao.VisionDAO;
import com.test.mylifegoale.model.AffirmationRowModel;
import com.test.mylifegoale.model.CategoryModel;
import com.test.mylifegoale.model.DbVersionModel;
import com.test.mylifegoale.model.DiaryData;
import com.test.mylifegoale.model.FolderRowModel;
import com.test.mylifegoale.model.LifePurposeModel;
import com.test.mylifegoale.model.VisionModel;
import com.test.mylifegoale.utilities.AppPref;
import com.test.mylifegoale.utilities.Constants;

@Database(entities = {VisionModel.class, CategoryModel.class, LifePurposeModel.class, DiaryData.class, FolderRowModel.class, AffirmationRowModel.class, DbVersionModel.class}, exportSchema = false, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        public void migrate(SupportSQLiteDatabase supportSQLiteDatabase) {
            supportSQLiteDatabase.execSQL("CREATE TABLE dbVersion ( versionNumber INTEGER PRIMARY KEY NOT NULL)");
            supportSQLiteDatabase.execSQL("ALTER TABLE 'visionData' ADD COLUMN 'ord' INTEGER NOT NULL DEFAULT 0");
            supportSQLiteDatabase.execSQL("UPDATE visionData set ord = rowid");
        }
    };
    private static AppDatabase instance;

    public abstract AffirmationDao affirmationDao();

    public abstract CategoryDAO categoryDAO();

    public abstract DbVersionDAO dbVersionDAO();

    public abstract DiaryDataDAO diaryDataDAO();

    public abstract FolderDao folderDao();

    public abstract LifePurposeDAO lifePurposeDao();

    public abstract VisionDAO visionDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, Constants.APP_DB_NAME).allowMainThreadQueries().addMigrations(MIGRATION_2_3).build();
            if (!AppPref.isDbVersionAdded(context.getApplicationContext())) {
                addDbVersion();
                AppPref.setIsDbversionAdded(context.getApplicationContext(), true);
            }
        }
        return instance;
    }

    private static void addDbVersion() {
        instance.dbVersionDAO().deleteAll();
        instance.dbVersionDAO().insert(new DbVersionModel(3));
    }
}
