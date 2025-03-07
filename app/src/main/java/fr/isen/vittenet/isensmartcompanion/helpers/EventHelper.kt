package fr.isen.vittenet.isensmartcompanion.helpers

import android.content.Context


    private const val PREFS_NAME = "event_prefs"

    fun getIsNotified(context: Context, eventTitle: String): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(eventTitle, false)
    }

    fun setNotificationState(context: Context, eventTitle: String, state: Boolean) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(eventTitle, state).apply()
    }
