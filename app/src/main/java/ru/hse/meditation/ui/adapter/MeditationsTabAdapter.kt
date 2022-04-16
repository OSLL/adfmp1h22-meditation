package ru.hse.meditation.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.hse.meditation.model.repository.PracticeRepository
import ru.hse.meditation.ui.meditations.AllMeditationsTabFragment
import ru.hse.meditation.ui.meditations.FavoriteMeditationsTabFragment
import ru.hse.meditation.ui.meditations.RecentMeditationsTabFragment

class MeditationsTabAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val repository = PracticeRepository(fragmentActivity.application)

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> FavoriteMeditationsTabFragment()
        1 -> RecentMeditationsTabFragment()
        2 -> AllMeditationsTabFragment()
        else -> throw IllegalStateException("Wrong fragment position: $position")
    }
}
