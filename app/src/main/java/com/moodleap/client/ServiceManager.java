package com.moodleap.client;

import android.content.Context;

import com.moodleap.client.requests.service.AuthService;
import com.moodleap.client.requests.service.MoodService;
import com.moodleap.client.requests.service.TagService;

public class ServiceManager {

    private static AuthService authService;
    private static TagService tagService;
    private static MoodService moodService;

    public static void init(Context context) {
        if (authService == null) {
            authService = new AuthService(context);
        }
        if (tagService == null) {
            tagService = new TagService(context);
        }
        if (moodService == null) {
            moodService = new MoodService(context);
        }
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

}
