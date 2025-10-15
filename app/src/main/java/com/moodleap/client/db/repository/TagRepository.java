package com.moodleap.client.db.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.moodleap.client.MainActivity;
import com.moodleap.client.db.AppDatabase;
import com.moodleap.client.db.dao.TagDao;
import com.moodleap.client.db.entity.Tag;
import com.moodleap.client.db.entity.TagWithUsage;

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

    public void update(Tag tag) {
        Executors.newSingleThreadExecutor().execute(() -> tagDao.update(tag));
    }

    public void insertOrUpdate(Tag tag) {
        if (tagDao.getTagByServerId(tag.serverId) == null) {
            Log.d("SYNC_TAGS_OP", "insert");
            insert(tag);
        } else {
            Log.d("SYNC_TAGS_OP", "update");
            tag.id = tagDao.getTagByServerId(tag.serverId).id;
            update(tag);
        }
    }

    public LiveData<List<Tag>> getTags() {
        return tagDao.getTags();
    }

    public LiveData<List<TagWithUsage>> getTagsWithUsageCount(String userId) {
        MutableLiveData<List<TagWithUsage>> data = new MutableLiveData<>();
        Executors.newSingleThreadExecutor().execute(() -> {
            List<TagWithUsage> tags = tagDao.getTagsWithUsageCount(userId);
            data.postValue(tags);
        });
        return data;
    }

    public Tag getTagByServerId(Long serverId) {
        return tagDao.getTagByServerId(serverId);
    }

    public Tag getTagById(Long id) {
        return tagDao.getTagById(id);
    }

}
