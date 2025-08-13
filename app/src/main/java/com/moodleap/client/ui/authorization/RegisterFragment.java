package com.moodleap.client.ui.authorization;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

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
    }

}
