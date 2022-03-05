package ru.hse.meditation

import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

abstract class ActivityWithOptionsMenu : AppCompatActivity() {
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_nav, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.change_course -> {
                Log.d("TAG", "Change course")
                true
            }
            R.id.settings -> {
                Log.d("TAG", "Settings")
                true
            }
            R.id.about -> {
                Log.d("TAG", "About")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}