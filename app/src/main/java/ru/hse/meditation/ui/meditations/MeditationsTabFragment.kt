package ru.hse.meditation.ui.meditations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import ru.hse.meditation.databinding.FragmentMeditationsTabBinding
import ru.hse.meditation.model.entity.Practice
import ru.hse.meditation.ui.adapter.MeditationsAdapter

abstract class MeditationsTabFragment : Fragment() {

    protected abstract fun getList(): LiveData<List<Practice>>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMeditationsTabBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.meditationsList.layoutManager = LinearLayoutManager(activity)
        val adapter = MeditationsAdapter(
            this
        )
        binding.meditationsList.adapter = adapter

        getList().observe(viewLifecycleOwner) {
            adapter.setPracticeList(it)
        }

        return root
    }
}
