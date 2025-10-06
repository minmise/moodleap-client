package com.moodleap.client.requests.service;

import android.content.Context;

import com.moodleap.client.dto.TagDto;
import com.moodleap.client.requests.RetrofitClient;
import com.moodleap.client.requests.api.TagApi;

import java.util.List;

import retrofit2.Call;

public class TagService {

    private final TagApi api;
    private final Context context;

    public TagService(Context context) {
        this.context = context.getApplicationContext();
        this.api = RetrofitClient.getInstance(context).create(TagApi.class);
    }

    public Call<List<TagDto>> getTags(String token) {
        return api.getTags(token);
    }

    public Context getContext() {
        return context;
    }
}
