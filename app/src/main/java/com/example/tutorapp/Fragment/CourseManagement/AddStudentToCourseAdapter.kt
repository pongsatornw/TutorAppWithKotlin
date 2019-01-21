package com.example.tutorapp.Fragment.CourseManagement

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tutorapp.Adapter.PeopleAdapter
import com.example.tutorapp.DataClass.AddPeopleModel
import com.example.tutorapp.R
import kotlinx.android.synthetic.main.cardview_people.view.*
import java.lang.StringBuilder

class AddStudentToCourseAdapter(private val context: Context,
    private val list: ArrayList<AddPeopleModel>): RecyclerView.Adapter<AddStudentToCourseAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(viewgroup: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.cardview_people, viewgroup, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.people_name.text = StringBuilder().apply {
            append(list[position].fname)
            append(" ")
            append(list[position].lname)
        }.toString()
        holder.itemView.people_id.text = list[position].user_id
        holder.itemView.people_gender.text = list[position].gender
    }
}