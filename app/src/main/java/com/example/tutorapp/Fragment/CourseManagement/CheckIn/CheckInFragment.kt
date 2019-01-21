package com.example.tutorapp.Fragment.CourseManagement.CheckIn

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tutorapp.R
import com.example.tutorapp.UniversalViewPager
import kotlinx.android.synthetic.main.fragment_viewpager.view.*

class CheckInFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_viewpager, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments!!
        val fragmentList = mutableListOf(
                CheckInRealFragment.newInstance(bundle["course_name"]!!,
                        bundle["sheet"]!!, bundle["doc_id"]!!, bundle["UUID"]!!),
                CheckInDataFragment.newInstance()
        )
        view.viewpager.adapter = UniversalViewPager(fragmentManager!!, fragmentList)

    }

    companion object {
        fun newInstance(course_name: String, sheet: Int, doc_id: String, uuid: String): Fragment {
            val fragment = CheckInFragment()
            val bundle = Bundle()
            bundle.putString("course_name", course_name)
            bundle.putString("doc_id", doc_id)
            bundle.putString("UUID", uuid)
            bundle.putInt("sheet", sheet)
            Log.d("course_name", course_name)
            Log.d("docID", doc_id)
            Log.d("UUID", uuid)
            Log.d("Sheet", sheet.toString())
            fragment.arguments = bundle
            return fragment
        }
    }

}