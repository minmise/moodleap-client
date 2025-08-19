package com.moodleap.client.ui.authorization;

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

public class RegisterFragment extends Fragment {

    public RegisterFragment() {
        super(R.layout.fragment_register);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnLogin = view.findViewById(R.id.btnGoToLogin);
        btnLogin.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_register_to_login));
        Button registerButton = view.findViewById(R.id.btnRegister);
        EditText emailField = view.findViewById(R.id.registerEmail);
        EditText passwordField = view.findViewById(R.id.registerPassword);
        EditText passwordConfirmationField = view.findViewById(R.id.registerConfirmPassword);
        registerButton.setOnClickListener(v -> {
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();
            String passwordConfirmation = passwordConfirmationField.getText().toString();
            if (!password.equals(passwordConfirmation)) {
                Toast.makeText(getContext(), "Passwords are different", Toast.LENGTH_LONG).show();
                return;
            }
            MainActivity.getAuthService().register(email, password);
        });
    }

}
