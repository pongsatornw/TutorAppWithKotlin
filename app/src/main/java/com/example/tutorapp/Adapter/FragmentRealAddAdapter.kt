package com.example.tutorapp.Adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.example.tutorapp.DataClass.AddCourseModel
import com.example.tutorapp.Fragment.CourseManagement.RealAddFragment
import com.example.tutorapp.Fragment.CourseManagement.ShowCurrentStudent

class FragmentRealAddAdapter(fm: FragmentManager, private val model1: AddCourseModel): FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> RealAddFragment.newInstance(model1)
            1 -> ShowCurrentStudent.newInstance(model1)
            else -> throw Exception("AAAAAAAAA")
        }
    }

    override fun getCount(): Int {
        return 2
    }
}