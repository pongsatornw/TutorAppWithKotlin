package com.example.tutorapp.Fragment.CourseManagement

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.example.tutorapp.DataClass.AddCourseModel
import com.example.tutorapp.R
import com.example.tutorapp.Utility.FireStoreInitial
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.android.synthetic.main.fragment_select_course_to_add.view.*
import java.util.*
import kotlin.collections.ArrayList

class SelectCourseToAddStudentFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_select_course_to_add, container, false)
        return view
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        autoTextViewSetting(v.auto_001, v, 0)
        autoTextViewSetting(v.auto_002, v, 1)
        v.btn001.setOnClickListener {
            FireStoreInitial.initial()
                    .collection("Course_Data")
                    .whereEqualTo("course_cat", v.auto_001.text.toString())
                    .whereEqualTo("course_grade", v.auto_002.text.toString())
                    .whereEqualTo("ac_year", v.auto_003.text.toString())
                        .get()
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful) {
                            Log.d("OnSuccess", "Success")
                            val result = task.result!!.documents
                            val dayInLong = Date().time
                            val preparingList : ArrayList<AddCourseModel> = ArrayList()
                            val ongoingList : ArrayList<AddCourseModel> = ArrayList()
                            if(result.size > 0) {
                                Log.d("OnSuccess", "Size > 0")
                                for (r in result) {
                                    if ((r["course_d_start"] as Long) > dayInLong) {
                                        val model = AddCourseModel(r["UUID"] as String, r["course_name"] as String,
                                                r["course_cat"] as String, r["course_grade"] as String,
                                                r["course_d_start"] as Long, r["course_t_start"] as String,
                                                r["course_d_end"] as Long, r["course_t_end"] as String,
                                                r["course_pic"] as String, r["user_id"] as String, r["ac_year"] as String,
                                                r["date_in_week"] as MutableMap<String, Boolean>
                                            )
                                        preparingList.add(model)
                                        Log.d("preparingList-chk Date", preparingList.size.toString())

                                    } else if ((r["course_d_start"] as Long) < dayInLong &&
                                            (r["course_d_end"] as Long) > Date().time) {
                                        val model = AddCourseModel(r["course_name"] as String, r["course_name"] as String,
                                                r["course_cat"] as String, r["course_grade"] as String,
                                                r["course_d_start"] as Long, r["course_t_start"] as String,
                                                r["course_d_end"] as Long, r["course_t_end"] as String,
                                                r["course_pic"] as String, r["user_id"] as String, r["ac_year"] as String,
                                                r["date_study"] as MutableMap<String, Boolean>
                                        )
                                        ongoingList.add(model)
                                    }
                                }
                                fragmentManager!!.apply {
                                    popBackStack()
                                    beginTransaction()
                                            .addToBackStack(null)
                                            .replace(R.id.root_main, AddStudentToCourseFragment.newInstance(
                                                    preparingList, ongoingList
                                            ))
                                            .commit()
                                }
                                FancyToast.makeText(context, "ไม่พบข้อมูล", Toast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
                                Log.d("OnSuccess", "Failure")
                            } else {
                                FancyToast.makeText(context, "ไม่พบข้อมูล", Toast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
                                Log.d("OnSuccess", "Failure")
                            }
                        } else {
                            FancyToast.makeText(context, "เกิดความผิดพลาดในการร้องขอข้อมูล กรุณรลองใหม่ในภายหลัง", Toast.LENGTH_SHORT, FancyToast.ERROR, false).show()
                            Log.d("OnSuccess", "Failure")
                        }
                    }

        }

    }

    private fun autoTextViewSetting(auto: AutoCompleteTextView, v: View, i : Int){
        var list = mutableListOf<String>()
        if( i == 1)
            list = mutableListOf("ป.1", "ป.2", "ป.3", "ป.4", "ป.5", "ป.6",
                    "ม.1", "ม.2", "ม.3", "ม.4", "ม.5", "ม.6" )
        else if( i == 0)
            list = mutableListOf("คณิตศาสตร์", "วิทยาศาสตร์", "อังกฤษ"
        , "ไทย", "สังคม", "ฟิสิกส์", "เคมี", "ชีวะ")
        auto.apply {
            setAdapter(ArrayAdapter<String>(activity!!,
                    android.R.layout.simple_dropdown_item_1line,
                    list
            ))
            showSoftInputOnFocus = false
            keyListener = null
            threshold = 0
            onItemClickListener = AdapterView.OnItemClickListener{
                parent, _, position, _ ->
                auto.setText(parent.getItemAtPosition(position).toString())
            }
            setOnClickListener {
                auto.text = null
                auto.clearFocus()
            }
            onFocusChangeListener = View.OnFocusChangeListener{
                _, b ->
                if(b){
                    auto.showDropDown()
                }
            }
        }

    }
    companion object {
        fun newInstance(): Fragment {
            return SelectCourseToAddStudentFragment()
        }
    }
}