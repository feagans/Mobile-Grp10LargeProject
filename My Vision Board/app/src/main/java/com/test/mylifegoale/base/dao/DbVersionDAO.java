package com.test.mylifegoale.base.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.test.mylifegoale.model.DbVersionModel;

@Dao
public interface DbVersionDAO {
    @Delete
    int delete(DbVersionModel dbVersionModel);

    @Query("DELETE FROM dbVersion")
    int deleteAll();

    @Insert
    long insert(DbVersionModel dbVersionModel);

    @Update
    int update(DbVersionModel dbVersionModel);
}
