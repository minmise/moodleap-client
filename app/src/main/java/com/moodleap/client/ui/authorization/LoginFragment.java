package com.moodleap.client.ui.authorization;

import static com.moodleap.client.MainActivity.saveUid;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.moodleap.client.R;
import com.moodleap.client.requests.AuthRepository;

public class LoginFragment extends Fragment {

    public LoginFragment() {
        super(R.layout.fragment_login);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnRegister = view.findViewById(R.id.btnGoToRegister);
        btnRegister.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_login_to_register));

        Button loginButton = view.findViewById(R.id.btnLogin);
        EditText emailField = view.findViewById(R.id.loginEmail);
        EditText passwordField = view.findViewById(R.id.loginPassword);
        loginButton.setOnClickListener(v -> {
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();
            AuthRepository repo = new AuthRepository();
            String uid = repo.login(email, password);
            saveUid(requireContext(), uid);
        });
    }

}
