package com.moodleap.client.db.entity;

import androidx.room.Embedded;

public class TagWithUsage {
    @Embedded
    public Tag tag;

    public Long usageCount;
}
