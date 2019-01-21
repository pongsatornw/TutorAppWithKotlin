package com.example.tutorapp.Service

import android.app.IntentService
import android.content.Intent
import android.icu.text.DateFormat
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.example.tutorapp.Constants
import com.example.tutorapp.DataClass.AddCourseModel
import com.example.tutorapp.Utility.FireStoreInitial
import com.google.firebase.firestore.SetOptions
import com.shashank.sony.fancytoastlib.FancyToast
import java.util.*


class FirestoreEditCourseService: IntentService("FirestoreEditCourseService") {
    private val TAG = javaClass.simpleName


    override fun setIntentRedelivery(enabled: Boolean) {
        super.setIntentRedelivery(true)
    }

    override fun onHandleIntent(intent: Intent?) {
        val db = FireStoreInitial.initial()
        val data = mutableMapOf<String, Any>()
        val dateInWeek = mutableMapOf<String, Boolean>()
        val courseModel = intent!!.getParcelableExtra<AddCourseModel>("course_model")
        val docID = intent.getStringExtra("documentID")
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

        Log.d("DOC ID is", docID + " ")
        db.collection("Course_Data")
                .document(docID)
                .set(data, SetOptions.mergeFields(listOf(Constants.Course.ac_year
                , Constants.Course.cat, Constants.Course.d_end, Constants.Course.d_start
                , Constants.Course.t_end, Constants.Course.t_start, Constants.Course.name
                , Constants.Course.dateInWeek)) )
                .addOnCompleteListener {
                    Log.d("Edit Data", "Complete")
                    if(it.isSuccessful) {
                        FancyToast.makeText(applicationContext, "การแก้ไขข้อมูลรายวิชาสำเร็จ", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()

                        Log.d("Edit Data", "Successful")
                    } else Log.d("Edit Data", "Fail ")
                }
                .addOnFailureListener{FancyToast.makeText(applicationContext, "การแก้ไขข้อมูลรายวิชาล้มเหลว กรุณาลองใหม่ในภายหลัง", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()

                    Log.d("Edit Data", "Failure")
                }
        /*db.collection("Course_Data")
                .whereEqualTo("course_name", courseModel.course_name)
                .get()
                .addOnSuccessListener {
                    if(it.isEmpty) {
                        db.collection("Course_Data")
                                .add(data)
                                .addOnSuccessListener {
                                    documentReference ->
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.id)
                                }
                                .addOnFailureListener {
                                    e ->
                                    Log.w(TAG, "Error adding document", e)
                                }
                    } else {
                        Toast.makeText(application, "พบรายวิชายี้อยู่ในฐานข้อมูอมูลแล้ว", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    e ->
                    Log.w(TAG, "Error adding document", e)
                }*/


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