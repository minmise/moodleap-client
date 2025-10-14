package com.moodleap.client.requests;

import android.util.Log;

import com.moodleap.client.MainActivity;
import com.moodleap.client.db.entity.Mood;
import com.moodleap.client.db.entity.MoodTag;
import com.moodleap.client.db.entity.Tag;
import com.moodleap.client.db.repository.MoodRepository;
import com.moodleap.client.db.repository.MoodTagRepository;
import com.moodleap.client.db.repository.TagRepository;
import com.moodleap.client.dto.MoodDto;
import com.moodleap.client.dto.TagDto;
import com.moodleap.client.requests.service.MoodService;
import com.moodleap.client.requests.service.TagService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Response;

public class SyncManager {

    private final TagRepository tagRepository;
    private final TagService tagService;
    private final MoodRepository moodRepository;
    private final MoodService moodService;
    private final MoodTagRepository moodTagRepository;

    public SyncManager(TagRepository tagRepository, TagService tagService,
                       MoodRepository moodRepository, MoodService moodService,
                       MoodTagRepository moodTagRepository) {
        this.tagRepository = tagRepository;
        this.tagService = tagService;
        this.moodRepository = moodRepository;
        this.moodService = moodService;
        this.moodTagRepository = moodTagRepository;
    }

    private void uploadUnsyncedMoods() throws IOException {
        List<Mood> unsyncedMoods = moodRepository.getUnsyncedMoodsByUserId(MainActivity.getUid(moodService.getContext()));
        for (Mood mood : unsyncedMoods) {
            List<MoodTag> moodTags = moodTagRepository.getTagsByMoodIdUnlive(mood.id);
            Log.d("SYNC_DEBUG", moodTags.toString());
            List<Tag> tags = moodTagRepository.getTagsByMoodIdUnlive(mood.id).stream()
                    .map(moodTag -> tagRepository.getTagById(moodTag.tagId)).collect(Collectors.toList());
            Log.d("SYNC_DEBUG", tags.toString());
            Response<MoodDto> response = moodService.createMood(MainActivity.getToken(moodService.getContext()),
                    new MoodDto(mood, tags)).execute();
            if (response.isSuccessful() && response.body() != null) {
                mood.isSynced = true;
                mood.serverId = response.body().getId();
                moodRepository.update(mood);
            }
        }
    }

    private void fetchMoodsFromServer() throws IOException {
        Response<List<MoodDto>> response = moodService.getMoods(MainActivity.getToken(moodService.getContext())).execute();
        if (response.isSuccessful() && response.body() != null) {
            for (MoodDto moodDto : response.body()) {
                Mood mood = new Mood();
                mood.emotion = moodDto.getEmotion();
                mood.timestamp = moodDto.getTimestamp();
                mood.userId = moodDto.getUserId();
                mood.serverId = moodDto.getId();
                List<TagDto> tags = moodDto.getTags();
                if (moodRepository.getMoodByServerId(mood.serverId) == null) {
                    moodRepository.insert(mood, id -> mood.id = id);
                    for (TagDto tagDto : tags) {
                        Long tagId = tagRepository.getTagByServerId(tagDto.getId()).id;
                        MoodTag moodTag = new MoodTag();
                        moodTag.moodId = mood.id;
                        moodTag.tagId = tagId;
                        moodTagRepository.insert(moodTag);
                    }
                }
            }
        }
    }

    private void fetchTagsFromServer() throws IOException {
        Response<List<TagDto>> response = tagService.getTags(MainActivity.getToken(tagService.getContext())).execute();
        if (response.isSuccessful() && response.body() != null) {
            for (TagDto tagDto : response.body()) {
                Log.d("SYNC_TAGS_1", tagDto.getTitle());
                Tag tag = new Tag();
                tag.title = tagDto.getTitle();
                tag.serverId = tagDto.getId();
                tagRepository.insertOrUpdate(tag);
                Log.d("SYNC_TAGS_2", tagRepository.getTagByServerId(tag.serverId).title);
            }
        }
    }

    public void syncAll() throws IOException {
        fetchTagsFromServer();
        uploadUnsyncedMoods();
        fetchMoodsFromServer();
    }

}
