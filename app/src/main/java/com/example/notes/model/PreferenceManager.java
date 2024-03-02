package com.example.notes.model;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    private static final String PREF_NAME = "MyAppPreferences";
    private static final String KEY_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_NAME = "userName";

    private static final String KEY_PROFILE_PIC = "profilePic";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public PreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_LOGGED_IN, false);
    }

    public void setLoggedInUserdetails(boolean loggedIn, String userEmail, String userName, String profilePic) {
        editor.putBoolean(KEY_LOGGED_IN, loggedIn);
        editor.putString(KEY_USER_EMAIL, userEmail);
        editor.putString(KEY_USER_NAME, userName);
        editor.putString(KEY_PROFILE_PIC, profilePic);
        editor.apply();
    }

    public String getUserEmail() {
        return sharedPreferences.getString(KEY_USER_EMAIL, "");
    }


    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, "");
    }


    public String getProfilePicUri() {
        return sharedPreferences.getString(KEY_PROFILE_PIC, "");
    }



    // Method to reset all user details
    public void resetUserDetails() {
        editor.remove(KEY_LOGGED_IN);
        editor.remove(KEY_USER_EMAIL);
        editor.remove(KEY_USER_NAME);
        editor.remove(KEY_PROFILE_PIC);
        editor.apply();
    }
}
