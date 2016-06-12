package me.kirimin.annictroid.settings

import android.content.Intent
import android.os.Bundle
import android.support.v7.preference.CheckBoxPreference
import android.support.v7.preference.EditTextPreference
import android.support.v7.preference.PreferenceFragmentCompat
import me.kirimin.annictroid.R
import me.kirimin.annictroid.notification.TimerSetReceiver

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(bundle: Bundle?, s: String?) {
        addPreferencesFromResource(R.xml.fragment_settings)
        val notificationBuffer = findPreference(getString(R.string.key_preference_notification_buffer)) as EditTextPreference
        notificationBuffer.setOnPreferenceChangeListener { preference, newValue ->
            try {
                val buffer = Integer.parseInt(newValue.toString())
                notificationBuffer.summary = "${buffer}分前"
                context.sendBroadcast(Intent(context, TimerSetReceiver::class.java))
                true
            } catch (e: NumberFormatException) {
                false
            }
        }
        if (notificationBuffer.text != null) {
            notificationBuffer.summary = notificationBuffer.text + "分前"
        }
        val isNotificationEnable = findPreference(getString(R.string.key_preference_is_notification)) as CheckBoxPreference
        isNotificationEnable.setOnPreferenceChangeListener { preference, newValue ->
            context.sendBroadcast(Intent(context, TimerSetReceiver::class.java))
            true
        }
        val about = findPreference("about")
        about.setOnPreferenceClickListener {
            startActivity(Intent(context, AboutActivity::class.java))
            true
        }
    }
}