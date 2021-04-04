package com.test.mylifegoale.base.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.test.mylifegoale.model.CategoryModel;
import java.util.List;

@Dao
public interface CategoryDAO {
    @Delete
    int delete(CategoryModel categoryModel);

    @Query("Select * FROM categoryData ")
    List<CategoryModel> getAll();

    @Insert
    long insert(CategoryModel categoryModel);

    @Update
    int update(CategoryModel categoryModel);
}
