package com.moodleap.client.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tags")
public class Tag {

    @PrimaryKey(autoGenerate = true)
    public Long id;
    public Long serverId;
    public String title;

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

}
