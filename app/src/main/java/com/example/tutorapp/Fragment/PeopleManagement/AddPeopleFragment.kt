package com.example.tutorapp.Fragment.PeopleManagement

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.tutorapp.DataClass.AddCourseModel
import com.example.tutorapp.DataClass.AddPeopleModel
import com.example.tutorapp.R
import com.example.tutorapp.Service.FirestoreAddPeopleService
import com.example.tutorapp.Service.FirestoreEditPeopleService
import kotlinx.android.synthetic.main.fragment_addcourse.*
import kotlinx.android.synthetic.main.fragment_addcourse.view.*
import kotlinx.android.synthetic.main.fragment_addpeople.view.*


class AddPeopleFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_addpeople, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = this.arguments

        if(bundle != null) {
            val model = bundle.getParcelable<AddPeopleModel>("peopleModel")
            if (model != null) {
                view.editText.setText(model.fname)
                view.editText8.setText(model.lname)
                view.editText9.setText(model.gender)
                view.editText10.setText(model.tel_no)
                view.editText11.setText(model.email)
                view.editText12.setText(model.role)
                resetDataForEdit(view, model)
                confirmDataForEdit(view)

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


        autoCompleteInitial()

    }

    private fun autoCompleteInitial() {
        val genderList = arrayListOf("ชาย", "หญิง")
        val roleList = arrayListOf("นักเรียน", "อาจารย์", "พนักงาน")

        val auto1 = view!!.findViewById<AutoCompleteTextView>(R.id.editText9)
        auto1.setAdapter(ArrayAdapter<String>(activity!!,
                android.R.layout.simple_dropdown_item_1line,
                genderList)
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


        val auto2 = view!!.findViewById<AutoCompleteTextView>(R.id.editText12)
        auto2.setAdapter(ArrayAdapter<String>(activity!!,
                        android.R.layout.simple_dropdown_item_1line,
                        roleList)
                )
        auto2.threshold = 0
        auto2.showSoftInputOnFocus = false
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

    }

    private fun resetDataForAdd(view: View) {
        view.button.setOnClickListener {
            view.editText.text = null
            view.editText8.text = null
            view.editText9.text = null
            view.editText10.text = null
            view.editText11.text = null
            view.editText12.text = null
        }
    }

    private fun confirmDataForAdd(view:View) {
        view.button2.setOnClickListener {
            if( view.editText.text != null && view.editText8.text !=null
                && view.editText9.text != null && view.editText10.text !=null
                && view.editText11.text != null && view.editText12.text !=null

                && view.editText.text.toString() != "" && view.editText8.text.toString() != ""
                && view.editText9.text.toString() != "" && view.editText10.text.toString() != ""
                && view.editText11.text.toString() != "" && view.editText12.text.toString() != "") {

            val addPeopleModel = AddPeopleModel("", view.editText.text.toString(), view.editText8.text.toString()
                    ,view.editText9.text.toString(), view.editText10.text.toString(), view.editText11.text.toString(),
                    view.editText12.text.toString() )
            AlertDialog.Builder(activity)
                    .setCancelable(false)
                    .setTitle("ยืนยันการเพิ่มข้อมูลบุคลากร")
                    .setPositiveButton("ยืนยัน"){dialog, _ ->
                        activity!!.startService(Intent(activity,
                                FirestoreAddPeopleService::class.java)
                                .putExtra("people_model", addPeopleModel)
                        )
                        dialog.dismiss()
                    }
                    .setNegativeButton("ยกเลิก"){dialog, _ ->
                        dialog.dismiss()
                    }.create().show()

            } else {
                AlertDialog.Builder(activity)
                        .setTitle("พบข้อผิดพลาด")
                        .setMessage("กรุณากรอกข้อมูลให้ครบทุกช่อง")
                        .setCancelable(false)
                        .setNeutralButton("รับทราบ"){dialog, _ ->
                            dialog.cancel()
                            /**fragmentManager!!.popBackStack(null ,FragmentManager.POP_BACK_STACK_INCLUSIVE)
                            fragmentManager!!.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.root_main, MainFragment.newInstance())
                            .commit()*/
                        }
                        .create().show()
            }
        }
    }

    private fun resetDataForEdit(view:View, model: AddPeopleModel) {
        view.button.setOnClickListener {
            view.editText.setText(model.fname)
            view.editText8.setText(model.lname)
            view.editText9.setText(model.gender)
            view.editText10.setText(model.tel_no)
            view.editText11.setText(model.email)
            view.editText12.setText(model.role)
        }
    }

    private fun confirmDataForEdit(view:View) {
        view.button2.text = "แก้ไขข้อมูล"
        view.button2.setOnClickListener {
            if( view.editText.text != null && view.editText8.text !=null
                    && view.editText9.text != null && view.editText10.text !=null
                    && view.editText11.text != null && view.editText12.text !=null

                    && view.editText.text.toString() != "" && view.editText8.text.toString() != ""
                    && view.editText9.text.toString() != "" && view.editText10.text.toString() != ""
                    && view.editText11.text.toString() != "" && view.editText12.text.toString() != "") {

                val editPeopleModel = AddPeopleModel(view.editText.text.toString(), view.editText8.text.toString()
                        ,view.editText9.text.toString(), view.editText10.text.toString(), view.editText11.text.toString(),
                        view.editText12.text.toString(), "")
                AlertDialog.Builder(activity)
                        .setCancelable(false)
                        .setTitle("ยืนยันการแก้ไขข้อมูลบุคลากร")
                        .setPositiveButton("ยืนยัน"){dialog, _ ->
                            activity!!.startService(Intent(activity,
                                    FirestoreEditPeopleService::class.java)
                                    .putExtra("people_model", editPeopleModel)
                            )
                            dialog.dismiss()
                        }
                        .setNegativeButton("ยกเลิก"){dialog, _ ->
                            dialog.dismiss()
                        }.create().show()

            } else {
                AlertDialog.Builder(activity)
                        .setTitle("พบข้อผิดพลาด")
                        .setMessage("กรุณากรอกข้อมูลให้ครบทุกช่อง")
                        .setCancelable(false)
                        .setNeutralButton("รับทราบ"){dialog, _ ->
                            dialog.cancel()
                            /**fragmentManager!!.popBackStack(null ,FragmentManager.POP_BACK_STACK_INCLUSIVE)
                            fragmentManager!!.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.root_main, MainFragment.newInstance())
                            .commit()*/
                        }
                        .create().show()
            }
        }
    }

    companion object {
        fun newInstance(): Fragment{
            return AddPeopleFragment()
        }
    }
}