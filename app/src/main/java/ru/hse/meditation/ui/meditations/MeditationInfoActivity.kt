package ru.hse.meditation.ui.meditations

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import ru.hse.meditation.R
import ru.hse.meditation.databinding.ActivityMeditationInfoBinding
import ru.hse.meditation.model.entity.Practice
import ru.hse.meditation.ui.ActivityWithBackButton

class MeditationInfoActivity : ActivityWithBackButton() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMeditationInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val practice = intent.getSerializableExtra("practice") as Practice
        title = practice.name
        binding.meditationDescription.text = practice.description
        binding.meditationDurationValue.setText(practice.duration.toString())
        binding.meditationDurationValue.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.meditationDurationValue.text.toString().toIntOrNull()?.let {
                    practice.duration = it
                }
                v.clearFocus();
            }
            false
        }
        binding.button2.setOnClickListener {
            val myIntent = Intent(this, MeditationActivity::class.java)
            myIntent.putExtra("practice", practice)
            startActivity(myIntent)
        }
    }
}
