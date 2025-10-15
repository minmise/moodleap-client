package com.moodleap.client;

import android.content.Context;

import androidx.room.Room;

import com.moodleap.client.db.AppDatabase;
import com.moodleap.client.db.repository.MoodRepository;
import com.moodleap.client.db.repository.MoodTagRepository;
import com.moodleap.client.db.repository.TagRepository;

public class DatabaseManager {

    private static AppDatabase db;
    private static MoodRepository moodRepository;
    private static TagRepository tagRepository;
    private static MoodTagRepository moodTagRepository;

    public static void init(Context context) {
        db = Room.databaseBuilder(context, AppDatabase.class, "moodleap").build();
        moodRepository = new MoodRepository(db);
        tagRepository = new TagRepository(db);
        moodTagRepository = new MoodTagRepository(db);
    }

    public static AppDatabase getDb() {
        return db;
    }

    public static MoodRepository getMoodRepository() {
        return moodRepository;
    }

    public static TagRepository getTagRepository() {
        return tagRepository;
    }

    public static MoodTagRepository getMoodTagRepository() {
        return moodTagRepository;
    }

}
