package com.moodleap.client.ui.main;

public class MoodConverter {

    public static Long stringToLong(String value) {
        switch (value) {
            case "Awful":
                return -2L;
            case "Bad":
                return -1L;
            case "Normal":
                return 0L;
            case "Good":
                return 1L;
            default:
                return 2L;
        }
    }

    public static String longToString(Long value) {
        if (value == -2L) {
            return "Awful";
        }
        if (value == -1L) {
            return "Bad";
        }
        if (value == 0L) {
            return "Normal";
        }
        if (value == 1L) {
            return "Good";
        }
        return "Great";
    }

}
