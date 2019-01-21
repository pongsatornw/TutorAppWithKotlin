package com.example.tutorapp.Service

import android.app.IntentService
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.tutorapp.DataClass.AddPeopleModel
import com.example.tutorapp.Utility.FireStoreInitial
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


class FirestoreEditPeopleService: IntentService("FirestoreEditPeopleService") {

    private lateinit var dataData: MutableMap<String, Any>

    override fun setIntentRedelivery(enabled: Boolean) {
        super.setIntentRedelivery(true)
    }

    override fun onHandleIntent(intent: Intent?) {
        val db = FireStoreInitial.initial()

        dataData = mutableMapOf()
        val peopleModel = intent!!.getParcelableExtra<AddPeopleModel>("people_model")
        dataData["fname"] = peopleModel.fname
        dataData["lname"] = peopleModel.lname
        dataData["gender"] = peopleModel.gender
        dataData["tel_no"] = peopleModel.tel_no
        dataData["email"] = peopleModel.email
        dataData["role"] = peopleModel.role
        dataData["user_id"] = peopleModel.user_id
        //accountData["InitialVector"] = byteArrayOf("HelloWorld".toByte())

        editUserProfile(db, dataData, intent.getStringExtra("user_id"))

    }

    private fun editUserProfile(db: FirebaseFirestore, map: MutableMap<String, Any>, docID: String ){
        db.collection("User_Profile").document(docID)
                .set(map, SetOptions.mergeFields(mutableListOf("fname", "lname", "gender"
                ,"tel_no", "email", "role")))
                .addOnSuccessListener {
                    Toast.makeText(applicationContext, "การแก้ไขข้อมูลบุคลากรสำเร็จ", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    e ->
                    Toast.makeText(applicationContext, "การแก้ไขข้อมูลบุคลากรล้มเหลว", Toast.LENGTH_SHORT).show()
                }
    }
}