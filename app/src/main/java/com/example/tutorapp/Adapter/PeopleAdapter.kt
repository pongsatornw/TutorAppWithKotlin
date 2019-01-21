package com.example.tutorapp.Adapter

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tutorapp.R
import com.google.firebase.firestore.DocumentSnapshot
import android.widget.Toast
import com.example.tutorapp.DataClass.AddPeopleModel
import com.example.tutorapp.Fragment.PeopleManagement.AddPeopleFragment
import com.example.tutorapp.Utility.FireStoreInitial
import com.google.firebase.firestore.FirebaseFirestore
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.android.synthetic.main.alert_course_dialog.*
import kotlinx.android.synthetic.main.cardview_people.view.*
import java.lang.StringBuilder


class PeopleAdapter(private val context: Context, private val list: MutableList<DocumentSnapshot>
                    , private val focus: Int) : RecyclerView.Adapter<PeopleAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): PeopleAdapter.ViewHolder {

        val view = LayoutInflater.from(context)
                .inflate(R.layout.cardview_people, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (list[position]["role"] != "นักเรียน") {
            holder.itemView.people_id_layout.visibility = View.GONE
        }

        holder.itemView.setOnLongClickListener {view ->

            Dialog(context).apply {
                setContentView(R.layout.alert_course_dialog)
                setCanceledOnTouchOutside(true)
                CompatBtn01.visibility = View.GONE
                CompatBtn02.text = "แก้ไขข้อมูลบัญชีผู้ใช้งาน"
                CompatBtn03.text = "ลบข้อมูลบัญชีผู้ใช้งาน"
                CompatBtn01.setOnClickListener {
                    Log.d("Helo", "Halo")
                    dismiss()
                }
                CompatBtn02.setOnClickListener {
                    dismiss()
                    val model = AddPeopleModel(
                            user_id = list[position]["user_id"] as String,
                            fname  = list[position]["fname"] as String,
                            lname = list[position]["lname"] as String,
                            gender = list[position]["gender"] as String,
                            tel_no = list[position]["tel_no"] as String,
                            email = list[position]["email"] as String,
                            role = list[position]["role"] as String
                    )
                    val args = Bundle()
                    args.putParcelable("peopleModel", model)
                    args.putString("user_id", list[position]["user_id"] as String)
                    val fragment = AddPeopleFragment.newInstance()
                    fragment.arguments = args
                    (view.context as FragmentActivity).supportFragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.root_main, fragment)
                            .commit()
                    dismiss()
                }
                CompatBtn03.setOnClickListener {
                    dismiss()
                    val userID = list[position]["user_id"] as String
                    FirebaseFirestore.getInstance().collection("User_Profile")
                            .document(list[position].id).delete().apply {
                                addOnCompleteListener { task ->
                                    if (task.isComplete){
                                        Log.d("Complete", "Task complete")
                                    }
                                    if (task.isSuccessful) {
                                        deleteAccount(FireStoreInitial.initial(),userID)
                                    } else {
                                        Log.d("Complete", "Task complete but not success")
                                    }
                                }
                                addOnCanceledListener {
                                    Log.d("Cancel", "Delete data canceled")
                                }
                                addOnFailureListener { ex ->
                                    ex.printStackTrace()
                                }
                            }
                    }
            }.show()

                return@setOnLongClickListener true
        }

        holder.itemView.people_name.text = StringBuilder().apply {
            append(list[position]["fname"])
            append(" ")
            append(list[position]["lname"])
        }
        if(list[position]["user_id"] != null)
            holder.itemView.people_id.text = list[position]["user_id"] as String
        holder.itemView.people_gender.text = list[position]["gender"] as String
        holder.itemView.people_role.text = list[position]["role"] as String
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = list.size

    private fun deleteAccount(db: FirebaseFirestore, user_id: String) {
        db.collection("Account").whereEqualTo("user_id", user_id)
                .limit(1)
                .get()
                .addOnCompleteListener {
                    if (it.result!!.size() == 1){
                        db.collection("Account").document(it.result!!.documents[0].id)
                                .delete()
                                .addOnCompleteListener {
                                    FancyToast.makeText(context, "การลบข้อมูลสำเร็จ", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()

                                }
                                .addOnCanceledListener {
                                    FancyToast.makeText(context, "การลบข้อมูลถูกยกเลิก", FancyToast.LENGTH_SHORT, FancyToast.CONFUSING, false).show()
                                }
                                .addOnFailureListener {
                                    FancyToast.makeText(context, "การลบข้อมูลล้มเหลว กรุณาลองใหม่ในภายหลัง", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
                                }
                    } else {

                    }
                }
                .addOnFailureListener{

                }
                .addOnCanceledListener {

                }
    }
}
