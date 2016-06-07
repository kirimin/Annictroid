package me.kirimin.annictroid.auth

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import me.kirimin.annictroid.R
import kotlinx.android.synthetic.main.activity_auth.*
import me.kirimin.annictroid.BuildConfig
import me.kirimin.annictroid._common.networks.RetrofitClient
import me.kirimin.annictroid._common.networks.apis.AnnictService
import me.kirimin.annictroid._common.preferences.AppPreferences
import me.kirimin.annictroid.notification.TimerSetReceiver
import me.kirimin.annictroid.top.TopActivity
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

class AuthActivity : AppCompatActivity() {

    private val subscriptions = CompositeSubscription()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "ログイン"
        buttonOpenAuthPage.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://annict.com/oauth/authorize?client_id=${BuildConfig.CLIENT_ID}&redirect_uri=urn%3Aietf%3Awg%3Aoauth%3A2.0%3Aoob&response_type=code&scope=read+write")))
        }

        buttonLogin.setOnClickListener {
            buttonLogin.isClickable = false
            subscriptions.add(RetrofitClient.default().build().create(AnnictService::class.java)
                    .token(code = editTextCode.text.toString())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe ({
                        AppPreferences.setToken(this, it.access_token)
                        sendBroadcast(Intent(this, TimerSetReceiver::class.java))
                        startActivity(Intent(this, TopActivity::class.java))
                        finish()
                    }, {
                        Toast.makeText(this, "認証に失敗したようです。。。", Toast.LENGTH_SHORT).show()
                        buttonLogin.isClickable = true
                    }))
        }
    }
}
