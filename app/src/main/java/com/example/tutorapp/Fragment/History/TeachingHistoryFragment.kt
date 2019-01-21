package com.example.tutorapp.Fragment.History

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tutorapp.*
import com.example.tutorapp.DataClass.AddCourseModelExtend
import com.example.tutorapp.Utility.FireStoreInitial
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.synthetic.main.cardview_universal.view.*
import kotlinx.android.synthetic.main.fragment_recycler_view.view.*
import java.lang.Exception

class TeachingHistoryFragment: Fragment() {

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var historyAdapter: TeachingCourseAdapter
    private lateinit var lstMAP: MutableList<DocumentSnapshot>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        lstMAP = mutableListOf()
        layoutManager = LinearLayoutManager(activity)
        historyAdapter = TeachingCourseAdapter(activity!!, lstMAP)
        return inflater.inflate(R.layout.fragment_recycler_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.prgBar04.visibility = View.GONE
        view.prgBar04.clearAnimation()
        checkTeachingCourse()
    }

    private fun checkTeachingCourse() {
        view?.prgBar04?.visibility = View.VISIBLE
        view?.prgBar04?.animate()
        FireStoreInitial.initial().collection(Constants.Course.collection_name)
                .whereEqualTo(Constants.Course.user_id, (activity as MainActivity).getCurrentID())
                .get()
                .addOnSuccessListener {
                    if(it.size() > 0) {
                        Log.d("ABC", "Size > 0")
                        try {
                            lstMAP.clear()
                            layoutManager.removeAllViews()
                            historyAdapter.notifyDataSetChanged()
                        } catch(ex: Exception) {
                            ex.printStackTrace()
                        }
                        lstMAP = mutableListOf<DocumentSnapshot>()
                        lstMAP.addAll(it.documents)
                        mCallback.onCallback(lstMAP)
                        view?.prgBar04?.visibility = View.GONE
                        view?.prgBar04?.clearAnimation()
                    } else {Log.d("ABC", "Size = 0")

                        mCallback.onEmptyCallback()
                        view?.prgBar04?.visibility = View.GONE
                        view?.prgBar04?.clearAnimation()
                    }
                }
                .addOnFailureListener {
                    mCallback.onErrorCallback(it)
                    view?.prgBar04?.visibility = View.GONE
                    view?.prgBar04?.clearAnimation()
                }
                .addOnCanceledListener {
                    view?.prgBar04?.visibility = View.GONE
                    view?.prgBar04?.clearAnimation()

                }
    }

    private val mCallback = object: ListCallbackWithErrorHandler<MutableList<DocumentSnapshot>, Exception>{
        override fun onCallback(data: MutableList<DocumentSnapshot>) {
            recycler(data)
            Log.d("LIST SIZEEE", data.size.toString())
        }

        override fun onErrorCallback(exception: Exception) {
            val errMessage = exception.message
            if(BuildConfig.DEBUG) {
                Log.e("Error", errMessage)
            }
        }

        override fun onEmptyCallback() {
            Toast.makeText(context, "ไม่พบรายวิชาที่ทำการสอน", Toast.LENGTH_SHORT).show()
        }
    }

    private fun recycler(list: MutableList<DocumentSnapshot>) {
        Log.d("RECYCLER", "CALLED")
        layoutManager = LinearLayoutManager(activity)
        historyAdapter = TeachingCourseAdapter(activity!!, list)
        Log.d("LIST COUNT", list.size.toString())
        view!!.recycler_view.setHasFixedSize(true)
        view!!.recycler_view.adapter = historyAdapter
        view!!.recycler_view.layoutManager = layoutManager

    }

    companion object {
        fun newInstance()= TeachingHistoryFragment()
    }


    inner class TeachingCourseAdapter(
            private val context: Context,
            private val list: MutableList<DocumentSnapshot>
    ): RecyclerView.Adapter<TeachingCourseAdapter.ViewHolder>() {

        inner class ViewHolder(view :View): RecyclerView.ViewHolder(view)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.cardview_universal
                , parent, false
            )
            Log.d("NAME", "DS")
                    val holder = ViewHolder(view)
            view.setOnLongClickListener{
                val data = list[holder.adapterPosition]
                val const = Constants.Course
                val model = AddCourseModelExtend(UUID = data[const.UUID] as String,
                        course_name = data[const.name] as String,
                        course_cat = data[const.cat] as String,
                        course_grade = data[const.grade] as String,
                        course_d_start = data[const.d_start] as Long,
                        course_d_end = data[const.d_end] as Long,
                        course_t_start = data[const.t_start] as String,
                        course_t_end = data[const.t_end] as String,
                        course_pic = data[const.pic] as String,
                        user_id = data[const.user_id] as String,
                        ac_year = data[const.ac_year] as String,
                        date_study = data[const.dateInWeek] as MutableMap<String, Boolean>,
                        stu_list = data[const.stu_List] as MutableMap<String, String>
                )

                (activity as MainActivity).supportFragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.root_main, RealTeachingHistoryFragment.newInstance(model))
                        .commit()
                return@setOnLongClickListener true
            }

            return holder
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = holder.itemView

            item.txt_id01.text = list[position][Constants.Course.name] as String
            item.txt_id02.text = list[position][Constants.Course.cat] as String
            item.txt_id03.text = list[position][Constants.Course.ac_year] as String
            item.layout_id04.visibility = View.GONE
            Log.d("NAME", list[position][Constants.Course.name] as String)
        }

        override fun getItemCount() = list.size

    }
}