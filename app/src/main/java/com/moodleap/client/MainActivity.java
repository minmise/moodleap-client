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

    private FrameLayout authContainer;
    private LinearLayout mainContainer;

    public static void saveUid(Context context, String uid) {
        context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                .edit()
                .putString("uid", uid)
                .apply();
    }

    public static String getUid(Context context) {
        return context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                .getString("uid", null);
    }

    public static void saveToken(Context context, String token) {
        context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                .edit()
                .putString("jwt_token", token)
                .apply();
    }

    public static String getToken(Context context) {
        return context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                .getString("jwt_token", null);
    }

    public static void saveEmail(Context context, String email) {
        context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                .edit()
                .putString("email", email)
                .apply();
    }

    public static String getEmail(Context context) {
        return context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                .getString("email", null);
    }

    public static AuthService getAuthService() {
        return authService;
    }

    public static TagService getTagService() {
        return tagService;
    }

    public static MoodService getMoodService() {
        return moodService;
    }

    public static LoginFragment getLoginFragment() {
        return loginFragment;
    }

    public static RegisterFragment getRegisterFragment() {
        return registerFragment;
    }

    public static MoodEntryFragment getMoodEntryFragment() {
        return moodEntryFragment;
    }

    public static StatisticsFragment getStatisticsFragment() {
        return statisticsFragment;
    }

    public static UserInfoFragment getUserInfoFragment() {
        return userInfoFragment;
    }

    public static AppDatabase getDb() {
        return db;
    }

    public static MoodRepository getMoodRepository() {
        return moodRepository;
    }

    public static TagRepository getTagRepository() {
        return tagRepository;
    }

    public static MoodTagRepository getMoodTagRepository() {
        return moodTagRepository;
    }

    private boolean isUserLoggedIn() {
        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
        return prefs.contains("jwt_token");
    }

    public void showAuthUi() {
        authContainer.setVisibility(View.VISIBLE);
        mainContainer.setVisibility(View.GONE);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.auth_container, getLoginFragment())
                .commit();
    }

    public void showMainUi() {
        authContainer.setVisibility(View.GONE);
        mainContainer.setVisibility(View.VISIBLE);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, getMoodEntryFragment())
                .commit();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_mood);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selected = null;
            if (item.getItemId() == R.id.nav_mood) {
                selected = getMoodEntryFragment();
            } else if (item.getItemId() == R.id.nav_statistics) {
                selected = getStatisticsFragment();
            } else if (item.getItemId() == R.id.nav_profile) {
                selected = getUserInfoFragment();
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
        if (authService == null) {
            authService = new AuthService(this);
        }
        if (tagService == null) {
            tagService = new TagService(this);
        }
        if (moodService == null) {
            moodService = new MoodService(this);
        }
        if (loginFragment == null) {
            loginFragment = new LoginFragment();
        }
        if (registerFragment == null) {
            registerFragment = new RegisterFragment();
        }
        if (moodEntryFragment == null) {
            moodEntryFragment = new MoodEntryFragment();
        }
        if (statisticsFragment == null) {
            statisticsFragment = new StatisticsFragment();
        }
        if (userInfoFragment == null) {
            userInfoFragment = new UserInfoFragment();
        }
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "moodleap").build();
        moodRepository = new MoodRepository(db);
        tagRepository = new TagRepository(db);
        moodTagRepository = new MoodTagRepository(db);
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