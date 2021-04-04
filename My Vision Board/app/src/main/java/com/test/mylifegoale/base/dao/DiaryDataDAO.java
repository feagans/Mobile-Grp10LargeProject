package com.test.mylifegoale.base.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.test.mylifegoale.model.DiaryData;
import java.util.List;

@Dao
public interface DiaryDataDAO {
    @Delete
    int delete(DiaryData diaryData);

    @Query("Select * FROM diaryData ")
    List<DiaryData> getAll();

    @Insert
    long insert(DiaryData diaryData);

    @Update
    int update(DiaryData diaryData);
}
