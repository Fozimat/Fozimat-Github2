package com.fozimat.fozimat_github

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import com.fozimat.fozimat_github.alarm.AlarmReceiver
import com.fozimat.fozimat_github.databinding.ActivityAlarmBinding
import com.google.android.material.snackbar.Snackbar

class AlarmActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlarmBinding
    private lateinit var alarmReceiver: AlarmReceiver
    private lateinit var mSharedPreferences: SharedPreferences

    companion object {
        const val PREF_NAME = "pref_name"
        const val DAILY = "daily"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        alarmReceiver = AlarmReceiver()
        mSharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        setAlarmTitle()

        binding.switch1.isChecked = mSharedPreferences.getBoolean(DAILY, false)
        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                val message = resources.getString(R.string.message)
                val messageRepeat = resources.getString(R.string.messageRepeat)
                alarmReceiver.setRepeatingAlarm(this@AlarmActivity, AlarmReceiver.TYPE_REPEATING, message)
                showSnackbarMessage(messageRepeat)
            } else {
                val messageCancel = resources.getString(R.string.messageCancel)
                alarmReceiver.cancelAlarm(this@AlarmActivity, AlarmReceiver.TYPE_REPEATING, messageCancel)
                showSnackbarMessage(messageCancel)
            }
            savePref(isChecked)
        }
    }

    private fun setAlarmTitle() {
        val title = resources.getString(R.string.daily_reminder)
        supportActionBar?.title = title
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_settings -> {
                val act = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(act)
            }
            R.id.action_fav -> {
                val fav = Intent(this, FavoriteActivity::class.java)
                startActivity(fav)
            }
            R.id.action_remind -> {
                val rem = Intent(this, AlarmActivity::class.java)
                startActivity(rem)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun savePref(value: Boolean) {
        val editor = mSharedPreferences.edit()
        editor.putBoolean(DAILY, value)
        editor.apply()
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}