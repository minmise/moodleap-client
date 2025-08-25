package com.moodleap.client.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.moodleap.client.db.entity.Mood;

import java.util.List;

@Dao
public interface MoodDao {
    @Insert
    void insert(Mood mood);

    @Query("SELECT * FROM moods WHERE userId = :userId")
    LiveData<List<Mood>> getMoodsByUserId(String userId);

    @Update
    void update(Mood mood);

    @Delete
    void delete(Mood mood);
}
