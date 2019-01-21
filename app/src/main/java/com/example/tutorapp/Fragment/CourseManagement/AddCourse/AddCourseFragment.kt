package com.example.tutorapp.Fragment.CourseManagement.AddCourse

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.tutorapp.DataClass.AddCourseModel
import com.example.tutorapp.Fragment.MainFragment
import com.example.tutorapp.R
import com.example.tutorapp.Service.FirestoreAddCourseService
import com.example.tutorapp.Service.FirestoreEditCourseService
import com.example.tutorapp.Utility.FireStoreInitial
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.synthetic.main.fragment_addcourse.*
import kotlinx.android.synthetic.main.fragment_addcourse.view.*
import java.lang.Exception
import java.text.DateFormat
import java.util.*

class AddCourseFragment: Fragment() {

    private val ins_list = mutableListOf<String>()
    private val doc_list = mutableListOf<DocumentSnapshot>()
    private var year_select01 = 0L
    private var year_select02 = 0L
    private var instructor_position: Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val db = FireStoreInitial.initial()
        db.collection("User_Profile").whereEqualTo("role", "อาจารย์")
                .get()
                .addOnCompleteListener {
                    if( it.isSuccessful){
                        if(it.result != null) {
                            for (doc in it.result!!.iterator()) {
                                ins_list.add(((doc.data["fname"] as String) + " " + (doc.data["lname"] as String)) )
                                doc_list.add(doc)
                            }
                        }
                    }
                }

        return inflater.inflate(R.layout.fragment_addcourse, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        autoCompleteInitial()

        val bundle = this.arguments

        if(bundle != null){
            val model = bundle.getParcelable<AddCourseModel>("course_data")
            val docID = bundle.getString("documentID")
            if( model != null) {
                val df = DateFormat.getDateInstance(DateFormat.DEFAULT)
                Log.d("MOdel", "Model is not null")
                view.edt_coursename.setText(model.course_name)
                view.edt_coursecat.setText(model.course_cat)
                view.edt_coursedstart.setText(df.format(Date(model.course_d_start)))
                view.edt_coursedend.setText(df.format(Date(model.course_d_end)))
                view.edt_coursetstart.setText(model.course_t_start)
                view.edt_coursetend.setText(model.course_t_end)
                view.edt_coursegrade.setText(model.course_grade)
                view.edt_coursepic.setText(model.course_pic)
                view.chk_coursesun.isChecked = model.date_study["sun"]!!
                view.chk_coursemon.isChecked = model.date_study["mon"]!!
                view.chk_coursetue.isChecked = model.date_study["tue"]!!
                view.chk_coursewed.isChecked = model.date_study["wed"]!!
                view.chk_coursethr.isChecked = model.date_study["thr"]!!
                view.chk_coursefri.isChecked = model.date_study["fri"]!!
                view.chk_coursesat.isChecked = model.date_study["sat"]!!
                view.edt_acyear.setText(model.ac_year)

                view.edt_coursecat.isEnabled = false
                view.edt_acyear.isEnabled = false
                view.edt_coursegrade.isEnabled = false
                view.bt_coursereset.text = "รีเซ็ทข้อมูล"

                resetDataForEdit(view, model)
                confirmDataForEdit(view, docID!!)
            } else {
                Log.d("MOdel", "Model is null")
                resetDataForAdd(view)
                confirmDataForAdd(view)
            }

        } else {
            Log.d("MOdel", "Model is null")
            resetDataForAdd(view)
            confirmDataForAdd(view)
        }

        view.edt_coursedstart.showSoftInputOnFocus = false
        view.edt_coursedstart.keyListener = null
        view.edt_coursedstart.setOnClickListener {
            dateChooser(it as EditText, 0)
        }

        view.edt_coursedend.setOnClickListener {
            dateChooser(it as EditText, 1)
        }

        view.edt_coursetstart.setOnClickListener {
            timeChooser(it as EditText)
        }

        view.edt_coursetend.setOnClickListener {
            timeChooser(it as EditText)
        }
    }

