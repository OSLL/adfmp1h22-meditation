package ru.hse.meditation.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import ru.hse.meditation.CustomRecyclerAdapter
import ru.hse.meditation.databinding.FragmentHistoryBinding
import ru.hse.meditation.ui.factory

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val adapter = CustomRecyclerAdapter(this)
    private val viewModel: HistoryViewModel by viewModels { factory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.records.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
        })

        binding.historyList.layoutManager = LinearLayoutManager(activity)
        binding.historyList.adapter = adapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
