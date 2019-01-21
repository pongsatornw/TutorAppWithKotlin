package com.example.tutorapp.Fragment.CourseManagement.ViewCourse

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tutorapp.Adapter.OngoingCourseAdapter
import com.example.tutorapp.DataClass.AddCourseModel
import com.example.tutorapp.R
import kotlinx.android.synthetic.main.fragment_recycle_ongoing.view.*
import java.lang.Exception

class OngoingCourseFragment: Fragment() {

    private lateinit var fragmentView: View
    private lateinit var viewManager: LinearLayoutManager
    private lateinit var viewAdapter: OngoingCourseAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentView = inflater.inflate(R.layout.fragment_recycle_ongoing, container, false)
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val bundle = this.arguments!!
        val list = bundle.getParcelableArrayList<AddCourseModel>("ongList")
        Log.d("OngList size: ", list.size.toString())
        try {
            viewAdapter.notifyDataSetChanged()
            viewManager.removeAllViews()
        } catch(ex: Exception) {
            ex.printStackTrace()
        }

        recycleViewCall(list as ArrayList<AddCourseModel>)
    }

    private fun recycleViewCall(map: ArrayList<AddCourseModel>) {

        viewManager = LinearLayoutManager(activity)
        viewAdapter = OngoingCourseAdapter(activity!!, map)

        view!!.recycle_ongoing.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }
    }
    companion object {
        fun newInstance(ongList: ArrayList<AddCourseModel>): Fragment {
            val bundle = Bundle()
            bundle.putParcelableArrayList("ongList", ongList)
            val fragment = OngoingCourseFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

}