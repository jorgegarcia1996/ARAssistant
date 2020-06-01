package com.iescampanillas.arassistant.utils;

import com.google.firebase.auth.FirebaseAuth;

public class UserUtils {

    private static UserUtils userUtilsInstance = null;
    private static FirebaseAuth fbAuth;

    private UserUtils() {
        fbAuth = FirebaseAuth.getInstance();
    }

    public static UserUtils getInstance() {
        if(userUtilsInstance == null) {
            userUtilsInstance = new UserUtils();
        }
        return userUtilsInstance;
    }

    public static String getCurrentUserUid() {
        return fbAuth.getCurrentUser().getUid();
    }
}
