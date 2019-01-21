package com.example.tutorapp.Fragment.CourseManagement

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tutorapp.R
import com.example.tutorapp.Adapter.FragmentRealAddAdapter
import com.example.tutorapp.DataClass.AddCourseModel
import kotlinx.android.synthetic.main.fragment_add_student_to_course_model.view.*

class RealAddStudentViewPagerFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_student_to_course_model, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = this.arguments!!
        val model = args["model"] as AddCourseModel
        view.vp_real.adapter = FragmentRealAddAdapter(fragmentManager!!, model)

    }

    companion object {
        fun newInstance(model: AddCourseModel): Fragment {
            val bundle = Bundle()
            bundle.putParcelable("model", model)
            val fragment = RealAddStudentViewPagerFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}