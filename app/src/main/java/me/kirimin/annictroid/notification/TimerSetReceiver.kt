package me.kirimin.annictroid.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import me.kirimin.annictroid.BuildConfig
import me.kirimin.annictroid._common.networks.RetrofitClient
import me.kirimin.annictroid._common.networks.apis.AnnictService
import me.kirimin.annictroid._common.preferences.AppPreferences
import me.kirimin.annictroid._common.utils.ApiDateFormatter
import rx.schedulers.Schedulers
import java.util.*

/**
 * 通知のためのタイマーを管理する方のReceiver
 */
class TimerSetReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (AppPreferences.getToken(context) == "" || !AppPreferences.isNotificationEnable(context)) return
        if (Intent.ACTION_PACKAGE_ADDED == intent.action && intent.dataString != ("package:" + context.packageName)) return

        setSelf(context)
        setProgramReceiver(context)
    }

    /**
     * program情報を更新するために次の6時に自分を呼び直す
     */
    private fun setSelf(context: Context) {
        val timeSetIntent = Intent(context, TimerSetReceiver::class.java)
        val sender = PendingIntent.getBroadcast(context, 0, timeSetIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        // 過去のアラームを解除する
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(sender);

        // 06時に通知を更新
        val calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.getDefault()
        if (calendar.get(Calendar.HOUR_OF_DAY) >= 6) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        calendar.set(Calendar.HOUR_OF_DAY, 6)
        calendar.set(Calendar.MINUTE, 0)
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, sender)
    }

    /**
     * 直近のprogramを取得してタイマーにセットする
     */
    private fun setProgramReceiver(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // 過去のアラームを解除する
        val cancelSender = PendingIntent.getBroadcast(context, 0, Intent(context, ProgramReceiver::class.java), PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.cancel(cancelSender);

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MINUTE, Integer.parseInt(AppPreferences.getNotificationBuffer(context)))
        val startedAtGt = ApiDateFormatter.getApiTime(calendar)
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val startedAt = ApiDateFormatter.getApiTime(calendar)

        RetrofitClient.default().build().create(AnnictService::class.java)
                .mePrograms(token = AppPreferences.getToken(context),
                        filterStartedAt = startedAt,
                        filterStartedAtGt = startedAtGt,
                        filterUnWatched = "true",
                        sortStartedAt = "asc",
                        perPage = "1")
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe ({
                    // 実際には一件しか来ない
                    it.programs.forEach {

                        val timeSetIntent = Intent(context, ProgramReceiver::class.java)
                        timeSetIntent.putExtra("work", it.work.title)
                        timeSetIntent.putExtra("episode_num", it.episode.number)
                        timeSetIntent.putExtra("time", ApiDateFormatter.getDisplayDateTimeByApiTime(it.started_at))

                        val sender = PendingIntent.getBroadcast(context, 0, timeSetIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                        val programDate = Calendar.getInstance()
                        programDate.time = ApiDateFormatter.getDateByApiTime(it.started_at)
                        programDate.add(Calendar.MINUTE, -Integer.parseInt(AppPreferences.getNotificationBuffer(context)))
                        alarmManager.set(AlarmManager.RTC_WAKEUP, programDate.timeInMillis, sender)
                        if (BuildConfig.DEBUG) Log.d("test", it.episode.title + " " + programDate.get(Calendar.HOUR_OF_DAY) + " " + programDate.get(Calendar.MINUTE))
                    }
                }, {
                    // omg
                })
    }
}