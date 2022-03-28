package ru.hse.meditation.ui

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.hse.meditation.ui.course.fragment.CourseViewModel
import ru.hse.meditation.ui.history.fragment.HistoryViewModel
import ru.hse.meditation.ui.home.HomeViewModel
import java.lang.IllegalStateException

class ViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            HistoryViewModel::class.java -> {
                HistoryViewModel(application)
            }
            HomeViewModel::class.java -> {
                HomeViewModel(application)
            }
            CourseViewModel::class.java -> {
                CourseViewModel(application)
            }
            else -> {
                throw IllegalStateException("Unknown view model class")
            }
        }
        return viewModel as T
    }
}

fun Fragment.factory() = ViewModelFactory(requireActivity().application)
