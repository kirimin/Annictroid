package me.kirimin.annictroid._common.utils

import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object ApiDateFormatter {

    /**
     * カレンダーをUTC時間に修正してAPI送信形式に整形
     */
    fun getApiTime(calendar: Calendar): String {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
        format.timeZone = TimeZone.getTimeZone("UTC")
        return format.format(calendar.time).toString()
    }

    /**
     * APIから取得したUTC時間を修正してDateに変換
     */
    fun getDateByApiTime(apiTime: String):Date {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
        format.timeZone = TimeZone.getTimeZone("UTC")
        return format.parse(apiTime.replace("T", " "))
    }

    /**
     * APIから取得したUTC時間を修正して表示形式に整形
     */
    fun getDisplayDateTimeByApiTime(apiTime: String, inFormat: String = "MM/dd(E) kk:mm~"): String {
        val calendar = Calendar.getInstance()
        calendar.time = getDateByApiTime(apiTime)
        return DateFormat.format(inFormat, calendar).toString()
    }

}