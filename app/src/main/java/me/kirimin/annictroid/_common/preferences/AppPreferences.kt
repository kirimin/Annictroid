package me.kirimin.annictroid._common.preferences

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import me.kirimin.annictroid.R

object AppPreferences {

    fun getToken(context: Context) = PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.key_token), "")

    fun setToken(context: Context, token: String) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(context.getString(R.string.key_token), token).apply()
    }

    fun getNotificationBuffer(context: Context) =
            PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.key_preference_notification_buffer), "0")

    fun isNotificationEnable(context: Context) =
            PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.key_preference_is_notification), true)
}