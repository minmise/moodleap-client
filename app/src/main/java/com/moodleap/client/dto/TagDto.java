package com.moodleap.client.dto;

import com.moodleap.client.db.entity.Tag;

public class TagDto {

    private Long id;
    private String title;

    public TagDto(Tag tag) {
        this.id = tag.serverId;
        this.title = tag.title;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
