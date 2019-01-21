package com.example.tutorapp

import java.util.*

class Constants {

    object User_Profile {
        const val collection_name : String  = "User_Profile"
        const val fname : String  = "fname"
        const val lname : String  = "lname"
        const val tel_no : String  = "tel_no"
        const val user_id : String  = "user_id"
        const val gender : String  = "gender"
        const val role : String  = "role"
        const val email : String  = "email"
    }

    object Account {
        const val collection_name : String  = "Account"
        const val user_id : String  = "user_id"
        const val password : String  = "password"
        const val role : String  = "role"
    }

    object Course {
        const val UUID: String = "UUID"
        const val collection_name : String  = "Course_Data"
        const val name : String  = "course_name"
        const val grade : String = "course_grade"
        const val cat : String  = "course_cat"
        const val pic : String  = "course_pic"
        const val d_start : String  = "course_d_start"
        const val d_end : String  = "course_d_end"
        const val t_start : String  = "course_t_start"
        const val t_end : String  = "course_t_end"
        const val ac_year : String = "ac_year"
        const val dateInWeek : String = "date_in_week"
        const val stu_List : String = "stu_list"
        const val user_id : String = "user_id"
    }

    object Count {
        const val collection_name : String  = "Count"
        val doc_name : String = ("Year_" + Calendar.getInstance()[Calendar.YEAR])
        const val st_count : String = "student_count"
    }

    object CheckIn {
        const val collection_name : String  = "CheckIn"

        const val time: String = "time"
        const val checkList : String = "stu_chk_list"
        const val stuList : String = "stu_list"
        const val UUID: String = "UUID"
    }
}