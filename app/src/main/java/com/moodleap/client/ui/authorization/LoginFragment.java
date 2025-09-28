package com.moodleap.client.ui.authorization;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.moodleap.client.MainActivity;
import com.moodleap.client.R;
import com.moodleap.client.db.Converters;
import com.moodleap.client.db.entity.Mood;
import com.moodleap.client.db.entity.Tag;
import com.moodleap.client.dto.MoodDto;
import com.moodleap.client.dto.TagDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    public LoginFragment() {
        super(R.layout.fragment_login);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnRegister = view.findViewById(R.id.btnGoToRegister);
        btnRegister.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.auth_container, new RegisterFragment())
                    .commit();
        });

        Button loginButton = view.findViewById(R.id.btnLogin);
        EditText emailField = view.findViewById(R.id.loginEmail);
        EditText passwordField = view.findViewById(R.id.loginPassword);
        loginButton.setOnClickListener(v -> {
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();
            MainActivity.getAuthService().login(email, password);
            if (MainActivity.getToken(requireContext()) != null) {
                ((MainActivity)requireActivity()).showMainUi();
                MainActivity.getTagService().getTags(MainActivity.getToken(requireContext())).enqueue(new Callback<List<TagDto>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<TagDto>> call, @NonNull Response<List<TagDto>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(requireContext(), response.body().stream().map(TagDto::getTitle).collect(Collectors.toList()).toString(), Toast.LENGTH_LONG).show();
                            Tag tag = new Tag();
                            tag.setId(0L);
                            tag.setTitle(response.body().stream().map(TagDto::getTitle).collect(Collectors.toList()).get(0));
                            tag.setServerId(response.body().stream().map(TagDto::getId).collect(Collectors.toList()).get(0));
                            Mood mood = new Mood();
                            mood.setId(0L);
                            mood.setEmotion(2L);
                            mood.setServerId(null);
                            mood.setUserId(MainActivity.getUid(requireContext()));
                            mood.setTimestamp(Converters.fromLocalDateTime(LocalDateTime.now()));
                            MainActivity.getMoodService().createMood(MainActivity.getToken(requireContext()),
                                    new MoodDto(mood, List.of(tag))).enqueue(new Callback<MoodDto>() {
                                @Override
                                public void onResponse(@NonNull Call<MoodDto> call, @NonNull Response<MoodDto> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        Toast.makeText(requireContext(), response.body().getEmotion().toString(), Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(requireContext(), "Error: " + response.code(), Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<MoodDto> call, @NonNull Throwable throwable) {
                                    Toast.makeText(requireContext(), "Network Error!", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            Toast.makeText(requireContext(), "Error: " + response.code(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<TagDto>> call, @NonNull Throwable throwable) {
                        Toast.makeText(requireContext(), "Network Error!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

}
