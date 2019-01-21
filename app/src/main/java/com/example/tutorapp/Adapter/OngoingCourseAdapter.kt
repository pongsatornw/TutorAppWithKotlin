package com.example.tutorapp.Adapter

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tutorapp.DataClass.AddCourseModel
import com.example.tutorapp.Constants
import com.example.tutorapp.DataClass.AddPeopleModel
import com.example.tutorapp.Fragment.PeopleManagement.AddPeopleFragment
import com.example.tutorapp.R
import com.example.tutorapp.Utility.FireStoreInitial
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.alert_course_dialog.*
import kotlinx.android.synthetic.main.view_course.view.*

class OngoingCourseAdapter(private val context: Context,
   private val list: ArrayList<AddCourseModel>): RecyclerView.Adapter<OngoingCourseAdapter.ViewHolder>()  {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(viewgroup: ViewGroup, p1: Int): OngoingCourseAdapter.ViewHolder {
        val view = LayoutInflater.from(context)
                .inflate(R.layout.view_course, viewgroup, false)
        val holder = ViewHolder(view)

        holder.itemView.setOnLongClickListener {
            Dialog(context).apply {
                setContentView(R.layout.alert_course_dialog)
                setCanceledOnTouchOutside(true)
                CompatBtn01.visibility = View.GONE
                CompatBtn02.text = "เพิ่มนักศึกษาเข้ารายวิชา"
                CompatBtn03.text = "ยกเลิก"
                CompatBtn02.setOnClickListener {
                    dismiss()

                    FireStoreInitial.initial()
                            .collection(Constants.Course.collection_name)
                            .document()

                }
                CompatBtn03.setOnClickListener {
                    dismiss()
                }
            }.show()
            return@setOnLongClickListener true
        }
        return holder
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: OngoingCourseAdapter.ViewHolder, position: Int) {
        setTitle(holder = holder)
        val curholder = holder.itemView
        // course_name
        curholder.content01.text = list[position].course_name
        // course_cat
        curholder.content02.text = list[position].course_cat
        // course_pic
        curholder.content03.text = list[position].course_pic
        // ac_year
        curholder.content04.text = list[position].ac_year
    }

    private fun setTitle(holder: OngoingCourseAdapter.ViewHolder) {
        holder.itemView.title01.text = "ชื่อรายวิชา"
        holder.itemView.title02.text = "หมวดหมู่รายวิชา"
        holder.itemView.title03.text = "อาจารย์ผู้สอน"
        holder.itemView.title04.text = "ปีการศึกษา"
    }
}