package com.moodleap.client;

import com.moodleap.client.ui.authorization.LoginFragment;
import com.moodleap.client.ui.authorization.RegisterFragment;
import com.moodleap.client.ui.main.MoodEntryFragment;
import com.moodleap.client.ui.main.StatisticsFragment;
import com.moodleap.client.ui.main.UserInfoFragment;

public class AppFragmentManager {

    private static MoodEntryFragment moodEntryFragment;
    private static StatisticsFragment statisticsFragment;
    private static UserInfoFragment userInfoFragment;
    private static LoginFragment loginFragment;
    private static RegisterFragment registerFragment;

    public static void init() {
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

}
