package com.moodleap.client.requests.service;

import android.content.Context;

import com.moodleap.client.dto.MoodDto;
import com.moodleap.client.requests.RetrofitClient;
import com.moodleap.client.requests.api.MoodApi;

import java.util.List;

import retrofit2.Call;

public class MoodService {

    private final MoodApi api;
    private final Context context;

    public MoodService(Context context) {
        this.context = context.getApplicationContext();
        this.api = RetrofitClient.getInstance(context).create(MoodApi.class);
    }

    public Call<List<MoodDto>> getMoods(String token) {
        return api.getMoods(token);
    }

    public Call<MoodDto> createMood(String token, MoodDto moodDto) {
        return api.createMood(token, moodDto);
    }

}