    private fun autoCompleteInitial() {
        val subjectList = arrayListOf("คณิตศาสตร์", "วิทยาศาสตร์", "อังกฤษ"
                , "ไทย", "สังคม", "ฟิสิกส์", "เคมี", "ชีวะ")
        val gradeList = arrayListOf("ป.1", "ป.2", "ป.3", "ป.4", "ป.5", "ป.6",
                "ม.1", "ม.2", "ม.3", "ม.4", "ม.5", "ม.6" )
        val auto1 = view!!.findViewById<AutoCompleteTextView>(R.id.edt_coursecat)
        auto1.setAdapter(ArrayAdapter<String>(activity!!,
                android.R.layout.simple_dropdown_item_1line,
                subjectList)
        )
        auto1.showSoftInputOnFocus = false
        auto1.keyListener = null
        auto1.threshold = 0
        auto1.onItemClickListener = AdapterView.OnItemClickListener{
            parent, _, position, _ ->
            auto1.setText(parent.getItemAtPosition(position).toString())
        }
        auto1.setOnClickListener {
            auto1.text = null
            auto1.clearFocus()
        }
        auto1.onFocusChangeListener = View.OnFocusChangeListener{
            _, b ->
            if(b){
                auto1.showDropDown()
            }
        }

        val auto2 = view!!.findViewById<AutoCompleteTextView>(R.id.edt_coursegrade)
        auto2.setAdapter(ArrayAdapter<String>(activity!!,
                android.R.layout.simple_dropdown_item_1line,
                gradeList)
        )
        auto2.showSoftInputOnFocus = false
        auto2.threshold = 0
        auto2.keyListener = null
        auto2.setOnClickListener {
            auto2.setText("")
            auto2.clearFocus()
        }
        auto2.onItemClickListener = AdapterView.OnItemClickListener{
            parent, _, position, _ ->
            auto2.setText(parent.getItemAtPosition(position).toString())

        }
        auto2.onFocusChangeListener = View.OnFocusChangeListener{
            _, b ->
            if(b){
                auto2.showDropDown()
            }
        }

        val auto3 = view!!.findViewById<AutoCompleteTextView>(R.id.edt_coursepic)
        auto3.setAdapter(ArrayAdapter<String>(activity!!,
                android.R.layout.simple_dropdown_item_1line,
                ins_list)
        )
        auto3.showSoftInputOnFocus = false
        auto3.threshold = 0
        auto3.keyListener = null
        auto3.setOnClickListener {
            auto3.setText("")
            auto3.clearFocus()
        }
        auto3.onItemClickListener = AdapterView.OnItemClickListener{
            parent, _, position, _ ->
            auto3.setText(parent.getItemAtPosition(position).toString())
            instructor_position = position
        }
        auto3.onFocusChangeListener = View.OnFocusChangeListener{
            _, b ->
            if(b){
                auto3.showDropDown()
            }
        }

    }

