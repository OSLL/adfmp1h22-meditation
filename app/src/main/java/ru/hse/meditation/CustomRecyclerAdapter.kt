package ru.hse.meditation

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class CustomRecyclerAdapter(
    private val names: MutableList<String>,
    private val fragment: Fragment
) : RecyclerView.Adapter<CustomRecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
        val dateTextView: TextView = itemView.findViewById(R.id.textDate)
        val timeTextView: TextView = itemView.findViewById(R.id.textTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.history_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textView.text = names[position]
        holder.dateTextView.text = "2022/04/02"
        holder.timeTextView.text = "1 hour"
        holder.itemView.setOnClickListener {
            Log.d("TAG", position.toString())
            val myIntent = Intent(fragment.activity, EditEntryActivity::class.java)
            fragment.startActivity(myIntent)
        }
    }

    override fun getItemCount(): Int = names.size
}