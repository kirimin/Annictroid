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

    /**
     * APIに送信するための今期指定文字列を取得
     * 例："2016-spring"
     */
    fun getCurrentSeason(calendar: Calendar): String {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        return year.toString() + "-" + when (month) {
            in Calendar.JANUARY..Calendar.MARCH -> "winter"
            in Calendar.APRIL..Calendar.JUNE -> "spring"
            in Calendar.JULY..Calendar.SEPTEMBER -> "summer"
            in Calendar.OCTOBER..Calendar.DECEMBER -> "autumn"
            else -> ""
        }
    }

    /**
     * APIに送信するための来期指定文字列を取得
     * 例："2016-summer"
     */
    fun getNextSeason(calendar: Calendar): String {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        return when (month) {
            in Calendar.JANUARY..Calendar.MARCH -> year.toString() + "-spring"
            in Calendar.APRIL..Calendar.JUNE -> year.toString() + "-summer"
            in Calendar.JULY..Calendar.SEPTEMBER -> year.toString() + "-autumn"
            in Calendar.OCTOBER..Calendar.DECEMBER -> (year + 1).toString() + "-winter"
            else -> ""
        }
    }

}