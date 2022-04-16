package ru.hse.meditation.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.hse.meditation.R
import ru.hse.meditation.databinding.FragmentHomeBinding
import ru.hse.meditation.ui.factory
import ru.hse.meditation.ui.meditations.MeditationInfoActivity

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels { factory() }
    private var _binding: FragmentHomeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        _binding = binding

        binding.meditationOfTheDay.itemName.visibility = View.INVISIBLE
        binding.meditationOfTheDay.progressBar.visibility = View.VISIBLE

        binding.todayValue.text = viewModel.today
        binding.thisWeekValue.text = viewModel.thisWeek

        addUserName(binding)
        addMeditationOfTheDay(binding)

        binding.userName.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                saveUserName(binding)
                true
            } else {
                false
            }
        }

        viewModel.records.observe(viewLifecycleOwner) { records ->
            lifecycleScope.launch {
                val (today, thisWeek) = viewModel.getStatistics(records)
                val formattedToday = getString(R.string.minutes).format(today)
                val formattedThisWeek = getString(R.string.minutes).format(thisWeek)
                binding.todayValue.text = formattedToday
                binding.thisWeekValue.text = formattedThisWeek
                viewModel.today = formattedToday
                viewModel.thisWeek = formattedThisWeek
            }
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        _binding?.let {
            addMeditationOfTheDay(it)
        }
    }

    private fun saveUserName(binding: FragmentHomeBinding) {
        val preferences = requireActivity().getPreferences(Activity.MODE_PRIVATE)
        binding.userName.clearFocus()
        viewModel.setUserName(binding.userName.text.toString(), preferences)
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun addUserName(binding: FragmentHomeBinding) {
        lifecycleScope.launch {
            val preferences = requireActivity().getPreferences(Activity.MODE_PRIVATE)
            val userName = viewModel.getUserName(preferences)
            binding.userName.setText(userName)
        }
    }

    private fun addMeditationOfTheDay(binding: FragmentHomeBinding) {
        lifecycleScope.launch {
            val preferences = requireActivity().getPreferences(Activity.MODE_PRIVATE)
            val practice = viewModel.getMeditationOfTheDay(preferences)
            with(binding.meditationOfTheDay) {
                itemName.text = practice.name
                root.setOnClickListener {
                    val myIntent = Intent(activity, MeditationInfoActivity::class.java)
                    myIntent.putExtra("practice", practice)
                    startActivity(myIntent)
                }
                itemName.visibility = View.VISIBLE
                progressBar.visibility = View.INVISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
