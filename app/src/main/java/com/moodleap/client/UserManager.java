package com.moodleap.client;

import android.content.Context;

public class UserManager {

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

}
