package ru.hse.meditation.ui.course.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import ru.hse.meditation.databinding.FragmentCourseBinding
import ru.hse.meditation.ui.adapter.MeditationsAdapter
import ru.hse.meditation.ui.adapter.TheoryAdapter
import ru.hse.meditation.ui.factory


class CourseFragment : Fragment() {
    private lateinit var binding: FragmentCourseBinding
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var theoryAdapter: TheoryAdapter
    private lateinit var practiceAdapter: MeditationsAdapter
    private val viewModel: CourseViewModel by viewModels { factory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCourseBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.theoryList.layoutManager = LinearLayoutManager(activity)
        theoryAdapter = TheoryAdapter(this)
        binding.theoryList.adapter = theoryAdapter

        binding.practiceList.layoutManager = LinearLayoutManager(activity)
        practiceAdapter = MeditationsAdapter(this)
        binding.practiceList.adapter = practiceAdapter

        adapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            mutableListOf()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                lifecycleScope.launch {
                    viewModel.changeLevel(position + 1)
                    updateCourse()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }

        return root
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            updateCourse()
        }
    }

    private suspend fun updateCourse() {
        val course = viewModel.currentCourse()
        binding.progressBar2.visibility = View.VISIBLE
        binding.content.visibility = View.INVISIBLE
        adapter.clear()
        adapter.addAll((1..course.numberOfLevels).map { "Level $it" })
        adapter.notifyDataSetChanged()
        binding.spinner.setSelection(course.currentLevel - 1)
        theoryAdapter.setTheoryList(viewModel.currentTheory(course))
        practiceAdapter.setPracticeList(viewModel.currentPractice(course))
        binding.progressBar2.visibility = View.INVISIBLE
        binding.content.visibility = View.VISIBLE
    }
}
