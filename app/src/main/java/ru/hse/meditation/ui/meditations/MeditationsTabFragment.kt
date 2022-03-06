package ru.hse.meditation.ui.meditations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import ru.hse.meditation.databinding.FragmentMeditationsTabBinding

class MeditationsTabFragment(private val list: List<String>) : Fragment() {

    private var _binding: FragmentMeditationsTabBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMeditationsTabBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.meditationsList.layoutManager = LinearLayoutManager(activity)
        binding.meditationsList.adapter = MeditationsAdapter(
            list.toMutableList()
        )

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}