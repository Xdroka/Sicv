package com.tcc.sicv.data.preferences;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import static com.tcc.sicv.utils.Constants.PREF_KEY;
import static com.tcc.sicv.utils.Constants.USER_FIELD;

public class PreferencesHelper {
    private SharedPreferences sp;

    public PreferencesHelper(Application applicationContext) {
        sp = applicationContext.getSharedPreferences(PREF_KEY , Context.MODE_PRIVATE);
    }

    public void saveUser(String userEmail){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USER_FIELD, userEmail);
        editor.apply();
    }

    public String getEmail(){
        if(!sp.contains(USER_FIELD)) return null;
        return sp.getString(USER_FIELD, "");
    }

    public void logout(){
        if(!sp.contains(USER_FIELD)) return;
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(USER_FIELD);
        editor.apply();

    }
}
