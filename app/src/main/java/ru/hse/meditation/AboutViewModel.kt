package ru.hse.meditation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class AboutViewModel : ViewModel() {

    suspend fun complexTask() {
        withContext(Dispatchers.IO) {
            delay(3000)
        }
    }
}
