package ru.hse.meditation.ui.home

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import ru.hse.meditation.R
import ru.hse.meditation.model.entity.Practice
import ru.hse.meditation.model.entity.PracticeRecord
import ru.hse.meditation.model.repository.PracticeRecordRepository
import ru.hse.meditation.model.repository.PracticeRepository
import java.util.*

class HomeViewModel(application: Application) : ViewModel() {
    private val practiceRecordRepository = PracticeRecordRepository(application)
    private val practiceRepository = PracticeRepository(application)

    val records: LiveData<List<PracticeRecord>> = practiceRecordRepository.getAll()

    private var userName: String? = null

    suspend fun getUserName(sharedPref: SharedPreferences): String {
        return userName ?: withContext(Dispatchers.IO) {
            val name = sharedPref.getString("user_name", "") ?: ""
            userName = name
            name
        }
    }

    fun setUserName(name: String, sharedPref: SharedPreferences) {
        userName = name
        viewModelScope.launch(NonCancellable) {
            with (sharedPref.edit()) {
                putString("user_name", name)
                apply()
            }
        }
    }

    var today = application.getString(R.string.calculating)
    var thisWeek = application.getString(R.string.calculating)

    private var practiceId = -1
    private var genTime: Date? = null

    suspend fun getStatistics(records: List<PracticeRecord>): Pair<Int, Int> {
        return withContext(Dispatchers.Default) {
            var today = 0
            var thisWeek = 0

            val beginOfTheDay = getBeginningOfTheDay()
            val beginOfTheWeek = getBeginningOfTheWeek()

            records.forEach { record ->
                if (record.dateTime.after(beginOfTheDay)) {
                    today += record.duration
                }
                if (record.dateTime.after(beginOfTheWeek)) {
                    thisWeek += record.duration
                }
            }
            today to thisWeek
        }
    }

    suspend fun getMeditationOfTheDay(sharedPref: SharedPreferences): Practice {
        return withContext(Dispatchers.IO) {
            if (getGenTime(sharedPref).after(getBeginningOfTheDay())) {
                getPractice(sharedPref)
            } else {
                generateNew(sharedPref)
            }
        }
    }

    private fun getGenTime(sharedPref: SharedPreferences): Date = genTime ?: run {
        Date(sharedPref.getLong("gen_date", 0))
    }

    private suspend fun getPractice(sharedPref: SharedPreferences): Practice {
        if (practiceId == -1) {
            practiceId = sharedPref.getInt("practice_id", -1)
        }
        return practiceRepository.getById(practiceId) ?: generateNew(sharedPref)
    }

    private suspend fun generateNew(sharedPref: SharedPreferences): Practice {
        val practice = practiceRepository.getRandom()
        val date = Date()
        genTime = date
        practiceId = practice.id
        with (sharedPref.edit()) {
            putInt("practice_id", practice.id)
            putLong("gen_date", date.time)
            apply()
        }
        return practice
    }

    private fun getBeginningOfTheDay(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR, calendar.getActualMinimum(Calendar.HOUR))
        calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE))
        calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND))
        calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND))
        return calendar.time
    }

    private fun getBeginningOfTheWeek(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getActualMinimum(Calendar.DAY_OF_WEEK))
        calendar.set(Calendar.HOUR, calendar.getActualMinimum(Calendar.HOUR))
        calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE))
        calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND))
        calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND))
        return calendar.time
    }
}
