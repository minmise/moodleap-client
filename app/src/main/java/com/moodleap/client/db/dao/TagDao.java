package com.moodleap.client.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.moodleap.client.db.entity.Tag;

import java.util.List;

@Dao
public interface TagDao {
    @Insert
    void insert(Tag tag);

    @Query("SELECT * FROM tags")
    List<Tag> getTags();
}
