package com.socialmedianetworking.iremember.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.socialmedianetworking.iremember.Login;

import java.util.HashMap;


public class UserSession {
    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context context;
    // Shared pref mode
    int PRIVATE_MODE = 0;
    // Sharedpref file name
    private static final String PREF_NAME = "UserSessionPref";
    // First time logic Check
    public static final String TOKEN = "Tokennn";
    public static final String KEY_USER_ID = "user_id";
    public  static final String USER_TYPE = "user_type";
    public static  final  String KEY_P_IMAGE = "p_image";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";
    // Mobile number (make variable public to access from outside)
    public static final String KEY_MOBiLE = "mobile";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_CATEGORY = "category";
    // check first time app launch
    public static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    // Constructor
    public UserSession(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */

    public void createLoginSession(String user_id,String name,String user_type,String email,String mobile,String p_image)
    {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        // Storing name in pref
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_USER_ID, user_id);
        editor.putString(USER_TYPE,user_type);

        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_MOBiLE, mobile);
        editor.putString(KEY_P_IMAGE,p_image);

        editor.commit();
    }


    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(context, Login.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            context.startActivity(i);
        }

    }



    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user phone number
        user.put(KEY_MOBiLE, pref.getString(KEY_MOBiLE, null));
        user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, null));
        user.put(USER_TYPE, pref.getString(USER_TYPE, null));

        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_P_IMAGE, pref.getString(KEY_P_IMAGE, null));



        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.putBoolean(IS_LOGIN,false);
        editor.remove(KEY_NAME); // will delete key name
        editor.remove(USER_TYPE);
        editor.remove(KEY_USER_ID);
        editor.remove(KEY_MOBiLE);

        editor.clear();
        editor.commit();
        editor.apply();

        // After logout redirect user to Login Activity
        Intent i = new Intent(context, Login.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }


    public Boolean getToken() {
        return pref.getBoolean(TOKEN, true);
    }


    public void setToken(Boolean n){
        editor.putBoolean(TOKEN,n);
        editor.commit();
    }


    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }


    public void setCategory(String arrayList)
    {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        // Storing name in pref
        editor.putString(KEY_CATEGORY, arrayList);
        editor.commit();
    }

    public String getCategory() {
        return pref.getString(KEY_CATEGORY,"");

    }






}