package me.kirimin.annictroid.work

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_about.*

import me.kirimin.annictroid.R

class WorkActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work)
        setSupportActionBar(toolbar)
        supportActionBar?.title = intent.getStringExtra(WORK_TITLE)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
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
