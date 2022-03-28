package ru.hse.meditation.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import ru.hse.meditation.R
import ru.hse.meditation.model.entity.Theory
import ru.hse.meditation.ui.course.theory.TheoryActivity

class TheoryAdapter(
    private val fragment: Fragment
) : RecyclerView.Adapter<TheoryAdapter.MyViewHolder>() {

    private var list: List<Theory> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setTheoryList(list: List<Theory>) {
        this.list = list
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.meditation_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.meditation_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val theory = list[position]
        holder.textView.text = theory.name
        holder.itemView.setOnClickListener {
            val myIntent = Intent(fragment.activity, TheoryActivity::class.java)
            myIntent.putExtra("theory", theory)
            fragment.startActivity(myIntent)
        }
    }

    override fun getItemCount(): Int = list.size
}
