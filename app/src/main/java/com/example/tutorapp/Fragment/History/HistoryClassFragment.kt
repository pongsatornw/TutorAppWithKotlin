package com.example.tutorapp.Fragment.History

import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.tutorapp.Constants
import com.example.tutorapp.ListCallbackWithErrorHandler
import com.example.tutorapp.R
import com.example.tutorapp.Utility.FireStoreInitial
import com.google.firebase.firestore.DocumentSnapshot
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.android.synthetic.main.fragment_table_view.view.*

class HistoryClassFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_table_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = this.arguments
        if(args != null) {
            callStudentCheckInHistory(args["uuid"] as String, args["time"] as Long)
        } else {
            //TODO("argument is null")
        }
    }

    private fun callStudentCheckInHistory(uuid: String, time: Long) {
        FireStoreInitial.initial().collection(Constants.CheckIn.collection_name)
                .whereEqualTo(Constants.CheckIn.UUID, uuid)
                .whereEqualTo(Constants.CheckIn.time, time)
                .limit(1)
                .get()
                .addOnSuccessListener {
                    if(it.size() > 0) {
                        mCallback.onCallback(it.documents)
                    } else {
                        mCallback.onEmptyCallback()
                    }
                }
                .addOnFailureListener{
                    mCallback.onErrorCallback(it)
                }
                .addOnCanceledListener {

                }
    }

    private val mCallback = object:ListCallbackWithErrorHandler<List<DocumentSnapshot>, Exception>{
        override fun onCallback(data: List<DocumentSnapshot>) {
            tableGenerate(data)
        }

        override fun onErrorCallback(exception: Exception) {
            FancyToast.makeText(context, "เกิดความผิดพลาดในการร้องขอข้อมูล กรุณาลองใหม่ในภายหลัง", Toast.LENGTH_SHORT, FancyToast.ERROR, false).show()
        }

        override fun onEmptyCallback() {
            FancyToast.makeText(context, "ไม่พบข้อมูลการเข้าชั้นเรียนของนักเรียนในใบเช็คชื่อนี้", Toast.LENGTH_SHORT, FancyToast.CONFUSING, false).show()
            fragmentManager!!.popBackStack()
        }
    }

    private fun tableGenerate(list: List<DocumentSnapshot>) {
        val data = list[0]
        val checkMap = data[Constants.CheckIn.checkList] as MutableMap<String, Boolean>
        //val table = TableView(activity!!)
        val sortMap = checkMap.toSortedMap()
        val tableLayout = TableLayout(activity)
        val lp = TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        val lpWeight_3f = TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        lpWeight_3f.weight = 3F

        val lpWeight_7f = TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        lpWeight_7f.weight = 7F

        tableLayout.apply {
            layoutParams = lp
            isShrinkAllColumns = true
        }
        var i = 0

        if(sortMap.size == 0){
            mCallback.onEmptyCallback()
        }
        sortMap.forEach{
            if(i == 0) {
                val row =TableRow(activity)
                row.addView(TextView(activity).
                    apply {
                        text = "รหัสนักศึกษา"
                        gravity = Gravity.CENTER
                        layoutParams = lpWeight_3f
                        background = ContextCompat.getDrawable(context!!, R.drawable.border)
                        setPadding(0, 10, 0, 10)
                        textSize = 18F
                        setTypeface(null, Typeface.BOLD)
                    }, 0
                )

                row.addView(TextView(activity).
                    apply {
                        text = "การเข้าชั้นเรียน"
                        gravity = Gravity.CENTER
                        layoutParams = lpWeight_7f
                        background = ContextCompat.getDrawable(context!!, R.drawable.border)
                        setPadding(0, 10, 0, 10)
                        textSize = 18F
                        setTypeface(null, Typeface.BOLD)
                    }, 1
                )
                tableLayout.addView(row)
            }

            val row =TableRow(activity)
            row.weightSum = 10F
            row.layoutParams = lp

            val tv_id = TextView(activity)
            val tv_check = TextView(activity)
            tv_id.apply {
                text = it.key
                layoutParams = lpWeight_3f
                background = ContextCompat.getDrawable(context!!, R.drawable.border)
                gravity = Gravity.CENTER
                textSize = 16F
                setPadding(0, 6, 0, 6)
            }
            if(it.value) tv_check.text = "มา" else {
                tv_check.setTextColor(ContextCompat.getColor(context!!, R.color.sun_red) )
                tv_check.text = "ขาด"
            }
            tv_check.apply {
                layoutParams = lpWeight_7f
                background = ContextCompat.getDrawable(context!!, R.drawable.border)
                gravity = Gravity.CENTER
                setPadding(0, 6, 0, 6)
                textSize = 16F
            }

            row.addView(tv_id, 0)
            row.addView(tv_check, 1)
            if (i % 2 == 0) row.background = ContextCompat.getDrawable(context!!, R.color.weak_orange)
            tableLayout.addView(row)
            Log.d("NewRow", "new row")
            i++

        }
        view!!.table_layout.addView(tableLayout)
    }

    companion object {
        fun newInstance(uuid: String, time: Long): Fragment {
            val fragment = HistoryClassFragment()
            val bundle = Bundle()
            bundle.putString("uuid", uuid)
            bundle.putLong("time", time)
            fragment.arguments = bundle
            return fragment
        }
    }



}