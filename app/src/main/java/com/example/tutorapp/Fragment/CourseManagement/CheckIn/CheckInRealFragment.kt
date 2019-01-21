package com.example.tutorapp.Fragment.CourseManagement.CheckIn

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.example.tutorapp.*
import com.example.tutorapp.DataClass.CheckInModel
import com.example.tutorapp.Service.FirestoreCheckInService
import com.example.tutorapp.Utility.FireStoreInitial
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.synthetic.main.fragment_real_checkin.view.*

class CheckInRealFragment: Fragment() {

    private val BARCODE_READER_REQUEST_CODE = 1234
    private var checkin_list: MutableMap<String, Boolean> = mutableMapOf()
    private var docID: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_real_checkin, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = this.arguments!!

        Log.d("COURSE_NAME", bundle["course_name"]!! as String)
        Log.d("COURSE_NAME", bundle["course_name"]!! as String + " 555 " as String)

        docID =  bundle["doc_id"] as String
        view.txt_course_name.text = bundle["course_name"] as String
        view.txt_sheet_no.text = "คาบเรียนที่ " + (bundle["sheet_no"] as Int).toString()
        val UUID = bundle["UUID"] as String

        // CheckIn with id
        val merge_field = mutableListOf(Constants.CheckIn.checkList)

        view.button3.setOnClickListener { v ->
            var curr_id = ""
            AlertDialog.Builder(context).apply {
                setMessage("กรุณาใส่รหัสประจำตัว")
                val linearLayout = LinearLayout(context)
                linearLayout.orientation = LinearLayout.VERTICAL
                val edt = EditText(context)
                linearLayout.addView(edt)
                setView(linearLayout)
                setPositiveButton("ยืนยัน"){ r, _ ->
                    curr_id = edt.text.toString()

                    if(checkin_list.isEmpty()) {
                        FireStoreInitial.initial().collection(Constants.CheckIn.collection_name)
                                .document(docID)
                                .get()
                                .addOnSuccessListener {
                                    checkin_list.clear()
                                    val s_list = it[Constants.CheckIn.checkList] as MutableMap<String, Boolean>
                                    s_list.forEach { pair -> checkin_list[pair.key] = pair.value }
                                    checkIn(curr_id, docID)
                                }
                                .addOnFailureListener {
                                    //TODO
                                }
                                .addOnCanceledListener {
                                    //TODO
                                }
                    } else {
                        checkIn(curr_id, docID)
                    }

                    r.dismiss()
                }
                setNegativeButton("ยกเลิก"){ r, _ ->
                    r.dismiss()
                }
            }.show()

        }

        /**
         * Scan this with QR Code, If yes same as above.
         * */
        view.button4.setOnClickListener {


            startActivityForResult(Intent(activity, BarcodeCaptureActivity::class.java)
                    .putExtra("docID", docID), BARCODE_READER_REQUEST_CODE)

            //startActivity(Intent(activity, LoginActivity::class.java))

        }

    }

    private fun getCheckList(result:String, docID:String) {
        if(checkin_list.isEmpty()) {
            FireStoreInitial.initial().collection(Constants.CheckIn.collection_name)
                    .document(docID)
                    .get()
                    .addOnSuccessListener {
                        checkin_list.clear()
                        val s_list = it[Constants.CheckIn.checkList] as MutableMap<String, Boolean>
                        s_list.forEach { pair -> checkin_list[pair.key] = pair.value }
                        checkIn(result, docID)
                    }
                    .addOnFailureListener {
                        //TODO
                    }
                    .addOnCanceledListener {
                        //TODO
                    }
        } else {
            checkIn(result, docID)
        }
    }

    private fun checkIn(curr_id: String, docID: String) {
        Log.d("CHeckIN", "CALLED")
        val bool = checkin_list.containsKey(curr_id)
        Log.d("Is id in stu_list", "_" + bool.toString())
        checkin_list.forEach { pair ->
            Log.d("KEY", pair.key)
        }
        Log.d("USER_ID", curr_id)
        Log.d("CheckInList_SIZE", checkin_list.size.toString())
        Log.d("DOC_ID", docID)
        if(bool) {
            if(checkin_list[curr_id] == true) {
                Toast.makeText(context, "การลงเวลาล้มเหลว เนื่องจากท่านได้ทำการลงเวลาเรียนเรียบร้อยแล้ว", Toast.LENGTH_SHORT).show()
            } else {
                checkin_list[curr_id] = true
                val checkModel = CheckInModel(doc_id = docID, checkin_list = checkin_list)
                activity!!.startService(Intent(activity, FirestoreCheckInService::class.java).apply {
                    putExtra("model", checkModel)
                })
            }

        } else {
            Toast.makeText(context, "ไม่มีรายชื่อของคุณอยู่ในรายวิชานี้", Toast.LENGTH_SHORT).show()
        }
    }

    private val onCheckIn = object: DocumentCallback{
        override fun onCallback(doc: DocumentSnapshot) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onEmptyCallback() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onErrorCallback(ex: Exception) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    companion object {
        fun newInstance(@NonNull name: Any, @NonNull sheet_no: Any
        , @NonNull doc_id: Any, @NonNull UUID:Any): Fragment {
            val fragment = CheckInRealFragment()
            val bundle = Bundle()
            bundle.putString("course_name", name as String)
            bundle.putString("doc_id", doc_id as String)
            bundle.putInt("sheet_no", sheet_no as Int)
            bundle.putString("UUID", UUID as String)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("RESULT CODE", "Processing")
        if(requestCode == BARCODE_READER_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK) {
                Log.d("CHeckIN", "RESULT_OK")
                Log.d("CHeckIN", data!!.getStringExtra("docID"))
                getCheckList(data.getStringExtra("result") as String, data.getStringExtra("docID") as String)
            } else {
                Log.d("RESULT CODE", "RESULT_CODE not OK")
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("STATE", "Save State")
        outState.putString("docID", docID)
        Log.d("STATE", "Save State End")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("STATE", "Read State")
        if(savedInstanceState != null) {
            docID = savedInstanceState["docID"] as String
        }
    }

}