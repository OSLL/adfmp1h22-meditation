package ru.hse.meditation.ui.course.add

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.hse.meditation.databinding.ActivityAddCourseBinding
import ru.hse.meditation.model.repository.CourseRepository
import ru.hse.meditation.ui.ActivityWithBackButton
import ru.hse.meditation.ui.adapter.AddCourseAdapter
import ru.hse.meditation.ui.course.create.CreateCourseActivity

class AddCourseActivity : ActivityWithBackButton() {
    private lateinit var binding: ActivityAddCourseBinding
    private lateinit var adapter: AddCourseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.listOfNewCourses.layoutManager = LinearLayoutManager(applicationContext)

        adapter = AddCourseAdapter(this)
        binding.listOfNewCourses.adapter = adapter

        binding.newCourseButton.setOnClickListener {
            val intent = Intent(applicationContext, CreateCourseActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            binding.addActivityBody.visibility = View.INVISIBLE
            binding.progressBarCoursesFromGithub.visibility = View.VISIBLE

            val courseRepository = CourseRepository(application)
            val allCourses = withContext(Dispatchers.IO) {
                courseRepository.loadAllCourses()
            }

            val currentCourses = withContext(Dispatchers.IO) {
                courseRepository.getAllAwait()
            }

            adapter.setCourseList(allCourses.toMutableList().apply { removeAll(currentCourses) })
            binding.progressBarCoursesFromGithub.visibility = View.INVISIBLE
            binding.addActivityBody.visibility = View.VISIBLE
        }
    }
}
