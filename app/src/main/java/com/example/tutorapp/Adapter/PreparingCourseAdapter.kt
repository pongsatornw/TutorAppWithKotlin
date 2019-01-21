package com.example.tutorapp.Adapter

import android.app.Dialog
import android.content.Context
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tutorapp.DataClass.AddCourseModel
import com.example.tutorapp.Fragment.CourseManagement.RealAddStudentViewPagerFragment
import com.example.tutorapp.R
import kotlinx.android.synthetic.main.alert_course_dialog.*
import kotlinx.android.synthetic.main.view_course.view.*

class PreparingCourseAdapter(private var context: Context,
    private val list: ArrayList<AddCourseModel>  )
    : RecyclerView.Adapter<PreparingCourseAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(viewgriup: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(context)
                .inflate(R.layout.view_course, viewgriup, false)
        val holder = ViewHolder(view)
        Log.d("ADAPTER", position.toString())

        holder.itemView.setOnLongClickListener {v ->

            Dialog(context).apply {
                setContentView(R.layout.alert_course_dialog)
                setCanceledOnTouchOutside(true)
                CompatBtn01.visibility = View.GONE
                CompatBtn02.text = "เพิ่มนักศึกษาเข้ารายวิชา"
                CompatBtn03.text = "ลบรายวิชา"
                CompatBtn01.setOnClickListener {
                    Log.d("Helo", "Halo")
                    dismiss()
                }
                CompatBtn02.setOnClickListener {

                    val model = AddCourseModel( UUID = list[holder.adapterPosition].UUID ,
                            course_name = list[holder.adapterPosition].course_name, course_cat = list[holder.adapterPosition].course_cat ,
                            course_grade = list[holder.adapterPosition].course_grade, course_d_start = list[holder.adapterPosition].course_d_start,
                            course_t_start = list[holder.adapterPosition].course_t_start, course_d_end = list[holder.adapterPosition].course_d_end,
                            course_t_end = list[holder.adapterPosition].course_t_end, course_pic = list[holder.adapterPosition].course_pic,
                            ac_year = list[holder.adapterPosition].ac_year, user_id = list[holder.adapterPosition].user_id,
                            date_study = list[holder.adapterPosition].date_study)

                    val fragment = RealAddStudentViewPagerFragment.newInstance(model)
                    (v.context as FragmentActivity).supportFragmentManager.beginTransaction()
                            //.addToBackStack(null)
                            .replace(R.id.root_main, fragment)
                            .commit()
                    dismiss()
                }
                CompatBtn03.setOnClickListener {
                    dismiss()
                    //TODO
                }
            }.show()



            return@setOnLongClickListener true
        }
        return holder
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
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

    private fun setTitle(holder: PreparingCourseAdapter.ViewHolder) {
        holder.itemView.title01.text = "ชื่อรายวิชา"
        holder.itemView.title02.text = "หมวดหมู่รายวิชา"
        holder.itemView.title03.text = "อาจารย์ผู้สอน"
        holder.itemView.title04.text = "ปีการศึกษา"
    }
}