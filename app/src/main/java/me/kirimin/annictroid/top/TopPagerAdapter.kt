package me.kirimin.annictroid.top

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class TopPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val fragmentList = mutableListOf<Fragment>()
    private val titleList = mutableListOf<String>()

    override fun getItem(i: Int) = fragmentList[i]

    override fun getCount() = fragmentList.size

    override fun getPageTitle(position: Int) = titleList[position]

    fun addPage(fragment: Fragment, title: String) {
        fragmentList.add(fragment)
        titleList.add(title)
    }
}
