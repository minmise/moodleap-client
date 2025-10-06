package com.moodleap.client.db.repository;

import androidx.lifecycle.LiveData;

import com.moodleap.client.MainActivity;
import com.moodleap.client.db.AppDatabase;
import com.moodleap.client.db.dao.MoodTagDao;
import com.moodleap.client.db.entity.MoodTag;

import java.util.List;
import java.util.concurrent.Executors;

public class MoodTagRepository {

    private final MoodTagDao moodTagDao;

    public MoodTagRepository(AppDatabase appDatabase) {
        moodTagDao = appDatabase.moodTagDao();
    }

    public void insert(MoodTag moodTag) {
        Executors.newSingleThreadExecutor().execute(() -> moodTagDao.insert(moodTag));
    }

    public LiveData<List<MoodTag>> getTagsByMoodId(Long moodId) {
        return moodTagDao.getTagsByMood(moodId);
    }

    public List<MoodTag> getTagsByMoodIdUnlive(Long moodId) {
        return moodTagDao.getTagsByMoodUnlive(moodId);
    }

}
