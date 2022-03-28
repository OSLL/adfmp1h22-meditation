package ru.hse.meditation.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import ru.hse.meditation.R
import ru.hse.meditation.model.entity.Practice
import ru.hse.meditation.ui.meditations.MeditationInfoActivity

class MeditationsAdapter(
    private val fragment: Fragment
): RecyclerView.Adapter<MeditationsAdapter.MyViewHolder>() {

    private var list: List<Practice> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setPracticeList(list: List<Practice>) {
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
        val practice = list[position]
        holder.textView.text = practice.name
        holder.itemView.setOnClickListener {
            val myIntent = Intent(fragment.activity, MeditationInfoActivity::class.java)
            myIntent.putExtra("practice", practice)
            fragment.startActivity(myIntent)
        }
    }

    override fun getItemCount(): Int = list.size
}
