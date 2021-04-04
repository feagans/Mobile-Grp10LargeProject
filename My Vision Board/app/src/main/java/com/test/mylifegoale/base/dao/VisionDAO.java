package com.test.mylifegoale.base.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.test.mylifegoale.model.VisionModel;
import java.util.List;

@Dao
public interface VisionDAO {
    @Delete
    int delete(VisionModel visionModel);

    @Query("Select * FROM visionData order by ord")
    List<VisionModel> getAll();

    @Query("Select * FROM visionData where isPending=:z")
    List<VisionModel> getAllStatus(boolean z);

    @Query("Select visionData.*,categoryData.title FROM visionData inner join categoryData on categoryData.id = visionData.category where isPending=:z")
    List<VisionModel> getAllStatusWithCat(boolean z);

    @Query("Select ifnull(max(ord),0) + 1  FROM visionData ")
    int getMaxOrd();

    @Insert
    long insert(VisionModel visionModel);

    @Update
    int update(VisionModel visionModel);

    @Query("UPDATE visionData SET ord =:i WHERE id =:str;")
    int updateVisionOrd(String str, int i);
}
