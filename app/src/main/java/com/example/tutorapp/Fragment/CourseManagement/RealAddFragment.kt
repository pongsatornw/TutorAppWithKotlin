package com.example.tutorapp.Fragment.CourseManagement

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tutorapp.Constants
import com.example.tutorapp.DataClass.AddCourseModel
import com.example.tutorapp.R
import com.example.tutorapp.Utility.FireStoreInitial
import com.example.tutorapp.ValueCallback
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.fragment_real_add_fragment.view.*

class RealAddFragment: Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_real_add_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = this.arguments
        var model: AddCourseModel? = null

        view.btn_res.setOnClickListener { view.edt_id.text = null }

        if(bundle != null) {
            model = bundle.getParcelable("model")
        }
        view.text_id.text = "ลงทะเบียนนักเรียนสู่รายวิชา"
        view.btn_add.setOnClickListener {
            view.layout_progress.visibility = View.VISIBLE
            view.progressBar.animate()
            checkAccount(object: ValueCallback{
                override fun onCallback(pair: Pair<String, Boolean>) {
                    Log.d("Pair in callback", pair.second.toString())
                    if (pair.second) {
                        FireStoreInitial.initial().collection(Constants.Course.collection_name)
                                .whereEqualTo("UUID", model?.UUID)
                                .limit(1)
                                .get()
                                .addOnCompleteListener { task ->

                                    if (task.isSuccessful) {
                                        if (task.result!!.size() > 0) {
                                            val doc = task.result!!.documents[0]
                                            val doc_id = doc.id
                                            val stu_list = doc["stu_list"] as MutableMap<String, String>

                                            if( isIDinClass(stu_list, view.edt_id.text.toString()) ) {
                                                //TODO AlertDialog
                                                Toast.makeText(context, "นักศึกษาได้ลงทะเบียนรายวิชานี้เรียบร้อยแล้ว", Toast.LENGTH_SHORT).show()

                                                Log.i("isIDinClass", "This ID already registered!")
                                            } else {
                                                val map = mutableMapOf<String, Any>()
                                                map.clear()
                                                stu_list[view.edt_id.text.toString()] = pair.first
                                                map["stu_list"] = stu_list
                                                FireStoreInitial.initial().collection(Constants.Course.collection_name)
                                                        .document(doc_id)
                                                        .set(map, SetOptions.mergeFields("stu_list"))
                                                        .addOnSuccessListener {
                                                            Toast.makeText(context, "ลงทะเบียนสำเร็จ", Toast.LENGTH_SHORT).show()
                                                        }
                                                        .addOnFailureListener{
                                                            Toast.makeText(context, "ลงทะเบียนล้มเหลว", Toast.LENGTH_SHORT).show()
                                                        }

                                            }

                                        } else {
                                            Log.i("Result Size", "Size <= 0")
                                        }
                                    } else {
                                        // isSuccessFul return False
                                        Log.i("isSuccessFul", "isSuccessFul return false")
                                    }
                                }
                        view.layout_progress.visibility = View.GONE
                        view.progressBar.clearAnimation()
                    } else {
                        // If user_id not found
                        //TODO
                        Toast.makeText(context, "ไม่พบรหัสนักศึกษาดังกล่าว", Toast.LENGTH_SHORT).show()
                        Log.i("USER_ID", "User Id not Found!!!")
                        view.layout_progress.visibility = View.GONE
                        view.progressBar.clearAnimation()
                    }
                }
            }, view.edt_id.text.toString())


        }
    }

    /**
     *         Return true if account found.
     */
    private fun checkAccount(myCallback: ValueCallback, userID: String) {
        var result = false
        var name = ""
        FireStoreInitial.initial().collection(Constants.User_Profile.collection_name)
                .whereEqualTo("user_id", userID)
                .limit(1)
                .get()
                .addOnCompleteListener {
                    if(it.isSuccessful) {
                        result = !it.result!!.isEmpty
                        Log.d("IsEmpty Result", result.toString())
                        if (result) {
                            name = it.result!!.documents[0]["fname"] as String +
                                    " " +
                                    it.result!!.documents[0]["lname"] as String
                        }
                        myCallback.onCallback(Pair(name, result))
                    }
                }

    }

    /**
     * Return true if found, false if not found
     */
    private fun isIDinClass(map: MutableMap<String, String>, id: String): Boolean {
        var isRegistered = false
        map.forEach {
            if(it.key == id) {
                isRegistered = true
                return@forEach
            }
        }
       return isRegistered
    }

    companion object {
        fun newInstance(model: AddCourseModel): Fragment {
            val bundle = Bundle()
            bundle.putParcelable("model", model)
            val fragment = RealAddFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

}
