package ru.hse.meditation.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.hse.meditation.R
import ru.hse.meditation.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val a = LayoutInflater.from(activity).inflate(R.layout.meditation_item, null)
        a.setOnClickListener {
            Log.d("TAG", "HI")
        }
        binding.meditationOfTheDay.addView(a)

        for (i in 0..10) {
            val x = LayoutInflater.from(activity).inflate(R.layout.meditation_item, null)
            x.setOnClickListener {
                Log.d("TAG", "HI")
            }
            binding.dynamic.addView(x)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}