package me.kirimin.annictroid._common.extensions

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ExtensionsTest {

    @Test
    fun floatToIntegerStringTest(){
        Assert.assertEquals(0f.toIntegerString(), "0")
        Assert.assertEquals(5f.toIntegerString(), "5")
        Assert.assertEquals(10.0f.toIntegerString(), "10")
        Assert.assertEquals(0.5f.toIntegerString(), "0.5")
        Assert.assertEquals(5.1f.toIntegerString(), "5.1")
        Assert.assertEquals(10.9f.toIntegerString(), "10.9")
    }
}