package com.example.todofirebase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_reminder.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ReminderAdapter(val user:ArrayList<reminders>):
    RecyclerView.Adapter<ReminderAdapter.ItemVieHolder>() {

    var onItemClick:((user: reminders)->Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVieHolder {
        return ItemVieHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_reminder,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = user.size

    override fun onBindViewHolder(holder: ItemVieHolder, position: Int) {
        holder.bind(user[position])
    }

    inner class ItemVieHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(user: reminders){
            itemView.apply {
                tv1.text = user.title
                tv2.text = user.details
                updateTime(user.time)
                updateDate(user.date)
//                time.text = user.time.toString()
                setOnClickListener {
                    onItemClick?.invoke(user)
                }
            }
        }
        private fun updateTime(time: Long) {
            val myFormat = "hh:mm a"
            val sdf = SimpleDateFormat(myFormat)
            itemView.time.text = sdf.format(Date(time))
        }


        private fun updateDate(time: Long) {
            val myFormat = "EEE, d MMM yyyy"
            val sdf = SimpleDateFormat(myFormat)
            itemView.date.text = sdf.format(Date(time))
        }
    }
}