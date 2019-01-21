package com.example.tutorapp.Service

import android.app.IntentService
import android.content.Intent
import android.icu.text.DateFormat
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.example.tutorapp.DataClass.AddCourseModel
import com.example.tutorapp.R
import com.example.tutorapp.Utility.FireStoreInitial
import java.util.*


class FirestoreAddCourseService: IntentService("FirestoreAddCourseService") {
    private val TAG = javaClass.simpleName


    override fun setIntentRedelivery(enabled: Boolean) {
        super.setIntentRedelivery(true)
    }

    override fun onHandleIntent(intent: Intent?) {
        val db = FireStoreInitial.initial()
        val data = mutableMapOf<String, Any>()
        val dateInWeek = mutableMapOf<String, Boolean>()
        val courseModel = intent!!.getParcelableExtra<AddCourseModel>("course_model")

        data["UUID"] = UUID.randomUUID().toString()
        data["course_name"] = courseModel.course_name
        data["course_cat"] = courseModel.course_cat
        data["course_grade"] = courseModel.course_grade
        data["course_d_start"] = courseModel.course_d_start
        data["course_d_end"] = courseModel.course_d_end
        data["course_t_start"] = courseModel.course_t_start
        data["course_t_end"] = courseModel.course_t_end
        data["course_pic"] = courseModel.course_pic
        data["date_in_week"] = courseModel.date_study
        data["ac_year"] = courseModel.ac_year
        data["user_id"] = courseModel.user_id
        data["stu_list"] = mutableMapOf<String, String>()

        db.collection("Course_Data")
                .whereEqualTo("course_name", courseModel.course_name)
                .whereEqualTo("ac_year", courseModel.ac_year)
                .get()
                .addOnSuccessListener {
                    if(it.isEmpty) {
                        db.collection("Course_Data")
                                .add(data)
                                .addOnSuccessListener {
                                    documentReference ->
                                    Toast.makeText(applicationContext, "การเพิ่มข้อมูลรายวิชาสำเร็จ", Toast.LENGTH_SHORT).show()

                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.id)
                                }
                                .addOnFailureListener {
                                    e ->
                                    Log.w(TAG, "Error adding document", e)
                                }
                    } else {
                        Toast.makeText(application, "พบรายวิชานี้อยู่ในฐานข้อมูลแล้ว", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    e ->
                    Toast.makeText(applicationContext, "เกิดความผิดพลาดระหว่างการเพิ่มข้อมูลบุคลากร", Toast.LENGTH_SHORT).show()

                    Log.w(TAG, "Error adding document", e)
                }


    }

    // Convert date in string format to date
    private fun dateTxtToDate(txtTime: String): Date{

        return if( Build.VERSION.SDK_INT < 24) {
            val dateFormat = java.text.DateFormat.getDateInstance(java.text.DateFormat.DEFAULT)
            dateFormat.parse(txtTime)
        }
        else {
            val dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT)

            dateFormat.parse(txtTime)
        }
    }

    private fun timeTxtToDate(txtTime: String){

        if( Build.VERSION.SDK_INT < 24) {

        } else {

        }
    }
}