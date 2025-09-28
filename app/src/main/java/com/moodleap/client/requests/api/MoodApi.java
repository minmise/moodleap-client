package com.moodleap.client.requests.api;

import com.moodleap.client.dto.MoodDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface MoodApi {

    @GET("api/moods")
    Call<List<MoodDto>> getMoods(@Header("Authorization") String token);

    @POST("api/moods")
    Call<MoodDto> createMood(@Header("Authorization") String token, @Body MoodDto mood);

}
