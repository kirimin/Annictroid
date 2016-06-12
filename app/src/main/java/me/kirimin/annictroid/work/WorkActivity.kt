package me.kirimin.annictroid.work

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_work.*

import me.kirimin.annictroid.R
import me.kirimin.annictroid._common.networks.RetrofitClient
import me.kirimin.annictroid._common.networks.apis.AnnictService
import me.kirimin.annictroid._common.preferences.AppPreferences
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

class WorkActivity : AppCompatActivity() {

    private val subscriptions = CompositeSubscription()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work)
        setSupportActionBar(toolbar)
        supportActionBar?.title = intent.getStringExtra(WORK_TITLE)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        subscriptions.add(RetrofitClient.default().build().create(AnnictService::class.java)
                .works(token = AppPreferences.getToken(this),
                        workIds = intent.getStringExtra(WORK_ID))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    layoutParent.visibility = View.VISIBLE
                    val work = it.works[0]
                    textViewSeason.text = getString(R.string.work_season, work.season_name_text)
                    textViewWatchersCount.text = getString(R.string.work_watchers_count, work.watchers_count)
                    textViewEpisodeCount.text = getString(R.string.work_episode_count, work.episodes_count)
                    textViewHashTag.text = getString(R.string.work_hashtag, work.twitter_hashtag ?: "")
                    work.official_site_url?.let {
                        buttonSite.setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(work.official_site_url))) }
                    }
                    work.wikipedia_url?.let {
                        buttonWikipedia.setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(work.wikipedia_url))) }
                    }
                    work.twitter_username?.let {
                        buttonTwitter.setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + work.twitter_username))) }
                    }
                }, {
                    Toast.makeText(this, R.string.common_network_error, Toast.LENGTH_SHORT).show()
                }))
    }

    override fun onDestroy() {
        subscriptions.unsubscribe()
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (android.R.id.home == item.itemId) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        val WORK_ID = "workId"
        val WORK_TITLE = "workTitle"

        fun getBundle(workId: String, workTitle: String = ""): Bundle {
            val bundle = Bundle()
            bundle.putString(WORK_ID, workId)
            bundle.putString(WORK_TITLE, workTitle)
            return bundle
        }
    }
}
