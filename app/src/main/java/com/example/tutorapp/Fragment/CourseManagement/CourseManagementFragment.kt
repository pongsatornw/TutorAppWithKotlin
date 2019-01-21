package com.example.tutorapp.Fragment.CourseManagement

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tutorapp.Fragment.CourseManagement.AddCourse.AddCourseFragment
import com.example.tutorapp.R
import kotlinx.android.synthetic.main.fragment_course.*

class CourseManagementFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_course, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bt_viewcourse.setOnClickListener {
            fragmentManager!!.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.root_main, ViewCourseFragment.newInstance())
                    .commit()
        }

        bt_viewcourse3.setOnClickListener {
            fragmentManager!!.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.root_main, SelectCourseToAddStudentFragment.newInstance())
                    .commit()
        }

        bt_addcourse.setOnClickListener {
            fragmentManager!!.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.root_main,  AddCourseFragment.newInstance())
                    .commit()
        }

    }

    companion object {
        fun newInstance(): Fragment {
            return CourseManagementFragment()
        }

    }

}