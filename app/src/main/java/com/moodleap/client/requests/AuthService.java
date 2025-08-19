package com.moodleap.client.requests;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.moodleap.client.MainActivity;
import com.moodleap.client.dto.AuthResponse;
import com.moodleap.client.dto.LoginRequest;
import com.moodleap.client.dto.RegisterRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthService {

    private final AuthApi api;
    private final Context context;

    public AuthService(Context context) {
        this.context = context.getApplicationContext();
        this.api = RetrofitClient.getInstance(context).create(AuthApi.class);
    }

    public void register(String email, String password) {
        Call<AuthResponse> call = api.register(new RegisterRequest(email, password));
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(@NonNull Call<AuthResponse> call, @NonNull Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(context, "User registrated!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Error: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthResponse> call, @NonNull Throwable t) {
                Toast.makeText(context, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void login(String email, String password) {
        Call<AuthResponse> call = api.login(new LoginRequest(email, password));
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(@NonNull Call<AuthResponse> call, @NonNull Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    //Toast.makeText(context, "jwt: " + response.body().getToken(), Toast.LENGTH_LONG).show();
                    MainActivity.saveToken(context, response.body().getToken());
                    MainActivity.saveUid(context, response.body().getUid());
                } else {
                    Toast.makeText(context, "Error: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthResponse> call, @NonNull Throwable t) {
                Toast.makeText(context, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
