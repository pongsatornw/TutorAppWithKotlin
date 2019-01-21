package com.example.tutorapp.DataClass

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class MiniCourseModel(
        val UUID: String,
        val course_name: String,
        val course_cat: String,
        val course_grade: String,
        val course_d_start: Long,
        val course_t_start: String,
        val course_d_end: Long,
        val course_t_end: String,
        val course_pic: String,
        val user_id: String,
        val ac_year: String,
        val date_study: MutableMap<String, Boolean>) : Parcelable