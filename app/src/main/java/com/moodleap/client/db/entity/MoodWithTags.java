package com.moodleap.client.db.entity;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class MoodWithTags {
    @Embedded
    public Mood mood;

    @Relation(
            parentColumn = "id",
            entityColumn = "id",
            associateBy = @Junction(
                    value = MoodTag.class,
                    parentColumn = "moodId",
                    entityColumn = "tagId"
            )
    )
    public List<Tag> tags;
}
