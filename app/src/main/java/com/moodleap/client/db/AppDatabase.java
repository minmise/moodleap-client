package com.moodleap.client.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.moodleap.client.db.dao.MoodDao;
import com.moodleap.client.db.dao.MoodTagDao;
import com.moodleap.client.db.dao.TagDao;
import com.moodleap.client.db.entity.Mood;
import com.moodleap.client.db.entity.MoodTag;
import com.moodleap.client.db.entity.Tag;

@Database(entities = {Mood.class, Tag.class, MoodTag.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract MoodDao moodDao();
    public abstract TagDao tagDao();
    public abstract MoodTagDao moodTagDao();
}
