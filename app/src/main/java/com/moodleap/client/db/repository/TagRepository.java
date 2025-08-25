package com.moodleap.client.db.repository;

import androidx.lifecycle.LiveData;

import com.moodleap.client.MainActivity;
import com.moodleap.client.db.AppDatabase;
import com.moodleap.client.db.dao.TagDao;
import com.moodleap.client.db.entity.Tag;

import java.util.List;
import java.util.concurrent.Executors;

public class TagRepository {

    private final TagDao tagDao;

    public TagRepository(AppDatabase appDatabase) {
        tagDao = appDatabase.tagDao();
    }

    public void insert(Tag tag) {
        Executors.newSingleThreadExecutor().execute(() -> tagDao.insert(tag));
    }

    public LiveData<List<Tag>> getTags() {
        return tagDao.getTags();
    }

}
