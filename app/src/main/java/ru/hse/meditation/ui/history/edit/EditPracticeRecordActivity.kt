package ru.hse.meditation.ui.history.edit

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.hse.meditation.R
import ru.hse.meditation.databinding.ActivityEditPracticeRecordBinding
import ru.hse.meditation.model.entity.PracticeRecord
import ru.hse.meditation.model.repository.PracticeRecordRepository
import ru.hse.meditation.ui.ActivityWithBackButton
import java.text.DateFormat

class EditPracticeRecordActivity : ActivityWithBackButton() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityEditPracticeRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val record = intent.getSerializableExtra("record") as PracticeRecord
        binding.editMeditationValue.text = record.practiceName
        binding.editDateValue.text = dateFormat.format(record.dateTime)
        binding.editDurationValue.text = getString(R.string.minutes).format(record.duration)
        binding.editComment.setText(record.comment)

        binding.editSave.setOnClickListener {
            record.comment = binding.editComment.text.toString()
            val repository = PracticeRecordRepository(application)
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    withContext(NonCancellable) {
                        repository.update(record)
                    }
                }
                onBackPressed()
            }
        }

        binding.editDelete.setOnClickListener {
            val repository = PracticeRecordRepository(application)
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    withContext(NonCancellable) {
                        repository.delete(record)
                    }
                }
                onBackPressed()
            }
        }
    }

    companion object {
        private val dateFormat = DateFormat.getDateTimeInstance(
            DateFormat.MEDIUM,
            DateFormat.SHORT
        )
    }
}
