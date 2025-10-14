package com.moodleap.client.requests;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.moodleap.client.MainActivity;
import com.moodleap.client.db.AppDatabase;
import com.moodleap.client.db.repository.MoodRepository;
import com.moodleap.client.db.repository.MoodTagRepository;
import com.moodleap.client.db.repository.TagRepository;
import com.moodleap.client.requests.service.MoodService;
import com.moodleap.client.requests.service.TagService;

public class SyncWorker extends Worker {

    private final SyncManager syncManager;

    public SyncWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        AppDatabase db = MainActivity.getDb();
        MoodService moodService = new MoodService(context);
        TagService tagService = new TagService(context);
        MoodRepository moodRepository = new MoodRepository(db);
        TagRepository tagRepository = new TagRepository(db);
        MoodTagRepository moodTagRepository = new MoodTagRepository(db);
        syncManager = new SyncManager(tagRepository, tagService, moodRepository, moodService, moodTagRepository);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            syncManager.syncAll();
            Log.d("LOGIN_SYNC", "correct");
            return Result.success();
        } catch (Exception e) {
            Log.d("LOGIN_SYNC", e.toString());
            return Result.retry();
        }
    }

}
