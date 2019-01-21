package com.example.tutorapp.Fragment.PeopleManagement

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.tutorapp.Adapter.PeopleAdapter
import com.example.tutorapp.R
import com.example.tutorapp.Utility.FireStoreInitial
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.android.synthetic.main.fragment_viewpeople.view.*

class ViewPeopleFragment: Fragment() {

    private lateinit var viewManager: LinearLayoutManager
    private lateinit var viewAdapter: PeopleAdapter
    private var focusAuto: AutoCompleteTextView? = null
    private var list: MutableList<String> = mutableListOf()
    private lateinit var listMAP: MutableList<DocumentSnapshot>
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_viewpeople, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = FireStoreInitial.initial()
        listMAP = mutableListOf<DocumentSnapshot>()
        initialSpinner(view)
    }

    private fun initialSpinner(view: View) {
        view.viewpeople_spinner.adapter = ArrayAdapter( view.viewpeople_spinner.context, android.R.layout.simple_spinner_dropdown_item,
                arrayOf("บทบาทในระบบ", "ชื่อ", "นามสกุล", "รหัสประจำตัว")
        )
        view.viewpeople_spinner.setSelection(0)
        view.viewpeople_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, v: View?, position: Int, id: Long) {

                try {
                    viewAdapter.notifyDataSetChanged()
                    viewManager.removeAllViews()
                } catch(ex: java.lang.Exception) {
                    ex.printStackTrace()
                }

                when (position) {
                    0 -> {
                        view.linear00.visibility = View.VISIBLE
                        view.linear01.visibility = View.GONE
                        view.linear02.visibility = View.GONE
                        view.linear03.visibility = View.GONE
                        autoCompleteFocus(0)
                    }
                    1 -> {
                        view.linear00.visibility = View.GONE
                        view.linear01.visibility = View.VISIBLE
                        view.linear02.visibility = View.GONE
                        view.linear03.visibility = View.GONE
                        autoCompleteFocus(1)
                    }
                    2 -> {
                        view.linear00.visibility = View.GONE
                        view.linear01.visibility = View.GONE
                        view.linear02.visibility = View.VISIBLE
                        view.linear03.visibility = View.GONE
                        autoCompleteFocus(2)
                    }
                    3 -> {
                        view.linear00.visibility = View.GONE
                        view.linear01.visibility = View.GONE
                        view.linear02.visibility = View.GONE
                        view.linear03.visibility = View.VISIBLE
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
                    list = mutableListOf()
                    list.clear()
                    list = mutableListOf("นักเรียน", "อาจารย์", "พนักงาน")
                } catch (ex:Exception){
                    ex.printStackTrace()
                }
                view!!.search_role.setAdapter(ArrayAdapter<String>(activity!!,
                        android.R.layout.simple_dropdown_item_1line,
                        list)
                )
                view!!.search_role.showSoftInputOnFocus = false
                view!!.search_role.keyListener = null
                view!!.search_role.threshold = 0
                view!!.search_role.onItemClickListener = AdapterView.OnItemClickListener{
                    parent, _, position, _ ->
                    view!!.search_role.setText(parent.getItemAtPosition(position).toString())
                }
                view!!.search_role.setOnClickListener {
                    view!!.search_role.text = null
                    view!!.search_role.clearFocus()
                    view!!.clearFocus()
                }
                view!!.search_role.onFocusChangeListener = View.OnFocusChangeListener{
                    _, b ->
                    if(b){
                        view!!.search_role.showDropDown()
                    }
                }
                view!!.search_role_btn.setOnClickListener{
                    try {
                        listMAP.clear()
                        viewAdapter.notifyDataSetChanged()
                        viewManager.removeAllViews()
                    } catch(ex: java.lang.Exception) {
                        ex.printStackTrace()
                    }
                    if( view!!.search_role.text != null && view!!.search_role.text.toString() != ""){
                        db.collection("User_Profile")
                                .whereEqualTo("role", view!!.search_role.text.toString())
                                .limit(50)
                                .get()
                                .addOnCompleteListener { task ->
                                    if(task.isSuccessful){
                                        if(task.result!!.size() > 0){
                                            Log.d("Role", "Result > 0")
                                            listMAP = mutableListOf()
                                            listMAP.clear()
                                            listMAP.addAll(task.result!!.documents)
                                            recycleViewCall(listMAP, 0)
                                            FancyToast.makeText(context, "การร้องขอข้อมูลสำเร็จ", FancyToast.LENGTH_SHORT,
                                                FancyToast.SUCCESS, false).show()
                                        } else {
                                            Log.d("Role", "Result = 0")
                                            listMAP = mutableListOf()
                                            listMAP.clear()
                                            listMAP.addAll(task.result!!.documents)
                                            recycleViewCall(listMAP, 0)

                                            FancyToast.makeText(context, "การร้องขอข้อมูลสำเร็จ ไม่พบข้อมูลในฐานข้อมูล", FancyToast.LENGTH_SHORT,
                                                FancyToast.SUCCESS, false).show()
                                        }
                                    } else {
                                        FancyToast.makeText(context, "การร้องขอข้อมูลล้มเหลว กรุณาลองใหม่ในภายหลัง", FancyToast.LENGTH_SHORT,
                                            FancyToast.ERROR, false).show()
                                    }
                                }
                    } else {
                        FancyToast.makeText(context, "กรุณาเลือกบทบาทในระบบ", FancyToast.LENGTH_SHORT,
                            FancyToast.WARNING, false).show()
                    }
                }
            }
            1 -> {
                try {
                    list = mutableListOf()
                    list.clear()
                    viewAdapter.notifyDataSetChanged()
                    viewManager.removeAllViews()
                } catch (ex:Exception){
                    ex.printStackTrace()
                }
                view!!.search_fname_btn.setOnClickListener {
                    try{
                        listMAP.clear()
                        viewAdapter.notifyDataSetChanged()
                        viewManager.removeAllViews()
                    } catch(ex: Exception) {

                    }
                    if( view!!.search_fname.text != null && view!!.search_fname.text.toString() != "") {
                        db.collection("User_Profile")
                            .whereEqualTo("fname", view!!.search_fname.text.toString())
                            .limit(50)
                            .get()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d("Complete", "Success to get data.")
                                    if (task.result!!.size() > 0) {
                                        Log.d("Fname", "Result > 0")
                                        listMAP = mutableListOf()
                                        listMAP.clear()
                                        listMAP.addAll(task.result!!.documents)
                                        recycleViewCall(listMAP, 1)
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
                                } else {
                                    Log.d("Fname", "Result = 0")
                                    listMAP = mutableListOf()
                                    listMAP.clear()
                                    listMAP.addAll(task.result!!.documents)
                                    recycleViewCall(listMAP, 1)
                                    Log.d("Complete", "Fail to get data.")

                                    FancyToast.makeText(
                                        context, "การร้องขอข้อมูลล้มเหลว กรุณาลองใหม่ในภายหลัง", FancyToast.LENGTH_SHORT,
                                        FancyToast.ERROR, false
                                    ).show()
                                }
                            }
                    } else {
                        FancyToast.makeText(context, "กรุณาเใส่ชื่อที่ต้องการค้นหาลงในช่องว่าง", FancyToast.LENGTH_SHORT,
                            FancyToast.WARNING, false).show()
                    }
                }
            }
            2 -> {
                try {
                viewAdapter.notifyDataSetChanged()
                viewManager.removeAllViews()
            } catch(ex: java.lang.Exception) {
                ex.printStackTrace()
            }
                view!!.search_lname_btn.setOnClickListener {
                    try {
                        listMAP.clear()
                        viewAdapter.notifyDataSetChanged()
                        viewManager.removeAllViews()
                    } catch(ex: java.lang.Exception) {
                        ex.printStackTrace()
                    }
                    if( view!!.search_lname.text != null && view!!.search_lname.text.toString() != "") {
                        db.collection("User_Profile")
                            .whereEqualTo("lname", view!!.search_stu_id.text.toString())
                            .limit(20)
                            .get()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    if (task.result!!.size() > 0) {
                                        Log.d("Lname", "Result > 0")
                                        listMAP = mutableListOf()
                                        listMAP.clear()
                                        listMAP.addAll(task.result!!.documents)
                                        recycleViewCall(listMAP, 2)
                                        FancyToast.makeText(
                                            context, "การร้องขอข้อมูลสำเร็จ", FancyToast.LENGTH_SHORT,
                                            FancyToast.SUCCESS, false
                                        ).show()
                                    } else {
                                        Log.d("Lname", "Result = 0")
                                        listMAP = mutableListOf()
                                        listMAP.clear()
                                        listMAP.addAll(task.result!!.documents)
                                        recycleViewCall(listMAP, 2)
                                        FancyToast.makeText(
                                            context,
                                            "การร้องขอข้อมูลสำเร็จ ไม่พบข้อมูลในฐานข้อมูล",
                                            FancyToast.LENGTH_SHORT,
                                            FancyToast.SUCCESS,
                                            false
                                        ).show()
                                    }
                                } else {
                                    FancyToast.makeText(
                                        context, "การร้องขอข้อมูลล้มเหลว กรุณาลองใหม่ในภายหลัง", FancyToast.LENGTH_SHORT,
                                        FancyToast.ERROR, false
                                    ).show()
                                }
                            }
                    } else {
                        FancyToast.makeText(context, "กรุณาเใส่สกุลที่ต้องการค้นหาลงในช่องว่าง", FancyToast.LENGTH_SHORT,
                            FancyToast.WARNING, false).show()
                    }
                }
            }
            3 -> {
                try {
                    viewAdapter.notifyDataSetChanged()
                    viewManager.removeAllViews()
                } catch(ex: java.lang.Exception) {
                    ex.printStackTrace()
                }
                view!!.search_stu_id_btn.setOnClickListener {
                    try {
                        listMAP.clear()
                        viewAdapter.notifyDataSetChanged()
                        viewManager.removeAllViews()
                    } catch(ex: java.lang.Exception) {
                        ex.printStackTrace()
                    }
                    if( view!!.search_stu_id.text != null && view!!.search_stu_id.text.toString() != "") {
                        db.collection("User_Profile")
                            .whereGreaterThanOrEqualTo("user_id", view!!.search_stu_id.text.toString())
                            .limit(30)
                            .get()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    if (task.result!!.size() > 0) {
                                        Log.d("User_ID", "Result > 0")
                                        listMAP = mutableListOf()
                                        listMAP.clear()
                                        listMAP.addAll(task.result!!.documents)
                                        recycleViewCall(listMAP, 3)
                                        FancyToast.makeText(
                                            context, "การร้องขอข้อมูลสำเร็จ", FancyToast.LENGTH_SHORT,
                                            FancyToast.SUCCESS, false
                                        ).show()
                                    } else {
                                        Log.d("User_ID", "Result = 0")
                                        listMAP = mutableListOf()
                                        listMAP.clear()
                                        listMAP.addAll(task.result!!.documents)
                                        recycleViewCall(listMAP, 3)
                                        FancyToast.makeText(
                                            context,
                                            "การร้องขอข้อมูลสำเร็จ ไม่พบข้อมูลในฐานข้อมูล",
                                            FancyToast.LENGTH_SHORT,
                                            FancyToast.SUCCESS,
                                            false
                                        ).show()
                                    }
                                } else {
                                    FancyToast.makeText(
                                        context, "การร้องขอข้อมูลล้มเหลว กรุณาลองใหม่ในภายหลัง", FancyToast.LENGTH_SHORT,
                                        FancyToast.ERROR, false
                                    ).show()
                                }
                            }
                    } else {
                        FancyToast.makeText(context, "กรุณาเใส่รหัสประจำตัวที่ต้องการค้นหาลงในช่องว่าง", FancyToast.LENGTH_SHORT,
                            FancyToast.WARNING, false).show()
                    }
                }
            }
        }

    }

    private fun recycleViewCall(map: MutableList<DocumentSnapshot>, focus: Int) {

        viewManager = LinearLayoutManager(activity)
        viewAdapter = PeopleAdapter(activity!!, map, focus)
        //viewManager.removeView(view!!.linearLayout0)

        view!!.recycle_view_people.apply {
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
            return ViewPeopleFragment()
        }
    }
}