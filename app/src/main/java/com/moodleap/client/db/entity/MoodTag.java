package com.moodleap.client.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "mood_tags")
public class MoodTag {

    @PrimaryKey(autoGenerate = true)
    public Long id;
    public Long serverId;
    public Long moodId;
    public Long tagId;

}
