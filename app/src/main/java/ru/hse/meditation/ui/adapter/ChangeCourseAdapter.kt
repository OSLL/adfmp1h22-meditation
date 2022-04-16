package ru.hse.meditation.ui.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.runBlocking
import ru.hse.meditation.R
import ru.hse.meditation.model.entity.Course
import ru.hse.meditation.model.repository.CourseRepository

class ChangeCourseAdapter(
    private val activity: Activity
) : RecyclerView.Adapter<ChangeCourseAdapter.MyViewHolder>() {

    private var list: List<Course> = emptyList()
    private val courseRepository = CourseRepository(activity.application)

    @SuppressLint("NotifyDataSetChanged")
    fun setCourseList(list: List<Course>) {
        this.list = list
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.item_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycle_view_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val course = list[position]
        holder.textView.text = course.name
        holder.itemView.setOnClickListener {
            runBlocking {
                courseRepository.setActive(course)
            }
            activity.onBackPressed()
        }
    }

    override fun getItemCount(): Int = list.size
}