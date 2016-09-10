package me.kirimin.annictroid._common.utils

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

@RunWith(JUnit4::class)
class ApiDateFormatterTest {

    @Test
    @JvmName(name = "getCurrentSeasonは今期を取得出来る")
    fun getCurrentSeasonTest() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, 2016)
        calendar.set(Calendar.MONTH, Calendar.JANUARY)
        Assert.assertTrue(ApiDateFormatter.getCurrentSeason(calendar) == "2016-winter")
        calendar.set(Calendar.MONTH, Calendar.MARCH)
        Assert.assertTrue(ApiDateFormatter.getCurrentSeason(calendar) == "2016-winter")
        calendar.set(Calendar.MONTH, Calendar.APRIL)
        Assert.assertTrue(ApiDateFormatter.getCurrentSeason(calendar) == "2016-spring")
        calendar.set(Calendar.MONTH, Calendar.JUNE)
        Assert.assertTrue(ApiDateFormatter.getCurrentSeason(calendar) == "2016-spring")
        calendar.set(Calendar.YEAR, 2017)
        calendar.set(Calendar.MONTH, Calendar.JULY)
        Assert.assertTrue(ApiDateFormatter.getCurrentSeason(calendar) == "2017-summer")
        calendar.set(Calendar.MONTH, Calendar.SEPTEMBER)
        Assert.assertTrue(ApiDateFormatter.getCurrentSeason(calendar) == "2017-summer")
        calendar.set(Calendar.MONTH, Calendar.OCTOBER)
        Assert.assertTrue(ApiDateFormatter.getCurrentSeason(calendar) == "2017-autumn")
        calendar.set(Calendar.MONTH, Calendar.DECEMBER)
        Assert.assertTrue(ApiDateFormatter.getCurrentSeason(calendar) == "2017-autumn")
    }

    @Test
    @JvmName(name = "getNextSeasonは来期を取得出来る")
    fun getNextSeasonTest() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, 2016)
        calendar.set(Calendar.MONTH, Calendar.JANUARY)
        Assert.assertTrue(ApiDateFormatter.getNextSeason(calendar) == "2016-spring")
        calendar.set(Calendar.MONTH, Calendar.MARCH)
        Assert.assertTrue(ApiDateFormatter.getNextSeason(calendar) == "2016-spring")
        calendar.set(Calendar.MONTH, Calendar.APRIL)
        Assert.assertTrue(ApiDateFormatter.getNextSeason(calendar) == "2016-summer")
        calendar.set(Calendar.MONTH, Calendar.JUNE)
        Assert.assertTrue(ApiDateFormatter.getNextSeason(calendar) == "2016-summer")
        calendar.set(Calendar.YEAR, 2017)
        calendar.set(Calendar.MONTH, Calendar.JULY)
        Assert.assertTrue(ApiDateFormatter.getNextSeason(calendar) == "2017-autumn")
        calendar.set(Calendar.MONTH, Calendar.SEPTEMBER)
        Assert.assertTrue(ApiDateFormatter.getNextSeason(calendar) == "2017-autumn")
        calendar.set(Calendar.MONTH, Calendar.OCTOBER)
        Assert.assertTrue(ApiDateFormatter.getNextSeason(calendar) == "2018-winter")
        calendar.set(Calendar.MONTH, Calendar.DECEMBER)
        Assert.assertTrue(ApiDateFormatter.getNextSeason(calendar) == "2018-winter")
    }
}