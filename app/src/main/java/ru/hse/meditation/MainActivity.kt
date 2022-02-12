package ru.hse.meditation

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val message: TextView = findViewById(R.id.textView)
        val button: Button = findViewById(R.id.button)
        button.setOnClickListener(object : View.OnClickListener {
            var timesPressed: Int = 1
            override fun onClick(v: View?) {
                if (v?.id != R.id.button) return
                message.text = getString(R.string.text_view).format(timesPressed++)
            }
        })
    }
}