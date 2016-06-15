package me.kirimin.annictroid.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationManagerCompat
import android.support.v7.app.NotificationCompat
import me.kirimin.annictroid.R
import me.kirimin.annictroid._common.preferences.AppPreferences

/**
 * タイマーから呼び出されてprogramを通知する方のReceiver
 */
class ProgramReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (!AppPreferences.isNotificationEnable(context)) {
            return
        }
        val work = intent.getStringExtra("work")
        val episodeNum = intent.getIntExtra("episode_num", 0)
        val time = intent.getStringExtra("time")
        val builder = NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.ic_stat_annict)
                .setContentTitle(work)
                .setContentText("#$episodeNum　$time")
        if (AppPreferences.isNotificationWithSound(context)) {
            builder.setDefaults(NotificationCompat.DEFAULT_ALL)
        }

        val manager = NotificationManagerCompat.from(context);
        manager.notify(1, builder.build());

        // 次のアラームをセットするために更新する
        context.sendBroadcast(Intent(context, TimerSetReceiver::class.java))
    }
}