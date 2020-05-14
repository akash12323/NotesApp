package com.example.todofirebase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_list.view.*

class TodoAdapter(val user:ArrayList<usernote>):
    RecyclerView.Adapter<TodoAdapter.ItemVieHolder>() {

    var onItemClick:((user: usernote)->Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVieHolder {
        return ItemVieHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_list,
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
        fun bind(user: usernote){
            itemView.apply {
                tv1.text = user.title
                tv2.text = user.details
                setOnClickListener {
                    onItemClick?.invoke(user)
                }
            }
        }
    }
}