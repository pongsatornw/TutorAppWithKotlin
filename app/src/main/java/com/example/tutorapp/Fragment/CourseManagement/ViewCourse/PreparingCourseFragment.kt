package com.example.tutorapp.Fragment.CourseManagement.ViewCourse

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tutorapp.Adapter.PreparingCourseAdapter
import com.example.tutorapp.DataClass.AddCourseModel
import com.example.tutorapp.R
import kotlinx.android.synthetic.main.fragment_recycle_prepare.view.*
import java.lang.Exception

class PreparingCourseFragment: Fragment() {

    private lateinit var viewManager: LinearLayoutManager
    private lateinit var viewAdapter: PreparingCourseAdapter
    private var myRoot: View? = null
    private var savedState: Bundle? = null
    private lateinit var list: ArrayList<AddCourseModel>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if(myRoot == null) {
            myRoot = inflater.inflate(R.layout.fragment_recycle_prepare, container, false)
        }
        if(savedInstanceState != null && savedState == null) {
            savedState = savedInstanceState.getBundle("STAV")
        }
        if(savedState != null) {
            list = savedState!!["list"] as ArrayList<AddCourseModel>
        }
        savedState = null

        return myRoot
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val bundle = this.arguments!!
        list = bundle.getParcelableArrayList<AddCourseModel>("prepList") as ArrayList<AddCourseModel>
        Log.d("PrepList size: ", list.size.toString())
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
        viewAdapter = PreparingCourseAdapter(activity!!, map)

        view!!.recycle_prepare.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        savedState = saveState()
    }

    private fun saveState(): Bundle {
        val bundle = Bundle()
        bundle.putParcelableArrayList("list", list)
        return bundle
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBundle("STAV", if (savedState != null) savedState else saveState())
    }

    companion object {
        fun newInstance(prepList: ArrayList<AddCourseModel>): Fragment {
            val bundle = Bundle()
            Log.d("PrepList in NewInstance", prepList.size.toString())
            bundle.putParcelableArrayList("prepList", prepList)
            val fragment = PreparingCourseFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

}