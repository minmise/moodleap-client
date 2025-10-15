package com.moodleap.client.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.moodleap.client.db.entity.Tag;
import com.moodleap.client.db.entity.TagWithUsage;

import java.util.List;

@Dao
public interface TagDao {
    @Insert
    void insert(Tag tag);

    @Update
    void update(Tag tag);

    @Query("SELECT * FROM tags")
    LiveData<List<Tag>> getTags();

    @Query("SELECT t.*, COUNT(mt.tagId) AS usageCount FROM tags t LEFT JOIN mood_tags mt ON t.id = mt.tagId LEFT JOIN moods m ON mt.moodId = m.id WHERE m.userId = :userId GROUP BY t.id ORDER BY usageCount DESC")
    List<TagWithUsage> getTagsWithUsageCount(String userId);

    @Query("SELECT * FROM tags WHERE serverId = :serverId LIMIT 1")
    Tag getTagByServerId(Long serverId);

    @Query("SELECT * FROM tags WHERE id = :id LIMIT 1")
    Tag getTagById(Long id);
}
