package com.example.tutorapp.Fragment.CourseManagement.ViewCourse

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.example.tutorapp.DataClass.AddCourseModel
import java.lang.Exception

class CourseViewPagerAdapter(fm: FragmentManager,
    private val prepList: ArrayList<AddCourseModel>,
    private val ongList: ArrayList<AddCourseModel>): FragmentStatePagerAdapter(fm) {

    override fun getItem(p0: Int): Fragment {
        return when(p0) {
            0 -> PreparingCourseFragment.newInstance(prepList)
            1 -> OngoingCourseFragment.newInstance(ongList)
            else -> throw Exception("How this called!?")
        }
    }

    override fun getCount(): Int {
        return 2
    }
}