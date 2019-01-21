package com.example.tutorapp.Fragment.CourseManagement

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tutorapp.DataClass.AddCourseModel
import com.example.tutorapp.Fragment.CourseManagement.ViewCourse.CourseViewPagerAdapter
import com.example.tutorapp.R
import kotlinx.android.synthetic.main.fragment_addstudent_to_course.view.*

class AddStudentToCourseFragment: Fragment() {

    private lateinit var fragmentView: View
    private lateinit var bundle: Bundle
    private lateinit var list: ArrayList<AddCourseModel>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentView = inflater.inflate(R.layout.fragment_addstudent_to_course, container, false)
        bundle = this.arguments!!
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.add_student_to_course_viewpager.adapter = CourseViewPagerAdapter(fragmentManager!!
                , bundle.getParcelableArrayList("prepList"), bundle.getParcelableArrayList("ongList"))

    }

    companion object {
        fun newInstance(prepList: ArrayList<AddCourseModel>,
            ongList: ArrayList<AddCourseModel>): Fragment {
            val bundle = Bundle()
            bundle.putParcelableArrayList("prepList", prepList)
            bundle.putParcelableArrayList("ongList", ongList)
            val fragment = AddStudentToCourseFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

}