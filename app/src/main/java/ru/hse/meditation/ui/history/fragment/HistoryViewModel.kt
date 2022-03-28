package ru.hse.meditation.ui.history.fragment

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.hse.meditation.model.entity.Course
import ru.hse.meditation.model.entity.PracticeRecord
import ru.hse.meditation.model.repository.CourseRepository
import ru.hse.meditation.model.repository.PracticeRecordRepository

class HistoryViewModel(application: Application) : ViewModel() {
    private val practiceRecordRepository = PracticeRecordRepository(application)

    val records: LiveData<List<PracticeRecord>> = practiceRecordRepository.getAll()
}
