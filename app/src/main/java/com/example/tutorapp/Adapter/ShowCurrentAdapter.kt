package com.example.tutorapp.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tutorapp.R
import kotlinx.android.synthetic.main.cardview_people.view.*

class ShowCurrentAdapter(
        private val list: MutableList<Pair<String, String>>,
        private val context: Context
): RecyclerView.Adapter<ShowCurrentAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.cardview_people, viewGroup, false)
        v.people_gender_layout.visibility = View.GONE
        v.people_gender_layout.visibility = View.GONE
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.people_name.text = StringBuilder().apply {
            append(list[position].second)
            list.iterator()
        }.toString()
        holder.itemView.people_id.text = list[position].first
    }

    override fun getItemCount() = list.size

}