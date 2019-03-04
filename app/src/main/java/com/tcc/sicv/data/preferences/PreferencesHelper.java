package com.tcc.sicv.data.preferences;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.tcc.sicv.data.model.User;

public class PreferencesHelper {
    @SuppressWarnings("FieldCanBeLocal")
    private final String PREF_KEY = "preferences";
    private final String USER_FIELD = "user";
    private SharedPreferences sp;

    public PreferencesHelper(Application applicationContext) {
        sp = applicationContext.getSharedPreferences(PREF_KEY , Context.MODE_PRIVATE);
    }

    public void saveUser(User user){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USER_FIELD, new Gson().toJson(user));
        editor.apply();
    }

    public User getUser(){
        if(!sp.contains(USER_FIELD)) return null;
        String userJson = sp.getString(USER_FIELD, "");
        return new Gson().fromJson(userJson, User.class);
    }
}
