package com.moodleap.client;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import com.moodleap.client.db.AppDatabase;
import com.moodleap.client.db.entity.Mood;
import com.moodleap.client.requests.AuthService;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static AuthService authService;
    private AppDatabase db;

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

    public static AuthService getAuthService() {
        return authService;
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
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "moodleap").build();
        Mood testMood = new Mood();
        testMood.id = 1L;
        testMood.emotion = 2L;
        testMood.timestamp = System.currentTimeMillis();
        new Thread(() -> {
            db.moodDao().insert(testMood);
            List<Mood> moods = db.moodDao().getMoods();
            for (Mood m : moods) {
                Log.d("DB_TEST", "Mood: " + m.id + ", " + m.emotion + ", " + m.timestamp);
            }
        }).start();
        Toast.makeText(this, "BASE_URL = " + Config.getBaseUrl(this), Toast.LENGTH_LONG).show();
    }
}