package com.quiz.app.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPrefs {
    private var mSharedPref: SharedPreferences? = null
    private val NAME = "com.quiz.app"

    fun init(context: Context) {
        if (mSharedPref == null) mSharedPref =
            context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    }

    fun read(key: String?, defValue: String?): String? {
        return mSharedPref!!.getString(key, defValue)
    }

    fun write(key: String?, value: String?) {
        val prefsEditor = mSharedPref!!.edit()
        prefsEditor.putString(key, value)
        prefsEditor.apply()
    }
}
