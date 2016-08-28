package me.kirimin.annictroid.top

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_top.view.*
import me.kirimin.annictroid.R
import me.kirimin.annictroid.myanime.MyAnimeListFragment
import me.kirimin.annictroid.program.ProgramListView

class TopFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_top, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = TopPagerAdapter(childFragmentManager)
        adapter.addPage(ProgramListView.ProgramListFragment(), "放送予定")
        adapter.addPage(MyAnimeListFragment.newInstance(MyAnimeListFragment.Type.WATCHING), "見てるアニメ")
        adapter.addPage(MyAnimeListFragment.newInstance(MyAnimeListFragment.Type.WATCHED), "見たアニメ")
        adapter.addPage(MyAnimeListFragment.newInstance(MyAnimeListFragment.Type.WANNA_WATCH), "見たいアニメ")
        view?.viewPager?.adapter = adapter
        view?.pagerTab?.setTextColor(ContextCompat.getColor(context, android.R.color.white))
        view?.pagerTab?.setViewPager(view.viewPager)
    }
}
