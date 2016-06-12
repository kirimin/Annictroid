package me.kirimin.annictroid.episode

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_episode.*
import me.kirimin.annictroid.R
import me.kirimin.annictroid._common.networks.RetrofitClient
import me.kirimin.annictroid._common.networks.apis.AnnictService
import me.kirimin.annictroid._common.preferences.AppPreferences
import me.kirimin.annictroid.work.WorkActivity
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

class EpisodeActivity : AppCompatActivity() {

    private val subscriptions = CompositeSubscription()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_episode)
        val episodeId = intent.getStringExtra(EPISODE_ID)
        val workTitle = intent.getStringExtra(WORK_TITLE)
        setSupportActionBar(toolbar)
        supportActionBar?.title = workTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        ratingBar.setOnRatingBarChangeListener { view, rating, fromUser ->
            if (!fromUser) return@setOnRatingBarChangeListener
            if (rating < 1f) {
                ratingBar.rating = 0f
                textViewRatingNum.text = "-"
                return@setOnRatingBarChangeListener
            }
            textViewRatingNum.text = rating.toString()
        }
        buttonRecode.setOnClickListener {
            buttonRecode.isClickable = false
            val rating = textViewRatingNum.text.toString()
            subscriptions.add(RetrofitClient.default().build().create(AnnictService::class.java)
                    .meRecords(token = AppPreferences.getToken(this),
                            episodeId = episodeId,
                            comment = editTextComment.text.toString(),
                            rating = if (rating == "-") "" else rating,
                            shareTwitter = checkboxTwitter.isChecked)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe ({
                        buttonRecode.isClickable = true
                        Toast.makeText(this, R.string.episode_recode_completed, Toast.LENGTH_SHORT).show()
                    }, {
                        buttonRecode.isClickable = true
                        Toast.makeText(this, R.string.common_network_error, Toast.LENGTH_SHORT).show()
                    }))
        }
        subscriptions.add(RetrofitClient.default().build().create(AnnictService::class.java)
                .episodes(token = AppPreferences.getToken(this),
                        episodeIds = episodeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    layoutParent.visibility = View.VISIBLE
                    val episode = it.episodes[0]
                    textViewEpisodeNumber.text = episode.number_text
                    textViewEpisodeTitle.text = episode.title
                    textViewRecodeCount.text = getString(R.string.episode_recode_count, episode.records_count)
                    buttonWork.setOnClickListener {
                        val intent = Intent(this, WorkActivity::class.java)
                        intent.putExtras(WorkActivity.getBundle(episode.work.id, episode.work.title))
                        startActivity(intent)
                    }
                }, {
                    Toast.makeText(this, R.string.common_network_error, Toast.LENGTH_SHORT).show()
                }))
    }

    override fun onDestroy() {
        subscriptions.unsubscribe()
        super.onDestroy()
    }

    companion object {

        val EPISODE_ID = "episodeId"
        val WORK_TITLE = "workTitle"

        fun getBundle(episodeId: String, workTitle: String): Bundle {
            val bundle = Bundle()
            bundle.putString(EPISODE_ID, episodeId)
            bundle.putString(WORK_TITLE, workTitle)
            return bundle
        }
    }
}