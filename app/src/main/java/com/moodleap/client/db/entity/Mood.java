package com.moodleap.client.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "moods")
public class Mood {

    @PrimaryKey(autoGenerate = true)
    public Long id;
    public String uid;
    public Long emotion;
    public Long timestamp;

}
