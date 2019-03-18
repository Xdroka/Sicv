package com.tcc.sicv.data.preferences

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

import com.tcc.sicv.utils.Constants.PREF_KEY
import com.tcc.sicv.utils.Constants.USER_FIELD

class PreferencesHelper(applicationContext: Application) {
    private val sp: SharedPreferences = applicationContext.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)

    val email: String?
        get() = if (!sp.contains(USER_FIELD)) null else sp.getString(USER_FIELD, "")

    fun saveUser(userEmail: String) {
        val editor = sp.edit()
        editor.putString(USER_FIELD, userEmail)
        editor.apply()
    }

    fun logout() {
        if (!sp.contains(USER_FIELD)) return
        val editor = sp.edit()
        editor.remove(USER_FIELD)
        editor.apply()

    }
}
