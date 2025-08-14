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

public class MainActivity extends AppCompatActivity {

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
        String uid = getUid(this);
        if (uid != null) {
            Log.d("Auth", "UID найден: " + uid);
            Toast.makeText(this, "UID найден: " + uid, Toast.LENGTH_LONG).show();
        } else {
            Log.d("Auth", "UID не найден");
        }
    }
}