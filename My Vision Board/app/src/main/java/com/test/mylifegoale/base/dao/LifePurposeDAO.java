package com.test.mylifegoale.base.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.test.mylifegoale.model.LifePurposeModel;
import java.util.List;

@Dao
public interface LifePurposeDAO {
    @Delete
    int delete(LifePurposeModel lifePurposeModel);

    @Query("Select * FROM lifePurposeData ")
    List<LifePurposeModel> getAll();

    @Insert
    long insert(LifePurposeModel lifePurposeModel);

    @Update
    int update(LifePurposeModel lifePurposeModel);
}
