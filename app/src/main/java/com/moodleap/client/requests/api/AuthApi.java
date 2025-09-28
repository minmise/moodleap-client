package com.moodleap.client.requests.api;

import com.moodleap.client.dto.AuthResponse;
import com.moodleap.client.dto.LoginRequest;
import com.moodleap.client.dto.RegisterRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {

    @POST("auth/register")
    Call<AuthResponse> register(@Body RegisterRequest request);

    @POST("auth/login")
    Call<AuthResponse> login(@Body LoginRequest request);
}
