package com.example.tutorapp.Fragment.CourseManagement.CheckIn

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tutorapp.Constants
import com.example.tutorapp.DataClass.AddCourseModelExtend
import com.example.tutorapp.LoginActivity
import com.example.tutorapp.MainActivity
import com.example.tutorapp.R
import com.example.tutorapp.Utility.FireStoreInitial
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.alert_course_dialog.*
import kotlinx.android.synthetic.main.cardview_universal.view.*
import kotlinx.android.synthetic.main.fragment_class_checkin.view.*
import kotlinx.android.synthetic.main.fragment_select_class_to_checkin.view.*
import java.lang.StringBuilder
import java.text.DateFormat
import java.util.*

class SelectSheetCheckInFragment: Fragment() {

    private lateinit var list: MutableList<DocumentSnapshot>

    private lateinit var viewManager: LinearLayoutManager
    private lateinit var viewAdapter : CheckInAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_class_checkin , container, false)
            view.btn_new_checkin_sheet.elevation = 20F
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.background = ContextCompat.getDrawable(view.context, R.color.weak_orange)
        val bundle = this.arguments
        if(bundle != null) {
            val model = bundle.getParcelable<AddCourseModelExtend>("model")!!

            // Create new Course
            view.btn_new_checkin_sheet.setOnClickListener {
                val data = mutableMapOf<String, Any>()
                data.clear()
                data[Constants.Course.UUID] = model.UUID
                data["time"] = Date().time
                val map = mutableMapOf<String, Boolean>()
                model.stu_list.forEach{ pair ->
                    map[pair.key] =  false
                }
                data[Constants.CheckIn.checkList] = map
                createNewCheckIn(data = data, model = model) // Event in Button click
            }
            getPastCheckIn(model)

        } else {
            Log.w("TODO", "TODO")
            // TODO or No Else
        }
    }

    private fun createNewCheckIn(data: MutableMap<String, Any>, model: AddCourseModelExtend) {
        FireStoreInitial.initial().collection(Constants.CheckIn.collection_name)
                .add(data)
                .addOnCompleteListener {
                    when(it.isSuccessful) {
                        true -> {
                            Toast.makeText(view!!.context, "การสร้างใบเช็คชื่อสำเร็จ", Toast.LENGTH_SHORT).show()
                            getPastCheckIn(model)
                        }
                        false  -> {
                            Toast.makeText(view!!.context, "การสร้างใบเช็คชื่อใหม่ผิดพลาด", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .addOnSuccessListener {

                }

    }

    private fun getPastCheckIn(model: AddCourseModelExtend) {
        view!!.progressBar2.visibility = View.VISIBLE
        view!!.progressBar2.animate()
        FireStoreInitial.initial().collection(Constants.CheckIn.collection_name)
                .whereEqualTo(Constants.CheckIn.UUID, model.UUID)
                .orderBy(Constants.CheckIn.time, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        if(it.result!!.size() > 0){
                            list = mutableListOf()
                            list.clear()
                            list.addAll(it.result!!.documents)
                            recyclerVIew(list, model.course_name)
                        } else {
                            //TODO result size is 0, list is empty
                        }
                        view!!.progressBar2.clearAnimation()
                        view!!.progressBar2.visibility = View.GONE
                    } else {
                        view!!.progressBar2.clearAnimation()
                        view!!.progressBar2.visibility = View.GONE
                        // TODO isSuccessful return false
                    }
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    view!!.progressBar2.visibility = View.GONE
                    view!!.progressBar2.clearAnimation()
                    view!!.recycle_checkin_list.visibility = View.VISIBLE
                }
                .addOnCanceledListener {
                    view!!.progressBar2.visibility = View.GONE
                    view!!.progressBar2.clearAnimation()
                    view!!.recycle_checkin_list.visibility = View.VISIBLE
                }
    }

    private fun recyclerVIew(list: MutableList<DocumentSnapshot>, course_name: String) {
        viewAdapter = CheckInAdapter(activity!!, list, course_name)
        viewManager = LinearLayoutManager(activity)

        view!!.recycle_checkin_list.apply {
            setHasFixedSize(true)
            adapter = viewAdapter
            layoutManager = viewManager
        }
    }

    companion object {
        fun newInstance(model: AddCourseModelExtend): Fragment {
            val bundle = Bundle()
            bundle.putParcelable("model", model)
            val fragment = SelectSheetCheckInFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private class CheckInAdapter(
            private val context: Context,
            private val list: MutableList<DocumentSnapshot>,
            private val course_name: String
    ): RecyclerView.Adapter<CheckInAdapter.ViewHolder>(){

        inner class ViewHolder(view:View): RecyclerView.ViewHolder(view)

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.cardview_universal , viewGroup, false)
            val holder = ViewHolder(view)
            if(holder.adapterPosition == 0) {
                holder.itemView.card_background.background = ContextCompat
                        .getDrawable(holder.itemView.context, R.drawable.cardview_background_v2)
            }
            holder.itemView.elevation = 10F
            holder.itemView.layout_id04.visibility = View.GONE
            holder.itemView.setOnLongClickListener {
                Dialog(view.context as FragmentActivity).apply {
                    setContentView(R.layout.alert_course_dialog)
                    CompatBtn01.text = "เปิดการเช็ครายชื่อเข้าชั้นเรียนด้วยคาบเรียนที่ " + ( list.size - holder.adapterPosition).toString()
                    CompatBtn02.text = "ยกเลิก"
                    CompatBtn03.visibility = View.GONE

                    CompatBtn01.setOnClickListener {
                        (view.context as FragmentActivity).supportFragmentManager
                                .beginTransaction().apply{
                                    addToBackStack(null)
                                    replace(R.id.root_main, CheckInFragment.newInstance(
                                            course_name, list.size - holder.adapterPosition,
                                            list[holder.adapterPosition].id, list[0][Constants.Course.UUID] as String)
                                    )
                                    commit()
                                }

                        dismiss()
                    }
                    CompatBtn02.setOnClickListener {
                        dismiss()
                    }
                }.show()
                return@setOnLongClickListener true
            }
            return holder
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val miniView = holder.itemView
            if(position == 0) {
                miniView.card_background.background = ContextCompat
                        .getDrawable(holder.itemView.context, R.drawable.cardview_background_v2)
            } else {
                miniView.card_background.background = ContextCompat
                        .getDrawable(holder.itemView.context, R.drawable.cardview_background)
            }
            miniView.txt_id01.text = course_name
            miniView.txt_id02.text = StringBuilder().apply {
                append("คาบเรียนที่ ")
                append( (list.size - position).toString())
            }.toString()
            miniView.txt_id03.text = DateFormat.getDateTimeInstance(
                    DateFormat.DEFAULT, DateFormat.DEFAULT).format(list[position]["time"] as Long)
        }

        override fun getItemCount() = list.size

    }

}