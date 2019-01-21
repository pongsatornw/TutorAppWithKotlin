package com.example.tutorapp.Fragment.CourseManagement

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.example.tutorapp.DataClass.AddPeopleModel

class StudentInCourseViewPager(fm: FragmentManager,
   private val list: ArrayList<AddPeopleModel>
): FragmentStatePagerAdapter(fm) {

    override fun getCount() = 2

    override fun getItem(p0: Int): Fragment {
        return when(p0) {
            0 -> Fragment() // TODO
            1 -> StudentInCourseFragment.newInstance(list)
            else -> throw Exception("")
        }
    }

}