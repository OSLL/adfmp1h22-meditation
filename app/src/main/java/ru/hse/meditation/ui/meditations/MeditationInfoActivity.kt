package ru.hse.meditation.ui.meditations

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.hse.meditation.R
import ru.hse.meditation.databinding.ActivityMeditationInfoBinding
import ru.hse.meditation.model.entity.Practice
import ru.hse.meditation.model.repository.PracticeRepository
import ru.hse.meditation.ui.ActivityWithBackButton

class MeditationInfoActivity : ActivityWithBackButton() {
    private lateinit var practiceRepository: PracticeRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMeditationInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        practiceRepository = PracticeRepository(application)

        val practice = intent.getSerializableExtra("practice") as Practice
        title = practice.name
        binding.meditationDescription.text = practice.description
        binding.meditationDurationValue.setText(practice.duration.toString())
        binding.meditationDurationValue.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.meditationDurationValue.text.toString().toIntOrNull()?.let {
                    practice.duration = it
                    savePractice(practice)
                }
                v.clearFocus();
            }
            false
        }
        binding.meditationFavoriteValue.isChecked = practice.isFavorite
        binding.meditationFavoriteValue.setOnCheckedChangeListener { _, isChecked ->
            practice.isFavorite = isChecked
            savePractice(practice)
        }
        binding.button2.setOnClickListener {
            val myIntent = Intent(this, MeditationActivity::class.java)
            myIntent.putExtra("practice", practice)
            startActivity(myIntent)
        }
    }

    private fun savePractice(practice: Practice) {
        lifecycleScope.launch(NonCancellable) {
            withContext(Dispatchers.IO) {
                practiceRepository.update(practice)
            }
        }
    }
}
