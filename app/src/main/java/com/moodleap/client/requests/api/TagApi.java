package com.moodleap.client.requests.api;

import com.moodleap.client.dto.TagDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface TagApi {

    @GET("api/tags")
    Call<List<TagDto>> getTags(@Header("Authorization") String token);

}
