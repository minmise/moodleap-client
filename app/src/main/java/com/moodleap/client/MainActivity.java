package com.moodleap.client;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.room.Room;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.moodleap.client.db.AppDatabase;
import com.moodleap.client.db.repository.MoodRepository;
import com.moodleap.client.db.repository.MoodTagRepository;
import com.moodleap.client.db.repository.TagRepository;
import com.moodleap.client.requests.SyncWorker;
import com.moodleap.client.requests.service.AuthService;
import com.moodleap.client.requests.service.MoodService;
import com.moodleap.client.requests.service.TagService;
import com.moodleap.client.ui.authorization.LoginFragment;
import com.moodleap.client.ui.authorization.RegisterFragment;
import com.moodleap.client.ui.main.MoodEntryFragment;
import com.moodleap.client.ui.main.StatisticsFragment;
import com.moodleap.client.ui.main.UserInfoFragment;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    /*

    private static AuthService authService;
    private static TagService tagService;
    private static MoodService moodService;

    private static AppDatabase db;
    private static MoodRepository moodRepository;
    private static TagRepository tagRepository;
    private static MoodTagRepository moodTagRepository;

    private static MoodEntryFragment moodEntryFragment;
    private static StatisticsFragment statisticsFragment;
    private static UserInfoFragment userInfoFragment;
    private static LoginFragment loginFragment;
    private static RegisterFragment registerFragment;

     */
    private FrameLayout authContainer;
    private LinearLayout mainContainer;

    private boolean isUserLoggedIn() {
        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
        return prefs.contains("jwt_token");
    }

    public void showAuthUi() {
        authContainer.setVisibility(View.VISIBLE);
        mainContainer.setVisibility(View.GONE);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.auth_container, AppFragmentManager.getLoginFragment())
                .commit();
    }

    public void showMainUi() {
        authContainer.setVisibility(View.GONE);
        mainContainer.setVisibility(View.VISIBLE);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, AppFragmentManager.getMoodEntryFragment())
                .commit();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_mood);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selected = null;
            if (item.getItemId() == R.id.nav_mood) {
                selected = AppFragmentManager.getMoodEntryFragment();
            } else if (item.getItemId() == R.id.nav_statistics) {
                selected = AppFragmentManager.getStatisticsFragment();
            } else if (item.getItemId() == R.id.nav_profile) {
                selected = AppFragmentManager.getUserInfoFragment();
            }
            if (selected != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, selected)
                        .commit();
            }
            return true;
        });
    }

    public void logout() {
        getSharedPreferences("auth", MODE_PRIVATE).edit().clear().apply();
        showAuthUi();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ServiceManager.init(this);
        AppFragmentManager.init();
        DatabaseManager.init(getApplicationContext());
        PeriodicWorkRequest periodicSync =
                new PeriodicWorkRequest.Builder(SyncWorker.class, 15, TimeUnit.MINUTES)
                        .setConstraints(
                                new Constraints.Builder()
                                        .setRequiredNetworkType(NetworkType.CONNECTED)
                                        .build())
                        .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "sync_worker",
                ExistingPeriodicWorkPolicy.KEEP,
                periodicSync
        );
        authContainer = findViewById(R.id.auth_container);
        mainContainer = findViewById(R.id.main_container);
        if (isUserLoggedIn()) {
            showMainUi();
        } else {
            showAuthUi();
        }
    }
}