package com.moodleap.client.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.moodleap.client.db.entity.MoodTag;

import java.util.List;

@Dao
public interface MoodTagDao {
    @Insert
    void insert(MoodTag moodTag);

    @Query("SELECT * FROM mood_tags WHERE moodId = :moodId")
    LiveData<List<MoodTag>> getTagsByMood(Long moodId);

    @Query("SELECT * FROM mood_tags WHERE moodId = :moodId")
    List<MoodTag> getTagsByMoodUnlive(Long moodId);
}
