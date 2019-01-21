package com.example.tutorapp.Fragment.CourseManagement

import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tutorapp.DataClass.AddPeopleModel
import com.example.tutorapp.R
import kotlinx.android.synthetic.main.fragment_add_student_tocourse_vp.view.*

class StudentInCourseFragment: Fragment() {

    private lateinit var fragmentView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentView = inflater.inflate( R.layout.fragment_add_student_tocourse_vp, container, false)
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = this.arguments!!

        view.vp.adapter = StudentInCourseViewPager(fragmentManager!!,
                bundle["list"] as ArrayList<AddPeopleModel>)

    }

    companion object {
        fun newInstance(list: ArrayList<AddPeopleModel>): Fragment {
            val fragment = StudentInCourseFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList("list", list as java.util.ArrayList<out Parcelable>)
            return fragment
        }
    }
}