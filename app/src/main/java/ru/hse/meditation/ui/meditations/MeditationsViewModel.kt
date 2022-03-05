package ru.hse.meditation.ui.meditations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MeditationsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is meditations Fragment"
    }
    val text: LiveData<String> = _text
}