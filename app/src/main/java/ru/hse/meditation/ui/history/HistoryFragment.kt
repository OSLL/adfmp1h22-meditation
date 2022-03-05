package ru.hse.meditation.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ru.hse.meditation.CustomRecyclerAdapter
import ru.hse.meditation.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.historyList.layoutManager = LinearLayoutManager(activity)
        binding.historyList.adapter = CustomRecyclerAdapter(
            mutableListOf("A", "b", "C", "A", "b", "C", "A", "b", "C", "A", "b", "C", "A", "b", "C"),
            this
        )
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}