package com.example.tutorapp.Fragment.Schedule

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tutorapp.*
import com.example.tutorapp.Utility.FireStoreInitial
import com.google.firebase.firestore.DocumentSnapshot
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.android.synthetic.main.cardview_course_v2.view.*
import kotlinx.android.synthetic.main.fragment_recycler_view.view.*
import java.lang.StringBuilder
import java.util.*
import kotlin.Exception

class StudyingScheduleFragment: Fragment() {

    private lateinit var viewManager: LinearLayoutManager
    private lateinit var viewAdapter: StudyingScheduleAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate( R.layout.fragment_recycler_view, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.prgBar04.visibility = View.GONE
        view.prgBar04?.clearAnimation()
        prepareDataToRecyclerView(object: ListCallbackWithErrorHandler<List<*>, Exception> {
            override fun onCallback(data: List<*>) {
                try {
                    viewAdapter.notifyDataSetChanged()
                    viewManager.removeAllViews()
                } catch(ex: Exception) {
                    ex.printStackTrace()
                }
                recyclerView(data as List<DocumentSnapshot>)
            }

            override fun onErrorCallback(exception: Exception) {
                FancyToast.makeText(context, "เกิดความผิดพลากในการร้องขอข้อมูลจากฐานข้อมูล กรุณรลองใหม่ในภายหลัง", Toast.LENGTH_SHORT, FancyToast.ERROR, false).show()
            }

            override fun onEmptyCallback() {
                FancyToast.makeText(context, "ไม่พบรายวิชาที่ทำการเรียนในช่วงเวลาปัจจุบัน", Toast.LENGTH_SHORT, FancyToast.CONFUSING, false).show()
            }
        })

    }

    private fun prepareDataToRecyclerView(myCallBack: ListCallbackWithErrorHandler<List<*>, Exception>) {
        val list = mutableListOf<DocumentSnapshot>()
        list.clear()
        view?.prgBar04?.visibility = View.VISIBLE
        view?.prgBar04?.animate()
        FireStoreInitial.initial().collection(Constants.Course.collection_name)
                .whereGreaterThan(Constants.Course.d_end, Date().time)
                .get()
                .addOnSuccessListener {
                    if(it.size() > 0){
                        it.documents.forEach{
                            if((it[Constants.Course.stu_List] as Map<String, String>).containsKey((activity as MainActivity).getCurrentID())){
                                list.add(it)
                            }
                        }
                        myCallBack.onCallback(list)
                        view?.prgBar04?.visibility = View.GONE
                        view?.prgBar04?.clearAnimation()
                    } else {
                        myCallBack.onEmptyCallback()
                        view?.prgBar04?.visibility = View.GONE
                        view?.prgBar04?.clearAnimation()
                    }
                }
                .addOnFailureListener {
                    myCallBack.onErrorCallback(it)
                    view?.prgBar04?.visibility = View.GONE
                    view?.prgBar04?.clearAnimation()
                }
    }

    private fun recyclerView(list: List<DocumentSnapshot>) {
        viewManager = LinearLayoutManager(activity)
        viewAdapter = StudyingScheduleAdapter(activity!!, list)

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
            val fragment = StudyingScheduleFragment()
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
            val view =  LayoutInflater.from(context).inflate(R.layout.cardview_course_v2, viewGroup)
            val viewHolder = ViewHolder(view)
            viewHolder.itemView.ins_name_layout.visibility = View.GONE
            return viewHolder
        }


        override fun getItemCount() = list.size


        override fun onBindViewHolder(p0: ViewHolder, position: Int) {
            val item = p0.itemView
            item.course_name.text = list[position][Constants.Course.name] as String
            item.course_cat.text = list[position][Constants.Course.cat] as String
            item.period.text = StringBuilder().apply {
                append(list[position][Constants.Course.t_start] as String)
                append(" ")
                append(list[position][Constants.Course.t_end] as String)
            }

            val dayInWeek = list[position]["date_in_week"] as MutableMap<String, Boolean>

            if(dayInWeek["sun"] as Boolean)
                item.sun_txt.setBackgroundResource(R.color.sun_red)
            if(dayInWeek["mon"] as Boolean)
                item.mon_txt.setBackgroundResource(R.color.mon_yellow)
            if(dayInWeek["tue"] as Boolean)
                item.tue_txt.setBackgroundResource(R.color.tue_pink)
            if(dayInWeek["wed"] as Boolean)
                item.wed_txt.setBackgroundResource(R.color.wed_green)
            if(dayInWeek["thr"] as Boolean)
                item.thr_txt.setBackgroundResource(R.color.thr_orange)
            if(dayInWeek["fri"] as Boolean)
                item.fri_txt.setBackgroundResource(R.color.fri_aqua)
            if(dayInWeek["sat"] as Boolean)
                item.sat_txt.setBackgroundResource(R.color.sat_purple)
        }
    }

}