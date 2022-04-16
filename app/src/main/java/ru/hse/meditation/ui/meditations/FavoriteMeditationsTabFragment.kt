package ru.hse.meditation.ui.meditations

import androidx.lifecycle.LiveData
import ru.hse.meditation.model.entity.Practice
import ru.hse.meditation.model.repository.PracticeRepository

class FavoriteMeditationsTabFragment : MeditationsTabFragment() {

    override fun getList(): LiveData<List<Practice>> {
        val repository = PracticeRepository(requireActivity().application)
        return repository.getFavorite()
    }
}