    private fun dateChooser(view: EditText, num: Int) {
        var dateLong = 0L
        if( Build.VERSION.SDK_INT < 24) {
            val c = java.util.Calendar.getInstance()
            var year = c.get(java.util.Calendar.YEAR)
            var month = c.get(java.util.Calendar.MONTH)
            var day = c.get(java.util.Calendar.DAY_OF_MONTH)
            DatePickerDialog(activity!!, DatePickerDialog.OnDateSetListener
                { _, yeaR, monthOfYear, dayOfMonth ->
                    year = yeaR
                    month = monthOfYear
                    day = dayOfMonth

                    //val df = SimpleDateFormat("dd/MM/yyyy", Locale("tha", "THA"))
                    c.set(yeaR, monthOfYear, dayOfMonth)
                    val date = c.time
                    Log.d("RET", date.toString())
                    val df = DateFormat.getDateInstance(DateFormat.DEFAULT)
                    view.setText(df.format(date))
                    dateLong = date.time
                    Log.d("dateLong In OnDateSet", dateLong.toString())
                    if(num == 0){
                        year_select01 = dateLong
                    } else if(num == 1) {
                        year_select02 = dateLong
                    }
                }
            , year, month, day).show()
            Log.d("dateLong", dateLong.toString())
        } else {
            val c = Calendar.getInstance()
            var year = c.get(Calendar.YEAR)
            var month = c.get(Calendar.MONTH)
            var day = c.get(Calendar.DAY_OF_MONTH)
            DatePickerDialog(activity!!, DatePickerDialog.OnDateSetListener
                { _, yeaR, monthOfYear, dayOfMonth ->
                    year = yeaR
                    month = monthOfYear
                    day = dayOfMonth
                    view.text

                    c.set(yeaR, monthOfYear, dayOfMonth)
                    val date = c.time
                    Log.d("RET", date.toString())
                    val df = DateFormat.getDateInstance(DateFormat.DEFAULT)
                    //val df = android.icu.text.SimpleDateFormat("dd/MM/yyyy", Locale("tha", "THA"))
                    Log.d("Date", date.toString())
                    Log.d("Date", "")
                    view.setText(df.format(date))
                    dateLong = date.time
                    Log.d("dateLong In OnDateSet", dateLong.toString())
                    if(num == 0){
                        year_select01 = dateLong
                    } else if(num == 1) {
                        year_select02 = dateLong
                    }
                }
            , year, month, day).show()
            Log.d("dateLong", dateLong.toString())
        }
    }
    private fun timeChooser(view: EditText){
        if( Build.VERSION.SDK_INT < 24) {
            val c = java.util.Calendar.getInstance()
            var hour = c.get(java.util.Calendar.HOUR_OF_DAY)
            var min = c.get(java.util.Calendar.MINUTE)

            TimePickerDialog(activity!!, TimePickerDialog.OnTimeSetListener
                { dialog, Hour, Minute ->
                    hour = Hour
                    min = Minute
                    dialog.currentHour = hour
                    dialog.currentMinute = min
                    c.set(0,0,0, dialog.currentHour, dialog.currentMinute)
                    val tf = DateFormat.getTimeInstance(DateFormat.SHORT)
                    view.setText(c.get(Calendar.HOUR_OF_DAY).toString() + ":" + c.get(Calendar.MINUTE).toString())
                }
            , hour, min, true).show()
        } else {
            val c = Calendar.getInstance()
            var hour = c.get(java.util.Calendar.HOUR_OF_DAY)
            var min = c.get(java.util.Calendar.MINUTE)


            TimePickerDialog(activity!!, TimePickerDialog.OnTimeSetListener
            { dialog, Hour, Minute ->
                hour = Hour
                min = Minute

                dialog.hour = hour
                dialog.minute = min
                c.set(0,0,0, dialog.hour, dialog.minute)
                val tf = DateFormat.getTimeInstance(DateFormat.SHORT)

                view.setText(c.get(Calendar.HOUR_OF_DAY).toString() + ":" + c.get(Calendar.MINUTE).toString())

            } , hour, min, true).show()

        }
    }

    private fun resetDataForEdit(view: View, model: AddCourseModel){

        view.bt_coursereset.setOnClickListener {
            val df = DateFormat.getDateInstance(DateFormat.DEFAULT)
            view.edt_coursename.isFocusable = false
            view.edt_coursecat.isFocusable = false
            view.edt_coursename.setText(model.course_name)
            view.edt_coursecat.setText(model.course_cat)
            view.edt_coursedstart.setText(df.format(Date(model.course_d_start)) )
            view.edt_coursedend.setText(df.format(Date(model.course_d_end)))
            view.edt_coursetstart.setText(model.course_t_start)
            view.edt_coursetend.setText(model.course_t_end)
            view.edt_coursegrade.setText(model.course_grade)
            view.edt_coursepic.setText(model.course_pic)
            view.edt_acyear.setText(model.ac_year)
            view.chk_coursesun.isChecked = model.date_study["sun"]!!
            view.chk_coursemon.isChecked = model.date_study["mon"]!!
            view.chk_coursetue.isChecked = model.date_study["tue"]!!
            view.chk_coursewed.isChecked = model.date_study["wed"]!!
            view.chk_coursethr.isChecked = model.date_study["thr"]!!
            view.chk_coursefri.isChecked = model.date_study["fri"]!!
            view.chk_coursesat.isChecked = model.date_study["sat"]!!
        }
    }

