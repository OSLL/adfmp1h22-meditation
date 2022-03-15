package ru.hse.meditation.ui.course


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.hse.meditation.MeditationInfoActivity
import ru.hse.meditation.TheoryActivity
import ru.hse.meditation.databinding.FragmentCourseBinding


class CourseFragment : Fragment() {

    private var _binding: FragmentCourseBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val dataTheory = (1..10).map { "Theory $it"}
    private val dataPractice = (1..10).map { "Practice $it"}


    private fun justifyListViewHeightBasedOnChildren(listView: ListView) {
        val adapter: ListAdapter = listView.adapter ?: return
        val vg: ViewGroup = listView
        var totalHeight = 0
        for (i in 0 until adapter.count) {
            val listItem: View = adapter.getView(i, null, vg)
            listItem.measure(0, 0)
            totalHeight += listItem.measuredHeight
        }
        val par: ViewGroup.LayoutParams = listView.layoutParams
        par.height = totalHeight + listView.dividerHeight * (adapter.count - 1)
        listView.layoutParams = par
        listView.requestLayout()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ViewModelProvider(this).get(CourseViewModel::class.java)

        _binding = FragmentCourseBinding.inflate(inflater, container, false)
        val root: View = binding.root

        context?.let { context ->
            val listView1 = binding.theoryList
            val listView2 = binding.practiceList
            listView1.setOnItemClickListener { parent: AdapterView<*>, view: View, position: Int, id: Long ->
                val myIntent = Intent(activity, TheoryActivity::class.java)
                myIntent.putExtra(
                    "description", """
                this
                is
                theory

                for
                ${dataPractice[position]}
                !!!
            """.trimIndent()
                )
                startActivity(myIntent)
            }

            listView2.setOnItemClickListener { parent: AdapterView<*>, view: View, position: Int, id: Long ->
                val myIntent = Intent(activity, MeditationInfoActivity::class.java)
                myIntent.putExtra(
                    "description", """
                this
                is
                description

                of
                ${dataPractice[position]}
                !!!
            """.trimIndent()
                )
                startActivity(myIntent)
            }

            listView1.adapter =
                ArrayAdapter(context, android.R.layout.simple_list_item_1, dataTheory)
            listView2.adapter =
                ArrayAdapter(context, android.R.layout.simple_list_item_1, dataPractice)
            justifyListViewHeightBasedOnChildren(listView1)
            justifyListViewHeightBasedOnChildren(listView2)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}