package ru.hse.meditation.ui.meditations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import ru.hse.meditation.R
import ru.hse.meditation.databinding.FragmentMeditationsBinding
import ru.hse.meditation.ui.adapter.MeditationsTabAdapter

class MeditationsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMeditationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.viewPager.adapter = MeditationsTabAdapter(requireActivity())
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favorite)
                1 -> tab.text = getString(R.string.recent)
                2 -> tab.text = getString(R.string.all)
            }
        }.attach()

        return root
    }
}
