package com.example.tutorapp.Fragment.CourseManagement.CheckIn

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tutorapp.Constants
import com.example.tutorapp.DataClass.AddCourseModelExtend
import com.example.tutorapp.MainActivity
import com.example.tutorapp.R
import com.example.tutorapp.Utility.FireStoreInitial
import com.google.firebase.firestore.DocumentSnapshot
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.android.synthetic.main.cardview_universal.view.*
import kotlinx.android.synthetic.main.fragment_select_class_to_checkin.view.*
import java.lang.Exception
import java.util.*

class SelecCourseToCheckInFragment: Fragment() {


    private lateinit var list: MutableList<DocumentSnapshot>

    private lateinit var viewManager: LinearLayoutManager
    private lateinit var viewAdapter : SelectCourseCheckInAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_select_class_to_checkin , container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.progressBar3.visibility = View.VISIBLE
        view.progressBar3.animate()
        view.recycle_select_course_chkin.visibility = View.GONE
        listInitial()
    }

    private fun listInitial() {
        val dateInLong = Date().time
        FireStoreInitial.initial().collection(Constants.Course.collection_name)
                .whereEqualTo(Constants.Course.user_id,
                        (activity as MainActivity).getCurrentID()
                )
                .whereGreaterThanOrEqualTo(Constants.Course.d_end, dateInLong)
                .get()
                .addOnCompleteListener {
                    if(it.isSuccessful) {
                        Log.d("Success", "Success")
                        if(it.result!!.size() > 0) {
                            list = mutableListOf()
                            list.clear()
                            try {
                                viewAdapter.notifyDataSetChanged()
                                viewManager.removeAllViews()
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                            }
                            list.addAll(it.result!!.documents)
                            view!!.progressBar3.visibility = View.GONE
                            view!!.progressBar3.clearAnimation()
                            view!!.recycle_select_course_chkin.visibility = View.VISIBLE
                            recyclerVIew(list = list)
                            FancyToast.makeText(activity, "รับข้อมูลจากฐานข้อมูลสำเร็จ", FancyToast.LENGTH_SHORT,
                                FancyToast.SUCCESS, false).show()
                        } else {
                            view!!.progressBar3.visibility = View.GONE
                            view!!.progressBar3.clearAnimation()
                            view!!.recycle_select_course_chkin.visibility = View.VISIBLE
                            FancyToast.makeText(activity, "รับข้อมูลจากฐานข้อมูลสำเร็จ ไม่พบข้อมูลในฐานข้อมูล", FancyToast.LENGTH_SHORT,
                                FancyToast.SUCCESS, false).show()
                        }
                    } else {
                        view!!.progressBar3.visibility = View.GONE
                        view!!.progressBar3.clearAnimation()
                        view!!.recycle_select_course_chkin.visibility = View.VISIBLE
                    }
                }
                .addOnCanceledListener {
                    view!!.progressBar3.visibility = View.GONE
                    view!!.progressBar3.clearAnimation()
                    view!!.recycle_select_course_chkin.visibility = View.VISIBLE
                    FancyToast.makeText(activity, "การรับข้อมูลจากฐานข้อมูลถูกยกเลิก", FancyToast.LENGTH_SHORT,
                        FancyToast.WARNING, false).show()
                }
                .addOnFailureListener {
                    view!!.progressBar3.visibility = View.GONE
                    view!!.progressBar3.clearAnimation()
                    view!!.recycle_select_course_chkin.visibility = View.VISIBLE
                    it.printStackTrace()
                    FancyToast.makeText(activity, "เกิดความผิดพลาดในการรับข้อมูลจากฐานข้อมูล", FancyToast.LENGTH_SHORT,
                        FancyToast.ERROR, false).show()
                }

    }

    private fun recyclerVIew(list: MutableList<DocumentSnapshot>) {
        viewAdapter = SelectCourseCheckInAdapter(activity!!, list)
        viewManager = LinearLayoutManager(activity)

        view!!.recycle_select_course_chkin.apply {
            setHasFixedSize(true)
            adapter = viewAdapter
            layoutManager = viewManager
        }
    }

    companion object {
        fun newInstance(): Fragment {
            val fragment = SelecCourseToCheckInFragment()
            return fragment
        }
    }

    private class SelectCourseCheckInAdapter(private val context: Context,
                                 private val list: MutableList<DocumentSnapshot>
    ): RecyclerView.Adapter<SelectCourseCheckInAdapter.ViewHolder>(){

        inner class ViewHolder(view: View): RecyclerView.ViewHolder(view)

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.cardview_universal, viewGroup, false)
            val holder = ViewHolder(view)

            holder.itemView.setOnLongClickListener {
                // TODO
                (it.context as FragmentActivity).supportFragmentManager.beginTransaction().apply{
                    addToBackStack(null)
                    val model = AddCourseModelExtend(UUID = list[holder.adapterPosition][Constants.Course.UUID] as String,
                            course_name = list[holder.adapterPosition][Constants.Course.name] as String,
                            course_cat = list[holder.adapterPosition][Constants.Course.cat] as String,
                            course_grade = list[holder.adapterPosition][Constants.Course.grade] as String,
                            course_d_start = list[holder.adapterPosition][Constants.Course.d_start] as Long,
                            course_t_start = list[holder.adapterPosition][Constants.Course.t_start] as String,
                            course_d_end = list[holder.adapterPosition][Constants.Course.d_end] as Long,
                            course_t_end = list[holder.adapterPosition][Constants.Course.t_end] as String,
                            course_pic = list[holder.adapterPosition][Constants.Course.pic] as String,
                            user_id = list[holder.adapterPosition][Constants.Course.user_id] as String,
                            ac_year = list[holder.adapterPosition][Constants.Course.ac_year] as String,
                            date_study = list[holder.adapterPosition][Constants.Course.dateInWeek] as MutableMap<String, Boolean>,
                            stu_list = list[holder.adapterPosition][Constants.Course.stu_List] as MutableMap<String, String>
                    )
                    replace(R.id.root_main, SelectSheetCheckInFragment.newInstance(model))
                    commit()
                }
                return@setOnLongClickListener true
            }
            return holder
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.itemView.txt_id01.text = list[position][Constants.Course.name] as String
            holder.itemView.txt_id02.text = list[position][Constants.Course.cat] as String
            holder.itemView.txt_id03.text = "ปีการศึกษา " + list[position][Constants.Course.ac_year] as String
            holder.itemView.layout_id04.visibility = View.GONE
        }

        override fun getItemCount() = list.size

    }



}