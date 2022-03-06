package ru.hse.meditation.ui.meditations

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import ru.hse.meditation.MeditationInfoActivity
import ru.hse.meditation.R

class MeditationsAdapter(
    private val list: MutableList<String>,
    private val fragment: Fragment
): RecyclerView.Adapter<MeditationsAdapter.MyViewHolder>() {

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
        holder.textView.text = list[position]
        holder.itemView.setOnClickListener {
            val myIntent = Intent(fragment.activity, MeditationInfoActivity::class.java)
            myIntent.putExtra("description", """
                this
                is
                description
                
                
                !!!
            """.trimIndent())
            fragment.startActivity(myIntent)
        }
    }

    override fun getItemCount(): Int = list.size
}
