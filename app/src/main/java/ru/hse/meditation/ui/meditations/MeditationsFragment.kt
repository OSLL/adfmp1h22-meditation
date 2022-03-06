package ru.hse.meditation.ui.meditations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import ru.hse.meditation.R
import ru.hse.meditation.databinding.FragmentMeditationsBinding

class MeditationsFragment : Fragment() {

    private var _binding: FragmentMeditationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMeditationsBinding.inflate(inflater, container, false)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}