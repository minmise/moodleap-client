package com.moodleap.client.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.moodleap.client.MainActivity;
import com.moodleap.client.R;

public class UserInfoFragment extends Fragment {

    private TextView textLogin;
    private Button buttonLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textLogin = view.findViewById(R.id.textLogin);
        buttonLogout = view.findViewById(R.id.buttonLogout);
        String login = requireContext()
                .getSharedPreferences("auth", Context.MODE_PRIVATE)
                .getString("email", "unknown");
        textLogin.setText(login);
        buttonLogout.setOnClickListener(v -> {
            ((MainActivity)requireActivity()).logout();
        });
    }
}
