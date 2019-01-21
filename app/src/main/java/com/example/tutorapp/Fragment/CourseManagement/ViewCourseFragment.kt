package com.example.tutorapp.Fragment.CourseManagement


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import com.example.tutorapp.Adapter.CourseAdapter
import com.example.tutorapp.R
import com.example.tutorapp.Utility.FireStoreInitial
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.android.synthetic.main.fragment_viewcourse.view.*
import java.lang.Exception

class ViewCourseFragment: Fragment() {

    private lateinit var viewManager: LinearLayoutManager
    private lateinit var viewAdapter: CourseAdapter
    private lateinit var lstMAP: MutableList<DocumentSnapshot>
    private lateinit var prgBar: ProgressBar
    private var list: MutableList<String> = mutableListOf()
    private lateinit var db: FirebaseFirestore
    private val listOfSubject = mutableListOf("คณิตศาสตร์", "วิทยาศาสตร์", "อังกฤษ"
            , "ไทย", "สังคม", "ฟิสิกส์", "เคมี", "ชีวะ")
    private val listOfGrade = mutableListOf("ป.1", "ป.2", "ป.3", "ป.4", "ป.5", "ป.6",
            "ม.1", "ม.2", "ม.3", "ม.4", "ม.5", "ม.6" )
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_viewcourse, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = FireStoreInitial.initial()
        initialSpinner(view)
        prgBar = ProgressBar(context)
        prgBar.visibility = View.GONE
        /*val layout = view.findViewById<LinearLayout>(layout5555.id)
        layout.orientation = LinearLayout.VERTICAL
        layout.gravity = Gravity.CENTER
        val layout_params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        layout_params.gravity = Gravity.CENTER
        val prg_params = LinearLayout.LayoutParams(200, 200)
        prgBar.layoutParams = layout_params
        prg_params.gravity = Gravity.CENTER
        layout.addView(prgBar, prg_params)
        //prgBar.animate()*/
    }

    private fun initialSpinner(view: View) {
        view.viewcourse_spinner.adapter = ArrayAdapter(view.viewcourse_spinner.context, android.R.layout.simple_spinner_dropdown_item,
                arrayOf("ชื่อรายวิชา", "หมวดหมู่รายวิชา", "อาจารย์ผู้สอน", "ระดับชั้นของรายวิชา"))
        view.viewcourse_spinner.setSelection(1)
        view.viewcourse_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, v: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        view.linearLayout0.visibility = View.VISIBLE
                        view.linearLayout2.visibility = View.GONE
                        view.linearLayout3.visibility = View.GONE
                        view.linearLayout1.visibility = View.GONE
                        autoCompleteFocus(0)
                    }
                    1 -> {
                        view.linearLayout1.visibility = View.VISIBLE
                        view.linearLayout0.visibility = View.GONE
                        view.linearLayout2.visibility = View.GONE
                        view.linearLayout3.visibility = View.GONE
                        autoCompleteFocus(1)
                    }
                    2 -> {
                        view.linearLayout2.visibility = View.VISIBLE
                        view.linearLayout0.visibility = View.GONE
                        view.linearLayout3.visibility = View.GONE
                        view.linearLayout1.visibility = View.GONE
                        autoCompleteFocus(2)
                    }
                    3 -> {
                        view.linearLayout3.visibility = View.VISIBLE
                        view.linearLayout0.visibility = View.GONE
                        view.linearLayout2.visibility = View.GONE
                        view.linearLayout1.visibility = View.GONE
                        autoCompleteFocus(3)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }

    }

    private fun autoCompleteFocus(focus: Int){
        when(focus) {
            0 -> {
                try {
                    lstMAP.clear()
                    viewAdapter.notifyDataSetChanged()
                    viewManager.removeAllViews()
                } catch(ex:Exception) {
                    ex.printStackTrace()
                }
                view!!.search_coursename_btn.setOnClickListener {
                    prgBar.visibility = View.VISIBLE
                    prgBar.animate()
                    try {
                        lstMAP.clear()
                        viewAdapter.notifyDataSetChanged()
                        viewManager.removeAllViews()
                    } catch(ex:Exception) {
                        ex.printStackTrace()
                    }
                    if( view!!.search_coursename.text != null && view!!.search_coursename.text.toString() != "") {
                        db.collection("Course_Data")
                            .whereGreaterThanOrEqualTo("course_name", view!!.search_coursename.text.toString())
                            .limit(20)
                            .get()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    if (task.result!!.size() > 0) {
                                        lstMAP = mutableListOf<DocumentSnapshot>()
                                        lstMAP.addAll(task.result!!.documents)
                                        recycleViewCall(lstMAP, 0)
                                        FancyToast.makeText(
                                            context, "การร้องขอข้อมูลสำเร็จ", FancyToast.LENGTH_SHORT,
                                            FancyToast.SUCCESS, false
                                        ).show()
                                    } else {
                                        Log.d("doc", "0 doc")
                                        FancyToast.makeText(
                                            context,
                                            "การร้องขอข้อมูลสำเร็จ ไม่พบข้อมูลในฐานข้อมูล",
                                            FancyToast.LENGTH_SHORT,
                                            FancyToast.SUCCESS,
                                            false
                                        ).show()
                                    }
                                    prgBar.visibility = View.GONE
                                } else {
                                    Log.d("doc", "no doc")
                                    FancyToast.makeText(
                                        context, "การร้องขอข้อมูลล้มเหลว กรุณาลองใหม่ในภายหลัง", FancyToast.LENGTH_SHORT,
                                        FancyToast.ERROR, false
                                    ).show()
                                }
                                prgBar.visibility = View.GONE
                            }
                    } else {
                        FancyToast.makeText(context, "กรุณาเลือก \'หมวดหมู่รายวิชา\' เพื่อทำการค้นหารายวิชา", FancyToast.LENGTH_SHORT,
                            FancyToast.WARNING, false).show()
                    }
                }

            }
            1 -> {
                try {
                    lstMAP.clear()
                    viewAdapter.notifyDataSetChanged()
                    viewManager.removeAllViews()
                } catch(ex:Exception) {
                    ex.printStackTrace()
                }
                list = mutableListOf()
                list = listOfSubject
                view!!.search_coursecat.setAdapter(ArrayAdapter<String>(activity!!,
                        android.R.layout.simple_dropdown_item_1line,
                        list)
                )
                view!!.search_coursecat.showSoftInputOnFocus = false
                view!!.search_coursecat.keyListener = null
                view!!.search_coursecat.threshold = 0
                view!!.search_coursecat.onItemClickListener = AdapterView.OnItemClickListener{
                    parent, _, position, _ ->
                    view!!.search_coursecat.setText(parent.getItemAtPosition(position).toString())
                }
                view!!.search_coursecat.setOnClickListener {
                    view!!.search_coursecat.text = null
                    view!!.search_coursecat.clearFocus()
                    view!!.clearFocus()
                }
                view!!.search_coursecat.onFocusChangeListener = View.OnFocusChangeListener{
                    _, b ->
                    if(b){
                        view!!.search_coursecat.showDropDown()
                    }
                }
                view!!.search_coursecat_btn.setOnClickListener {
                    prgBar.visibility = View.VISIBLE
                    prgBar.animate()
                    try {
                        lstMAP.clear()
                        viewAdapter.notifyDataSetChanged()
                        viewManager.removeAllViews()
                    } catch(ex:Exception) {
                        ex.printStackTrace()
                    }
                    if( view!!.search_coursecat.text != null && view!!.search_coursecat.text.toString() != "") {
                        db.collection("Course_Data")
                            .whereEqualTo("course_cat", view!!.search_coursecat.text.toString())
                            .get()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    if (task.result!!.size() > 0) {
                                        lstMAP = mutableListOf<DocumentSnapshot>()
                                        lstMAP.clear()
                                        lstMAP.addAll(task.result!!.documents)
                                        recycleViewCall(lstMAP, 1)
                                        FancyToast.makeText(
                                            context, "การร้องขอข้อมูลสำเร็จ", FancyToast.LENGTH_SHORT,
                                            FancyToast.SUCCESS, false
                                        ).show()
                                    } else {
                                        Log.d("doc", "0 doc")
                                        FancyToast.makeText(
                                            context,
                                            "การร้องขอข้อมูลสำเร็จ ไม่พบข้อมูลในฐานข้อมูล",
                                            FancyToast.LENGTH_SHORT,
                                            FancyToast.SUCCESS,
                                            false
                                        ).show()
                                    }
                                    prgBar.visibility = View.GONE
                                } else {
                                    Log.d("doc", "no doc")
                                    FancyToast.makeText(
                                        context, "การร้องขอข้อมูลล้มเหลว กรุณาลองใหม่ในภายหลัง", FancyToast.LENGTH_SHORT,
                                        FancyToast.ERROR, false
                                    ).show()
                                    prgBar.visibility = View.GONE
                                }
                            }
                    } else {
                        FancyToast.makeText(context, "กรุณาเลือก \'หมวดหมู่รายวิชา\' เพื่อทำการค้นหารายวิชา", FancyToast.LENGTH_SHORT,
                            FancyToast.WARNING, false).show()
                    }
                }

            }
            2 -> {
                try {
                    lstMAP.clear()
                    viewAdapter.notifyDataSetChanged()
                    viewManager.removeAllViews()
                } catch(ex:Exception) {
                    ex.printStackTrace()
                }

                list = mutableListOf()
                db.collection("User_Profile")
                        .whereEqualTo("role", "อาจารย์")
                        .get()
                        .addOnCompleteListener {
                            if(it.isSuccessful){
                                if(it.result!!.size() > 0){
                                    for(doc in it.result!!.iterator()){
                                        list.add((doc.data["fname"] as String) + " "
                                        + (doc.data["lname"] as String))
                                    }
                                    view!!.search_ins.setAdapter(ArrayAdapter<String>(activity!!,
                                            android.R.layout.simple_dropdown_item_1line,
                                            list)
                                    )
                                }
                            }
                        }
                view!!.search_ins.setAdapter(ArrayAdapter<String>(activity!!,
                        android.R.layout.simple_dropdown_item_1line,
                        list)
                )
                view!!.search_ins.showSoftInputOnFocus = false
                view!!.search_ins.keyListener = null
                view!!.search_ins.threshold = 0
                view!!.search_ins.onItemClickListener = AdapterView.OnItemClickListener{
                    parent, _, position, _ ->
                    view!!.search_ins.setText(parent.getItemAtPosition(position).toString())
                }
                view!!.search_ins.setOnClickListener {
                    view!!.search_ins.text = null
                    view!!.clearFocus()

                }
                view!!.search_ins.onFocusChangeListener = View.OnFocusChangeListener{
                    _, b ->
                    if(b){
                        view!!.search_ins.showDropDown()
                    }
                }

                view!!.search_ins_btn.setOnClickListener {
                    prgBar.visibility = View.VISIBLE
                    prgBar.animate()
                    try {
                        lstMAP.clear()
                        viewAdapter.notifyDataSetChanged()
                        viewManager.removeAllViews()
                    } catch(ex:Exception) {
                        ex.printStackTrace()
                    }
                    if( view!!.search_ins.text != null && view!!.search_ins.text.toString() != "") {
                        db.collection("Course_Data")
                            .whereEqualTo("course_pic", view!!.search_ins.text.toString())
                            .get()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    if (task.result!!.size() > 0) {
                                        lstMAP = mutableListOf()
                                        lstMAP.addAll(task.result!!.documents)
                                        recycleViewCall(lstMAP, 2)
                                        FancyToast.makeText(
                                            context, "การร้องขอข้อมูลสำเร็จ", FancyToast.LENGTH_SHORT,
                                            FancyToast.SUCCESS, false
                                        ).show()
                                    } else {
                                        FancyToast.makeText(
                                            context,
                                            "การร้องขอข้อมูลสำเร็จ ไม่พบข้อมูลในฐานข้อมูล",
                                            FancyToast.LENGTH_SHORT,
                                            FancyToast.SUCCESS,
                                            false
                                        ).show()
                                    }
                                    prgBar.visibility = View.GONE
                                } else {
                                    FancyToast.makeText(
                                        context, "การร้องขอข้อมูลล้มเหลว กรุณาลองใหม่ในภายหลัง", FancyToast.LENGTH_SHORT,
                                        FancyToast.ERROR, false
                                    ).show()
                                    prgBar.visibility = View.GONE
                                }
                            }
                    } else {
                        FancyToast.makeText(context, "กรุณาเลือก \'อาจารย์ผู้สอน\' เพื่อทำการค้นหารายวิชา", FancyToast.LENGTH_SHORT,
                            FancyToast.WARNING, false).show()
                    }
                }
            }
            3 -> {
                try {
                    lstMAP.clear()
                    viewAdapter.notifyDataSetChanged()
                    viewManager.removeAllViews()

                } catch(ex:Exception) {
                    ex.printStackTrace()
                }

                list = mutableListOf()
                list = listOfGrade
                view!!.search_grade.setAdapter(ArrayAdapter<String>(activity!!,
                        android.R.layout.simple_dropdown_item_1line,
                        list)
                )
                view!!.search_grade.showSoftInputOnFocus = false
                view!!.search_grade.keyListener = null
                view!!.search_grade.threshold = 0
                view!!.search_grade.onItemClickListener = AdapterView.OnItemClickListener{
                    parent, _, position, _ ->
                    view!!.search_grade.setText(parent.getItemAtPosition(position).toString())
                }
                view!!.search_grade.setOnClickListener {
                    view!!.search_grade.text = null
                    view!!.search_grade.clearFocus()
                    view!!.clearFocus()
                }
                view!!.search_grade.onFocusChangeListener = View.OnFocusChangeListener{
                    _, b ->
                    if(b){
                        view!!.search_grade.showDropDown()
                    }
                }
                view!!.search_grade_btn.setOnClickListener {
                    prgBar.visibility = View.VISIBLE
                    prgBar.animate()
                    try {
                        lstMAP.clear()
                        viewAdapter.notifyDataSetChanged()
                        viewManager.removeAllViews()
                    } catch(ex:Exception) {
                        ex.printStackTrace()
                    }
                    if( view!!.search_grade.text != null && view!!.search_grade.text.toString() != "") {
                        db.collection("Course_Data")
                            .whereEqualTo("course_grade", view!!.search_grade.text.toString())
                            .get()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    if (task.result!!.size() > 0) {
                                        lstMAP = mutableListOf<DocumentSnapshot>()
                                        lstMAP.addAll(task.result!!.documents)
                                        recycleViewCall(lstMAP, 3)
                                    } else {
                                        Log.d("doc", "0 doc")
                                    }
                                    prgBar.visibility = View.GONE
                                } else {
                                    FancyToast.makeText(
                                        context, "การร้องขอข้อมูลล้มเหลว กรุณาลองใหม่ในภายหลัง", FancyToast.LENGTH_SHORT,
                                        FancyToast.ERROR, false
                                    ).show()
                                    prgBar.visibility = View.GONE
                                }
                            }
                    } else {
                        FancyToast.makeText(context, "กรุณาเลือกระดับชั้นเพื่อทำการค้นหารายวิชา", FancyToast.LENGTH_SHORT,
                            FancyToast.WARNING, false).show()
                    }
                }
            }
        }

    }

    private fun recycleViewCall(map: MutableList<DocumentSnapshot>, focus: Int) {

        viewManager = LinearLayoutManager(activity)
        viewAdapter = CourseAdapter(activity!!, map, focus)

        view!!.recycle_view.apply {
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
        fun newInstance(): Fragment {
            return ViewCourseFragment()
        }
    }

}