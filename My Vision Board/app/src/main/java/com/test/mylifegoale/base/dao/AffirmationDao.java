package com.test.mylifegoale.base.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.test.mylifegoale.model.AffirmationRowModel;
import java.util.List;

@Dao
public interface AffirmationDao {
    @Delete
    int delete(AffirmationRowModel affirmationRowModel);

    @Query("DELETE FROM affirmationList WHERE folderId =:str")
    int deleteFolder(String str);

    @Query("Select * FROM affirmationList WHERE isActive = 1 order by sequence")
    List<AffirmationRowModel> getAllActiveList();

    @Query("Select COUNT(*) FROM affirmationList ")
    int getAllCount();

    @Query("Select * FROM affirmationList order by sequence")
    List<AffirmationRowModel> getAllList();

    @Query("SELECT * FROM affirmationList WHERE id =:str ")
    AffirmationRowModel getDetail(String str);

    @Query("Select * FROM affirmationList WHERE folderId =:str order by sequenceFolder")
    List<AffirmationRowModel> getFolderList(String str);

    @Query("Select COUNT(*) FROM affirmationList WHERE  isActive = 1 ")
    int getFolderListActiveCount();

    @Query("Select COUNT(*) FROM affirmationList WHERE folderId =:str ")
    int getFolderListCount(String str);

    @Query("SELECT id FROM affirmationList ORDER BY random() limit 1")
    String getRandomId();

    @Insert
    long insert(AffirmationRowModel affirmationRowModel);

    @Update
    int update(AffirmationRowModel affirmationRowModel);

    @Query("UPDATE affirmationList SET sequence =:i WHERE id =:str;")
    int updateSequence(String str, int i);

    @Query("UPDATE affirmationList SET sequenceFolder =:i WHERE id =:str;")
    int updateSequenceFolder(String str, int i);
}
