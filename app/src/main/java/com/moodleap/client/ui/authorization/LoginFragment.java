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
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.moodleap.client.MainActivity;
import com.moodleap.client.R;
import com.moodleap.client.db.Converters;
import com.moodleap.client.db.entity.Mood;
import com.moodleap.client.db.entity.Tag;
import com.moodleap.client.dto.MoodDto;
import com.moodleap.client.dto.TagDto;
import com.moodleap.client.requests.SyncWorker;

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
                WorkRequest syncRequest = new OneTimeWorkRequest.Builder(SyncWorker.class).build();
                WorkManager.getInstance(requireContext()).enqueue(syncRequest);
            }
        });
    }

}
