package ru.hse.meditation.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.meditationOfTheDay.meditationName.visibility = View.INVISIBLE
        binding.meditationOfTheDay.progressBar.visibility = View.VISIBLE

        binding.todayValue.text = viewModel.today
        binding.thisWeekValue.text = viewModel.thisWeek

        binding.userName.onFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    binding.button.visibility = View.VISIBLE
                }
            }
        }

        addUserName(binding)
        addMeditationOfTheDay(binding)

        binding.button.setOnClickListener {
            val preferences = requireActivity().getPreferences(Activity.MODE_PRIVATE)
            binding.userName.clearFocus()
            viewModel.setUserName(binding.userName.text.toString(), preferences)
            binding.button.visibility = View.INVISIBLE
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view?.windowToken, 0)
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
                meditationName.text = practice.name
                root.setOnClickListener {
                    val myIntent = Intent(activity, MeditationInfoActivity::class.java)
                    myIntent.putExtra("practice", practice)
                    startActivity(myIntent)
                }
                meditationName.visibility = View.VISIBLE
                progressBar.visibility = View.INVISIBLE
            }
        }
    }
}
