package com.example.tutorapp.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.synthetic.main.cardview_course_v2.view.*
import com.google.firebase.firestore.FirebaseFirestore
import android.view.*
import android.view.ViewGroup
import android.app.Dialog
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.example.tutorapp.R
import com.example.tutorapp.Constants
import com.example.tutorapp.DataClass.AddCourseModel
import com.example.tutorapp.DataClass.AddCourseModelExtend
import com.example.tutorapp.Fragment.CourseManagement.AddCourse.AddCourseFragment
import com.example.tutorapp.Fragment.CourseManagement.SelectCourseToAddStudentFragment
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.alert_course_dialog.*
import java.lang.Exception

class CourseAdapter(private val context: Context, private val list: MutableList<DocumentSnapshot>
        , private val focus: Int) : RecyclerView.Adapter<CourseAdapter.ViewHolder>() {

    private var minHeight:  Int = 0

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): CourseAdapter.ViewHolder {

        val view = LayoutInflater.from(context)
                .inflate(R.layout.cardview_course_v2, parent, false)
        val holder = ViewHolder(view)

        when (focus) {
            0 -> {
                holder.itemView.viewTreeObserver
                holder.itemView.ins_name_layout.visibility = View.VISIBLE
                holder.itemView.course_cat_layout.visibility = View.VISIBLE
            }
            1 -> {
                holder.itemView.course_cat_layout.visibility = View.GONE
                holder.itemView.ins_name_layout.visibility = View.VISIBLE
            }
            2 -> {
                holder.itemView.ins_name_layout.visibility = View.GONE
                holder.itemView.course_cat_layout.visibility = View.VISIBLE
            }
            3 -> {
                holder.itemView.ins_name_layout.visibility = View.VISIBLE
                holder.itemView.course_cat_layout.visibility = View.VISIBLE
            }
            else -> Log.wtf("Terrible Failure", "How this even happen!?")
        }


        holder.itemView.setOnLongClickListener{ v ->

            Dialog(context).apply {
                setContentView(R.layout.alert_course_dialog)
                setCanceledOnTouchOutside(true)
                CompatBtn01.text = "เพิ่มนักศึกษาเข้ารายวิชา"
                CompatBtn02.text = "แก้ไขรายวิชา"
                CompatBtn03.text = "ลบรายวิชา"
                CompatBtn01.visibility = View.GONE
                CompatBtn01.setOnClickListener {
                    val doc = list[holder.adapterPosition]

                        val model = AddCourseModelExtend(
                                UUID = doc["UUID"] as String,
                                course_name = doc["course_name"] as String,
                                course_cat = doc["course_cat"] as String,
                                course_grade = doc[Constants.Course.grade] as String,
                                course_d_start = doc[Constants.Course.d_start] as Long,
                                course_t_start = doc[Constants.Course.t_start] as String,
                                course_d_end = doc[Constants.Course.d_end] as Long,
                                course_t_end = doc[Constants.Course.t_end] as String,
                                course_pic = doc[Constants.Course.pic] as String,
                                user_id = doc[Constants.Course.user_id] as String,
                                ac_year = doc[Constants.Course.ac_year] as String,
                                date_study = doc[Constants.Course.dateInWeek] as MutableMap<String, Boolean>,
                                stu_list = doc[Constants.Course.stu_List] as MutableMap<String, String>
                                )

                    //TODO("อย่าลืมความจริงที่เธอเคยทิ้งเราไป")
                    val fragment = SelectCourseToAddStudentFragment.newInstance()
                    fragment.arguments = Bundle().apply {
                        putParcelable("model", model)
                    }
                    (v.context as FragmentActivity).supportFragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.root_main, fragment)
                            .commit()
                    dismiss()
                }
                CompatBtn02.setOnClickListener {
                    val args = Bundle()

                    val model = AddCourseModel( list[holder.adapterPosition]["UUID"] as String,
                            list[holder.adapterPosition]["course_name"] as String, list[holder.adapterPosition]["course_cat"] as String,
                            list[holder.adapterPosition]["course_grade"] as String, list[holder.adapterPosition]["course_d_start"] as Long,
                            list[holder.adapterPosition]["course_t_start"] as String, list[holder.adapterPosition]["course_d_end"] as Long,
                            list[holder.adapterPosition]["course_t_end"] as String, list[holder.adapterPosition]["course_pic"] as String,
                            list[holder.adapterPosition]["user_id"] as String,list[holder.adapterPosition]["ac_year"] as String,
                            list[holder.adapterPosition]["date_in_week"] as MutableMap<String, Boolean>)
                    args.putParcelable("course_data", model)
                    args.putString("documentID", list[holder.adapterPosition].id)

                    Log.d("DOC ID", list[holder.adapterPosition].id)

                    val fragment = AddCourseFragment.newInstance()
                    fragment.arguments = args
                    (v.context as FragmentActivity).supportFragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.root_main, fragment)
                            .commit()
                    dismiss()
                }
                CompatBtn03.setOnClickListener {
                    dismiss()
                    FirebaseFirestore.getInstance().collection("Course_Data")
                            .document(list[holder.adapterPosition].id).delete().apply {
                                addOnCompleteListener { task ->
                                    if (task.isComplete){
                                        Log.d("Complete", "Task complete")
                                    }
                                    if (task.isSuccessful) {
                                        Log.d("Complete", "Task complete and Success")
                                    } else {
                                        Log.d("Complete", "Task complete but not success")
                                    }
                                }
                                addOnCanceledListener {
                                    Log.d("Cancel", "Delete data canceled")
                                }
                                addOnFailureListener { ex ->
                                    ex.printStackTrace()
                                }
                            }
                }
            }.show()

            /*LovelyStandardDialog(context).apply {
                setIcon(R.drawable.info_icon)
                setTopColorRes(R.color.colorDarkBlue)
                setMessage("ท่านต้องการดำเนินการใดกับรายวิชานี้ใช่หรือไม่")
                setPositiveButton(R.string.cancel) {
                    dismiss()
                }
                setNeutralButton(R.string.edit)  {
                    dismiss()
                    Log.d("JEE", "PREPARE TO EDIT DATA")
                    // TODO Edit Data
                }
                setNegativeButton("ลบข้อมูลรายวิชาปัจจุบัน") {
                    LovelyStandardDialog(context).apply {
                        setIcon(R.drawable.alert_icon)
                        setMessage("ท่านแน่ใจหรือไม่ว่าต้องการลบข้อมูลรายการนี้")
                        setNegativeButton(R.string.cancel){ dismiss() }
                        setPositiveButton(R.string.delete){ dismiss()
                            FirebaseFirestore.getInstance().collection("Course_Data")
                            .document(list[position].id).delete().apply {
                                addOnCompleteListener { task ->
                                    if (task.isComplete){
                                        Log.d("Complete", "Task complete")
                                    }
                                    if (task.isSuccessful) {
                                        Log.d("Complete", "Task complete and Success")
                                    } else {
                                        Log.d("Complete", "Task complete but not success")
                                    }
                                }
                                addOnCanceledListener {
                                    Log.d("Cancel", "Delete data canceled")
                                }
                                addOnFailureListener { ex ->
                                    ex.printStackTrace()
                                }
                            }
                        }

                    }.show()
                }

            }.show()*/
            return@setOnLongClickListener true
        }/*
        holder.itemView.course_name.text = list[holder.adapterPosition]["course_name"] as String
        holder.itemView.course_cat.text = list[holder.adapterPosition]["course_cat"] as String
        holder.itemView.ins_name.text = list[holder.adapterPosition]["course_pic"] as String
        //holder.view.period.text = list[position]["asd"] as String
        val dayInWeek = list[holder.adapterPosition]["date_in_week"] as MutableMap<String, Boolean>
        //dayInWeek[]
        if(dayInWeek["sun"] as Boolean)
            holder.itemView.sun_txt.setBackgroundResource(R.color.sun_red)
        if(dayInWeek["mon"] as Boolean)
            holder.itemView.mon_txt.setBackgroundResource(R.color.mon_yellow)
        if(dayInWeek["tue"] as Boolean)
            holder.itemView.tue_txt.setBackgroundResource(R.color.tue_pink)
        if(dayInWeek["wed"] as Boolean)
            holder.itemView.wed_txt.setBackgroundResource(R.color.wed_green)
        if(dayInWeek["thr"] as Boolean)
            holder.itemView.thr_txt.setBackgroundResource(R.color.thr_orange)
        if(dayInWeek["fri"] as Boolean)
            holder.itemView.fri_txt.setBackgroundResource(R.color.fri_aqua)
        if(dayInWeek["sat"] as Boolean)
            holder.itemView.sat_txt.setBackgroundResource(R.color.sat_purple)*/

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.course_name.text = list[position]["course_name"] as String
        holder.itemView.course_cat.text = list[position]["course_cat"] as String
        holder.itemView.ins_name.text = list[position]["course_pic"] as String
        holder.itemView.txt_asd.text = "ปีการศึกษา"
        holder.itemView.period.text = list[position][Constants.Course.ac_year] as String
        //holder.view.period.text = list[position]["asd"] as String
        val dayInWeek = list[position]["date_in_week"] as MutableMap<String, Boolean>
        //dayInWeek[]
        if(dayInWeek["sun"] as Boolean)
            holder.itemView.sun_txt.setBackgroundResource(R.color.sun_red)
        if(dayInWeek["mon"] as Boolean)
            holder.itemView.mon_txt.setBackgroundResource(R.color.mon_yellow)
        if(dayInWeek["tue"] as Boolean)
            holder.itemView.tue_txt.setBackgroundResource(R.color.tue_pink)
        if(dayInWeek["wed"] as Boolean)
            holder.itemView.wed_txt.setBackgroundResource(R.color.wed_green)
        if(dayInWeek["thr"] as Boolean)
            holder.itemView.thr_txt.setBackgroundResource(R.color.thr_orange)
        if(dayInWeek["fri"] as Boolean)
            holder.itemView.fri_txt.setBackgroundResource(R.color.fri_aqua)
        if(dayInWeek["sat"] as Boolean)
            holder.itemView.sat_txt.setBackgroundResource(R.color.sat_purple)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = list.size

}