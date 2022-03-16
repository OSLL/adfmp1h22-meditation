package ru.hse.meditation

import android.content.Intent
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
                val intent = Intent(applicationContext, ChangeCourseActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.about -> {
                Log.d("TAG", "About")
                val intent = Intent(applicationContext, AboutActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
