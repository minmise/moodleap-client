package com.moodleap.client.ui.authorization;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.moodleap.client.R;

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
    }

}
