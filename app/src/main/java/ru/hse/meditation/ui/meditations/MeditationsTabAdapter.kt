package ru.hse.meditation.ui.meditations

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.hse.meditation.model.repository.PracticeRepository
import java.lang.IllegalStateException

class MeditationsTabAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val repository = PracticeRepository(fragmentActivity.application)

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> MeditationsTabFragment(repository.getFavorite())
        1 -> MeditationsTabFragment(repository.getRecent())
        2 -> MeditationsTabFragment(repository.getAll())
        else -> throw IllegalStateException("Wrong fragment position: $position")
    }
}
