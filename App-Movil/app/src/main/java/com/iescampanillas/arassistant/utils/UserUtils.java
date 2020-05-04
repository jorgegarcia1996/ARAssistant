package com.iescampanillas.arassistant.utils;

import com.google.firebase.auth.FirebaseAuth;

public class UserUtils {
    private FirebaseAuth fbAuth;

    public UserUtils() {

    }

    public String getCurrentUserUid() {
        fbAuth = FirebaseAuth.getInstance();
        return fbAuth.getCurrentUser().getUid();
    }
}
