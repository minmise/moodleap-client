package com.moodleap.client.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.moodleap.client.db.entity.Mood;
import com.moodleap.client.db.entity.MoodWithTags;

import java.util.List;

@Dao
public interface MoodDao {
    @Insert
    Long insert(Mood mood);

    @Query("SELECT * FROM moods WHERE userId = :userId")
    LiveData<List<Mood>> getMoodsByUserId(String userId);

    @Query("SELECT * FROM moods WHERE userId = :userId AND isSynced = 0")
    List<Mood> getUnsyncedMoodsByUserId(String userId);

    @Query("SELECT * FROM moods WHERE serverId = :serverId LIMIT 1")
    Mood getMoodByServerId(Long serverId);

    @Update
    void update(Mood mood);

    @Delete
    void delete(Mood mood);

    @Transaction
    @Query("SELECT * FROM moods WHERE userId = :userId ORDER BY timestamp DESC")
    LiveData<List<MoodWithTags>> getMoodsWithTags(String userId);
}
