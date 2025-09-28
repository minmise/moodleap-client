package com.moodleap.client.dto;

import com.moodleap.client.db.entity.Mood;
import com.moodleap.client.db.entity.Tag;

import java.util.List;
import java.util.stream.Collectors;

public class MoodDto {

    private Long id;
    private String userId;
    private Long emotion;
    private Long timestamp;
    private List<TagDto> tags;

    public MoodDto(Mood mood, List<Tag> tags) {
        this.id = mood.serverId;
        this.userId = mood.userId;
        this.emotion = mood.emotion;
        this.timestamp = mood.timestamp;
        this.tags = tags.stream().map(TagDto::new).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public Long getEmotion() {
        return emotion;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public List<TagDto> getTags() {
        return tags;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmotion(Long emotion) {
        this.emotion = emotion;
    }

    public void setTags(List<TagDto> tags) {
        this.tags = tags;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
