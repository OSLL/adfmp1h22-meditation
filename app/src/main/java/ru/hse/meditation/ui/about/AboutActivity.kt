package ru.hse.meditation.ui.about

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.hse.meditation.ui.ActivityWithBackButton
import ru.hse.meditation.R
import ru.hse.meditation.model.entity.PracticeRecord
import ru.hse.meditation.model.repository.PracticeRecordRepository
import java.util.*

class AboutActivity : ActivityWithBackButton() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        val repository = PracticeRecordRepository(application)
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                withContext(NonCancellable) {
                    repository.insert(PracticeRecord(
                        "course_id",
                        "Blue Sky",
                        Date(),
                        3,
                        "Danil comment"
                    ))
                }
            }
        }
    }
}
