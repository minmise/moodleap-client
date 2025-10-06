package com.moodleap.client.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.moodleap.client.db.entity.Tag;

import java.util.List;

@Dao
public interface TagDao {
    @Insert
    void insert(Tag tag);

    @Update
    void update(Tag tag);

    @Query("SELECT * FROM tags")
    LiveData<List<Tag>> getTags();

    @Query("SELECT * FROM tags WHERE serverId = :serverId LIMIT 1")
    Tag getTagByServerId(Long serverId);

    @Query("SELECT * FROM tags WHERE id = :id LIMIT 1")
    Tag getTagById(Long id);
}
