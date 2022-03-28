package ru.hse.meditation.ui.history.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import ru.hse.meditation.R
import ru.hse.meditation.model.entity.PracticeRecord
import ru.hse.meditation.ui.history.edit.EditPracticeRecordActivity
import java.text.DateFormat

class PracticeRecordsRecyclerAdapter(
    private val fragment: Fragment
) : RecyclerView.Adapter<PracticeRecordsRecyclerAdapter.MyViewHolder>() {
    private var data = emptyList<PracticeRecord>()
    private val dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT)

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newData: List<PracticeRecord>) {
        data = newData
        notifyDataSetChanged()
    }

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
        val record = data[position]
        holder.textView.text = record.practiceName
        val dateTime = dateFormat.format(record.dateTime)
        holder.dateTextView.text = dateTime
        holder.timeTextView.text = fragment.getString(R.string.minutes).format(record.duration)
        holder.itemView.setOnClickListener {
            val myIntent = Intent(fragment.activity, EditPracticeRecordActivity::class.java)
            myIntent.putExtra("record", record)
            fragment.startActivity(myIntent)
        }
    }

    override fun getItemCount(): Int = data.size
}
