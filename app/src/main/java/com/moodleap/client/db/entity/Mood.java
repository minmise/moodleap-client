package com.moodleap.client.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "moods")
public class Mood {

    @PrimaryKey(autoGenerate = true)
    public Long id;
    public Long serverId;
    public String userId;
    public Long emotion;
    public Long timestamp;
    public boolean isSynced = false;

    public void setId(Long id) {
        this.id = id;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setEmotion(Long emotion) {
        this.emotion = emotion;
    }
}
