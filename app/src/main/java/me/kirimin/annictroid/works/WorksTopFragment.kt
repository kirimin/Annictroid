package me.kirimin.annictroid.works

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_top.view.*
import me.kirimin.annictroid.R
import me.kirimin.annictroid.myanime.MyAnimeListFragment
import me.kirimin.annictroid.program.ProgramListFragment
import me.kirimin.annictroid.top.TopPagerAdapter

class WorksTopFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_works_top, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = TopPagerAdapter(childFragmentManager)
//        adapter.addPage(ProgramListFragment(), "シーズン")
//        adapter.addPage(MyAnimeListFragment(), "人気のアニメ")
        view?.viewPager?.adapter = adapter
        view?.pagerTab?.setTextColor(ContextCompat.getColor(context, android.R.color.white))
        view?.pagerTab?.setViewPager(view.viewPager)
    }
}