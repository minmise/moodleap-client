package com.moodleap.client.requests;

public class AuthRepository {

    public String login(String email, String password) {
        if (email.equals("test@test.com") && password.equals("1234")) {
            return "UID_123456";
        } else {
            return "UID_" + System.currentTimeMillis();
        }
    }

}