    private fun resetDataForAdd(view: View) {
        view.bt_coursereset.setOnClickListener {
            view.edt_coursename.text = null
            view.edt_coursecat.text = null
            view.edt_coursegrade.text = null
            view.edt_coursedstart.text = null
            view.edt_coursetstart.text = null
            view.edt_coursedend.text = null
            view.edt_coursetend.text = null
            view.edt_coursepic.text = null
            view.edt_acyear.text = null
            chk_coursesun.isChecked = false
            chk_coursemon.isChecked = false
            chk_coursetue.isChecked = false
            chk_coursewed.isChecked = false
            chk_coursethr.isChecked = false
            chk_coursefri.isChecked = false
            chk_coursesat.isChecked = false
        }
    }

    private fun confirmDataForAdd(view: View) {
        view.bt_coursenext.setOnClickListener {
            //activity!!.startService( Intent(activity, FirestoreAddCourseService::class.java))
            if( view.edt_coursename.text != null && view.edt_coursecat.text !=null
                    && view.edt_coursedstart.text != null && view.edt_coursetstart.text !=null
                    && view.edt_coursedend.text != null && view.edt_coursetend.text !=null
                    && view.edt_coursegrade.text != null && view.edt_coursepic.text != null &&

                    view.edt_coursename.text.toString() != "" && view.edt_coursecat.text.toString() != ""
                    && view.edt_coursedstart.text.toString() != ""&& view.edt_coursetstart.text.toString() != ""
                    && view.edt_coursedend.text.toString() != "" && view.edt_coursetend.text.toString() != ""
                    && view.edt_coursegrade.text.toString() != "" && view.edt_coursepic.text.toString() != "" ) {

                if( view.chk_coursesun.isChecked || view.chk_coursemon.isChecked
                        || view.chk_coursetue.isChecked || view.chk_coursewed.isChecked
                        || view.chk_coursethr.isChecked || view.chk_coursefri.isChecked
                        || view.chk_coursesat.isChecked ) {

                    try {
                        val ac_year = view.edt_acyear.text.toString().toInt()
                        val curr_year = java.util.Calendar.getInstance().get(Calendar.YEAR)
                        if(ac_year < curr_year ) {
                            Toast.makeText(context, "ปีการศึกษาต้องมีค่าไม่น้อยกว่าปี ค.ศ. ปัจจุบัน", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }
                    } catch (ex: Exception) {
                        Toast.makeText(context, "ข้อมูลปีการศึกษาต้องเป็นตัวเลขเท่านั้น", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    if(year_select01 >= year_select02 ) {
                        Log.d("Start time", year_select01.toString())
                        Log.d("End time", year_select02.toString())
                    } else if(year_select01 < Date().time) {
                        Log.d("Diff time", (Date().time - year_select01).toString())
                    } else {
                        val df = DateFormat.getDateInstance(DateFormat.DEFAULT)
                        val addCourseModel = AddCourseModel(UUID = "", course_name = view.edt_coursename.text.toString(), course_cat = view.edt_coursecat.text.toString(),
                                course_grade = view.edt_coursegrade.text.toString(), course_d_start = df.parse(view.edt_coursedstart.text.toString() ).time,
                                course_t_start = view.edt_coursetstart.text.toString(), course_d_end = df.parse(view.edt_coursedend.text.toString()).time,
                                course_t_end = view.edt_coursetend.text.toString(), course_pic = view.edt_coursepic.text.toString(),
                                ac_year = view.edt_acyear.text.toString(), user_id = ((doc_list[instructor_position]["user_id"] as String) + view.edt_coursepic.text.toString().split(" ")[1].substring(0,2)),
                                date_study = mutableMapOf("sun" to view.chk_coursesun.isChecked, "mon" to view.chk_coursemon.isChecked ,
                                        "tue" to view.chk_coursetue.isChecked , "wed" to view.chk_coursewed.isChecked ,
                                        "thr" to view.chk_coursethr.isChecked , "fri" to view.chk_coursefri.isChecked ,
                                        "sat" to view.chk_coursesat.isChecked
                                )
                        )

                        AlertDialog.Builder(activity)
                                .setTitle("ยืนยันการเพิ่มข้อมูลรายวิชา")
                                .setPositiveButton("ยืนยัน"){dialog, _ ->
                                    activity!!.startService(Intent(activity,
                                            FirestoreAddCourseService::class.java)
                                            .putExtra("course_model", addCourseModel)
                                    )
                                    dialog.dismiss()
                                }
                                .setNegativeButton("ยกเลิก"){dialog, _ ->
                                    dialog.dismiss()
                                }.create().show()
                    }
                } else {

                    AlertDialog.Builder(activity)
                            .setTitle("พบข้อผิดพลาด")
                            .setMessage("โปรดเลือกวันที่ทำการสอนอย่างน้อย 1 วัน")
                            .setCancelable(false)
                            .setNeutralButton("รับทราบ"){dialog, _ ->
                                dialog.dismiss()
                                /*fragmentManager!!.popBackStackImmediate(null ,FragmentManager.POP_BACK_STACK_INCLUSIVE)
                                fragmentManager!!.beginTransaction()
                                        .addToBackStack(null)
                                        .replace(R.id.root_main, MainFragment.newInstance())
                                        .commit()*/
                            }
                            .create().show()

                }

            } else {
                AlertDialog.Builder(activity)
                        .setTitle("พบข้อผิดพลาด")
                        .setMessage("กรุณากรอกข้อมูลให้ครบทุกช่อง")
                        .setCancelable(false)
                        .setNeutralButton("รับทราบ"){dialog, _ ->
                            dialog.dismiss()
                            fragmentManager!!.popBackStack(null ,FragmentManager.POP_BACK_STACK_INCLUSIVE)
                            fragmentManager!!.beginTransaction()
                                    //.addToBackStack(null)
                                    .replace(R.id.root_main, MainFragment.newInstance())
                                    .commit()
                        }
                        .create().show()
            }

        }
    }

    private fun confirmDataForEdit(view: View, dccuID: String) {
        view.bt_coursenext.text = "แก้ไขข้อมูล"
        view.bt_coursenext.setOnClickListener{

            if( view.edt_coursename.text != null && view.edt_coursecat.text !=null
                    && view.edt_coursedstart.text != null && view.edt_coursetstart.text !=null
                    && view.edt_coursedend.text != null && view.edt_coursetend.text !=null
                    && view.edt_coursegrade.text != null && view.edt_coursepic.text != null
                    && view.edt_acyear.text != null

                    && view.edt_coursename.text.toString() != "" && view.edt_coursecat.text.toString() != ""
                    && view.edt_coursedstart.text.toString() != ""&& view.edt_coursetstart.text.toString() != ""
                    && view.edt_coursedend.text.toString() != "" && view.edt_coursetend.text.toString() != ""
                    && view.edt_coursegrade.text.toString() != "" && view.edt_coursepic.text.toString() != ""
                    && view.edt_acyear.text.toString() != "") {

                Log.d("LOGGER", view.edt_coursename.text.toString())
                Log.d("LOGGER", view.edt_coursecat.text.toString())
                Log.d("LOGGER", view.edt_coursedstart.text.toString())
                Log.d("LOGGER", view.edt_coursetstart.text.toString())
                Log.d("LOGGER", view.edt_coursedend.text.toString())
                Log.d("LOGGER", view.edt_coursetend.text.toString())
                Log.d("LOGGER", view.edt_coursegrade.text.toString())
                if( view.chk_coursesun.isChecked || view.chk_coursemon.isChecked
                        || view.chk_coursetue.isChecked || view.chk_coursewed.isChecked
                        || view.chk_coursethr.isChecked || view.chk_coursefri.isChecked
                        || view.chk_coursesat.isChecked ) {

                    /*if( DateFormat.getDateInstance(DateFormat.DEFAULT).parse(view.edt_coursedstart.text.toString()).before(
                                    DateFormat.getDateInstance(DateFormat.DEFAULT).parse(view.edt_coursedstart.text.toString())
                            )){*/

                    if(year_select01 >= year_select02 ) {
                        Log.d("Start time", year_select01.toString())
                        Log.d("End time", year_select02.toString())
                    } else if(year_select01 < Date().time) {
                        Log.d("Diff time", (Date().time - year_select01).toString())
                    } else {
                        val df = DateFormat.getDateInstance(DateFormat.DEFAULT)
                        val editCourseModel = AddCourseModel("",view.edt_coursename.text.toString(), view.edt_coursecat.text.toString(),
                                view.edt_coursegrade.text.toString(), df.parse(view.edt_coursedstart.text.toString() ).time,
                                view.edt_coursetstart.text.toString(), df.parse(view.edt_coursedend.text.toString()).time,
                                view.edt_coursetend.text.toString(), view.edt_coursepic.text.toString(),
                                 "", view.edt_acyear.text.toString(),
                                mutableMapOf("sun" to view.chk_coursesun.isChecked, "mon" to view.chk_coursemon.isChecked ,
                                        "tue" to view.chk_coursetue.isChecked , "wed" to view.chk_coursewed.isChecked ,
                                        "thr" to view.chk_coursethr.isChecked , "fri" to view.chk_coursefri.isChecked ,
                                        "sat" to view.chk_coursesat.isChecked
                                )
                        )

                        AlertDialog.Builder(activity)
                                .setTitle("ยืนยันการแก้ไขข้อมูลรายวิชา")
                                .setPositiveButton("ยืนยัน"){dialog, _ ->
                                    activity!!.startService(Intent(activity,
                                            FirestoreEditCourseService::class.java)
                                            .putExtra("course_model", editCourseModel)
                                            .putExtra("documentID", dccuID)
                                    )
                                    dialog.dismiss()
                                }
                                .setNegativeButton("ยกเลิก"){dialog, _ ->
                                    dialog.dismiss()
                                }.create().show()
                    }

                } else {

                    AlertDialog.Builder(activity)
                            .setTitle("พบข้อผิดพลาด")
                            .setMessage("โปรดเลือกวันที่ทำการสอนอย่างน้อย 1 วัน")
                            .setCancelable(false)
                            .setNeutralButton("รับทราบ"){dialog, _ ->
                                dialog.dismiss()
                                /*fragmentManager!!.popBackStackImmediate(null ,FragmentManager.POP_BACK_STACK_INCLUSIVE)
                                fragmentManager!!.beginTransaction()
                                        .addToBackStack(null)
                                        .replace(R.id.root_main, MainFragment.newInstance())
                                        .commit()*/
                            }
                            .create().show()

                }

            } else {
                AlertDialog.Builder(activity)
                        .setTitle("พบข้อผิดพลาด")
                        .setMessage("กรุณากรอกข้อมูลให้ครบทุกช่อง")
                        .setCancelable(false)
                        .setNeutralButton("รับทราบ"){dialog, _ ->
                            dialog.dismiss()
                            fragmentManager!!.popBackStack(null ,FragmentManager.POP_BACK_STACK_INCLUSIVE)
                            fragmentManager!!.beginTransaction()
                                    //.addToBackStack(null)
                                    .replace(R.id.root_main, MainFragment.newInstance())
                                    .commit()
                        }
                        .create().show()
            }

        }
    }

    companion object {
        fun newInstance(): Fragment{
            return AddCourseFragment()
        }
    }
}