package com.example.tutorapp.Fragment.CourseManagement

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tutorapp.Adapter.ShowCurrentAdapter
import com.example.tutorapp.Constants
import com.example.tutorapp.DataClass.AddCourseModel
import com.example.tutorapp.R
import com.example.tutorapp.Utility.FireStoreInitial
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_curr_student.view.*

class ShowCurrentStudent: Fragment() {

    private lateinit var viewAdapter: ShowCurrentAdapter
    private lateinit var viewManager: LinearLayoutManager
    private lateinit var UUID: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_curr_student, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = this.arguments!!
        UUID = (args["model"] as AddCourseModel).UUID
        updateRecyclerView(view, UUID)

        view.btn_update.setOnClickListener {
            updateRecyclerView(view, UUID)
        }

    }

    private fun updateRecyclerView(view: View, uuid: String) {
        view.prgStudent.animate()
        view.prgStudent.visibility = View.VISIBLE
        FireStoreInitial.initial()
                .collection(Constants.Course.collection_name)
                .whereEqualTo("UUID",uuid)
                .limit(1)
                .get()
                .addOnCompleteListener {
                    if(it.isSuccessful) {
                        if(it.result!!.size() > 0 ) {
                            val list = mutableListOf<Pair<String, String>>()
                            list.clear()
                            val doc = it.result!!.documents[0]
                            val maps = doc["stu_list"] as MutableMap<String, String>
                            for (map in maps) {
                                list.add(Pair(map.key, map.value) )
                            }
                            list.sortBy {pair -> pair.first}
                            if(list.size > 0)
                                recycleView(view, list = list)
                            else {
                                //TODO
                            }
                        }
                        view.prgStudent.clearAnimation()
                        view.prgStudent.visibility = View.GONE
                    } else {
                        view.prgStudent.clearAnimation()
                        view.prgStudent.visibility = View.GONE
                    }
                }
    }

    private fun recycleView(view1: View, list: MutableList<Pair<String, String>>) {

        try {
            viewAdapter.notifyDataSetChanged()
            viewManager.removeAllViews()

        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        viewManager = LinearLayoutManager(activity)
        viewAdapter = ShowCurrentAdapter(context = activity!!, list = list)

        view1.recycler_curr_stu.apply {
            setHasFixedSize(true)

            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    companion object {
        fun newInstance(model: AddCourseModel): Fragment {
            val bundle = Bundle()
            bundle.putParcelable("model", model)
            val fragment = ShowCurrentStudent()
            fragment.arguments = bundle
            return fragment
        }
    }

}