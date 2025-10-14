package com.moodleap.client.db.repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.moodleap.client.MainActivity;
import com.moodleap.client.db.AppDatabase;
import com.moodleap.client.db.dao.MoodDao;
import com.moodleap.client.db.entity.Mood;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class MoodRepository {
    private final MoodDao moodDao;

    public MoodRepository(AppDatabase appDatabase) {
        moodDao = appDatabase.moodDao();
    }

    public void insert(Mood mood, Consumer<Long> callback) {
        Executors.newSingleThreadExecutor().execute(() -> {
            Long id = moodDao.insert(mood);
            if (id == null) {
                Log.d("MOOD_REPO", "NULL");
            } else {
                Log.d("MOOD_REPO", id.toString());
            }
            new Handler(Looper.getMainLooper()).post(() -> callback.accept(id));
        });
    }

    public void update(Mood mood) {
        Executors.newSingleThreadExecutor().execute(() -> moodDao.update(mood));
    }

    public void delete(Mood mood) {
        Executors.newSingleThreadExecutor().execute(() -> moodDao.delete(mood));
    }

    public LiveData<List<Mood>> getMoodsByUserId(String userId) {
        return moodDao.getMoodsByUserId(userId);
    }

    public List<Mood> getUnsyncedMoodsByUserId(String userId) {
        return moodDao.getUnsyncedMoodsByUserId(userId);
    }

    public Mood getMoodByServerId(Long serverId) {
        return moodDao.getMoodByServerId(serverId);
    }

}
