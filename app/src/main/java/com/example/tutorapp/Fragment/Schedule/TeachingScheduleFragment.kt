package com.example.tutorapp.Fragment.Schedule

import android.content.Context
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.example.tutorapp.*
import com.example.tutorapp.Adapter.PreparingCourseAdapter
import com.example.tutorapp.Utility.FireStoreInitial
import com.google.firebase.firestore.DocumentSnapshot
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.android.synthetic.main.cardview_course_v2.view.*
import kotlinx.android.synthetic.main.fragment_recycler_view.view.*
import kotlinx.android.synthetic.main.fragment_viewpager.view.*
import java.lang.Exception
import java.lang.StringBuilder
import java.util.*

class TeachingScheduleFragment: Fragment() {

    private lateinit var viewManager: LinearLayoutManager
    private lateinit var viewAdapter: StudyingScheduleAdapter
    private lateinit var progressBar: ProgressBar
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate( R.layout.fragment_recycler_view, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareDataToRecyclerView(object: ListCallback<List<*>>{
            override fun onCallback(data: List<*>) {
                val list = data as List<DocumentSnapshot>

                try {
                    viewAdapter.notifyDataSetChanged()
                    viewManager.removeAllViews()
                } catch(ex: Exception) {
                    ex.printStackTrace()
                }
                recyclerView(list)
            }
        })

    }

    private fun prepareDataToRecyclerView(myCallBack: ListCallback<List<*>>) {
        val list = mutableListOf<DocumentSnapshot>()
        Log.d("USER_ID", (activity as MainActivity).getCurrentID())
        view?.prgBar04?.animate()
        view?.prgBar04?.visibility = View.VISIBLE
        FireStoreInitial.initial().collection(Constants.Course.collection_name)
                .whereEqualTo(Constants.Course.user_id, (activity as MainActivity).getCurrentID())
                .get()
                .addOnSuccessListener {
                    Log.d("GetDataSuccess", "Success134679")
                    if(it.documents.size > 0){
                        val time = Date().time + 86400000L // add 1 day from end
                        it.documents.forEach { doc -> if(doc[Constants.Course.d_end] as Long >= time) list.add(doc) }
                        Log.d("GetDataSuccess", "List size is " + list.size)
                        myCallBack.onCallback(data = list)
                    } else {
                        //TODO("Not found data")
                        FancyToast.makeText(context, "ไม่พบรายวิชาที่ทำการสอนในช่วงนี้", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
                    }
                    view?.prgBar04?.clearAnimation()
                    view?.prgBar04?.visibility = View.GONE
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    view?.prgBar04?.clearAnimation()
                    view?.prgBar04?.visibility = View.GONE
                    FancyToast.makeText(context, "การรับข้อมูลจากฐานข้อมูลล้มเหลว", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
                }
                .addOnCanceledListener {
                    view?.prgBar04?.clearAnimation()
                    view?.prgBar04?.visibility = View.GONE
                    FancyToast.makeText(context, "การรับข้อมูลจากฐานข้อมูลถูกยกเลิก", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
                }
        //myCallBack.onCallback(data = list)
    }

    private fun recyclerView(list: List<DocumentSnapshot>) {
        viewManager = LinearLayoutManager(activity)
        viewAdapter = StudyingScheduleAdapter(activity!!, list)

        Log.d("GetDataSuccess", "RecyclerViewCalled")
        view!!.recycler_view.apply {
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }
    }

    companion object {
        fun newInstance(): Fragment {
            val fragment = TeachingScheduleFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    inner class StudyingScheduleAdapter(
            private val context: Context,
            private val list: List<DocumentSnapshot>
    ): RecyclerView.Adapter<StudyingScheduleAdapter.ViewHolder>() {

        inner class ViewHolder(view:View): RecyclerView.ViewHolder(view)

        // Set Button event and others stuff here.
        override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolder {
            val view =  LayoutInflater.from(context).inflate(R.layout.cardview_course_v2, viewGroup, false)
            val viewHolder = ViewHolder(view)
            viewHolder.itemView.ins_name_layout.visibility = View.GONE
            return ViewHolder(view)
        }


        override fun getItemCount() = list.size


        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = holder.itemView
            item.course_name.text = list[position][Constants.Course.name] as String
            item.course_cat.text = list[position][Constants.Course.cat] as String
            item.period.text = StringBuilder().apply {
                append(list[position][Constants.Course.t_start] as String)
                append(" ")
                append(list[position][Constants.Course.t_end] as String)
            }

            val dayInWeek = list[position]["date_in_week"] as MutableMap<String, Boolean>
            //dayInWeek[]
            if(dayInWeek["sun"] as Boolean)
                holder.itemView.sun_txt.setBackgroundResource(R.color.sun_red)
            if(dayInWeek["mon"] as Boolean)
                holder.itemView.mon_txt.setBackgroundResource(R.color.mon_yellow)
            if(dayInWeek["tue"] as Boolean)
                holder.itemView.tue_txt.setBackgroundResource(R.color.tue_pink)
            if(dayInWeek["wed"] as Boolean)
                holder.itemView.wed_txt.setBackgroundResource(R.color.wed_green)
            if(dayInWeek["thr"] as Boolean)
                holder.itemView.thr_txt.setBackgroundResource(R.color.thr_orange)
            if(dayInWeek["fri"] as Boolean)
                holder.itemView.fri_txt.setBackgroundResource(R.color.fri_aqua)
            if(dayInWeek["sat"] as Boolean)
                holder.itemView.sat_txt.setBackgroundResource(R.color.sat_purple)
        }
    }

}