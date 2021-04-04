package com.test.mylifegoale.base.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.test.mylifegoale.model.FolderRowModel;
import java.util.List;

@Dao
public interface FolderDao {
    @Delete
    int delete(FolderRowModel folderRowModel);

    @Query("select f.*,count(a.id) as counts from folderList f left join affirmationList a on a.folderId = f.id where a.isActive = 1 group by f.name order by f.sequence ")
    List<FolderRowModel> getActiveListWithCount();

    @Query("Select * FROM folderList order by name")
    List<FolderRowModel> getAll();

    @Query("select f.*,count(a.id) as counts from folderList f left join affirmationList a on a.folderId = f.id group by f.name order by f.sequence ")
    List<FolderRowModel> getAllListWithCount();

    @Query("select min(sequence),* from folderList")
    FolderRowModel getDefault();

    @Query("Select * FROM folderList WHERE id=:str")
    FolderRowModel getDetail(String str);

    @Query("Select COUNT(*) FROM folderList ")
    int getFolderListCount();

    @Insert
    long insert(FolderRowModel folderRowModel);

    @Update
    int update(FolderRowModel folderRowModel);

    @Query("UPDATE folderList SET sequence =:i WHERE id =:str;")
    int updateSequence(String str, int i);
}
