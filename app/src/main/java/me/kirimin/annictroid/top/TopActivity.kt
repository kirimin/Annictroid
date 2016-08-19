package me.kirimin.annictroid.top

import android.app.Dialog
import android.content.Intent
import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.view.MenuItem
import android.widget.TextView

import me.kirimin.annictroid.auth.AuthActivity

import kotlinx.android.synthetic.main.activity_top.*
import me.kirimin.annictroid.R
import me.kirimin.annictroid._common.preferences.AppPreferences
import me.kirimin.annictroid.settings.SettingsActivity
import me.kirimin.annictroid.works.WorksTopFragment
import org.w3c.dom.Text

class TopActivity : AppCompatActivity() {

    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top)
        setSupportActionBar(toolbar)
        if (AppPreferences.getToken() == "") {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
            return
        }

        supportActionBar?.title = getString(R.string.app_name)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        drawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name)
        drawerLayout.addDrawerListener(drawerToggle)
        navigationButtonHome.setOnClickListener {
            replaceContent(TopFragment(), navigationButtonHome)
            drawerLayout.closeDrawers()
        }
        navigationButtonAnimeList.setOnClickListener {
            replaceContent(WorksTopFragment(), navigationButtonAnimeList, "アニメを探す")
            drawerLayout.closeDrawers()
        }
        navigationButtonSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            drawerLayout.closeDrawers()
        }
        navigationButtonLogout.setOnClickListener {
            LogoutDialogFragment().show(supportFragmentManager, LogoutDialogFragment::class.java.canonicalName)
        }

        if (savedInstanceState == null) {
            replaceContent(TopFragment(), navigationButtonHome)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (drawerToggle.onOptionsItemSelected(item)) return true
        return super.onOptionsItemSelected(item)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToggle.onConfigurationChanged(newConfig)
    }

    private fun replaceContent(fragment: Fragment, selectedItem: TextView, newTitle: String = getString(R.string.app_name)) {
        toolbar.title = newTitle
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.layoutFragmentContainer, fragment)
                .commit()
        navigationButtonHome.setTextColor(ContextCompat.getColor(this, R.color.colorTextSecondary))
        navigationButtonAnimeList.setTextColor(ContextCompat.getColor(this, R.color.colorTextSecondary))

        selectedItem.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
    }

    class LogoutDialogFragment : DialogFragment() {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return AlertDialog.Builder(activity)
                    .setTitle("ログアウト")
                    .setMessage("ログアウトしますか？")
                    .setPositiveButton(android.R.string.ok) { dialog, which ->
                        val activity = activity ?: return@setPositiveButton
                        AppPreferences.setToken("")
                        activity.finish()
                        val intent = Intent(activity, AuthActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                    }
                    .setNegativeButton(android.R.string.cancel, null)
                    .create()
        }
    }
}
