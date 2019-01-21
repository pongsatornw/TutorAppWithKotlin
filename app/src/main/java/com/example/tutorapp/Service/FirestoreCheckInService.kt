package com.example.tutorapp.Service

import android.app.IntentService
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.tutorapp.Constants
import com.example.tutorapp.DataClass.CheckInModel
import com.example.tutorapp.Utility.FireStoreInitial
import com.google.firebase.firestore.SetOptions

class FirestoreCheckInService: IntentService("FirestoreCheckInService") {

    override fun setIntentRedelivery(enabled: Boolean) {
        super.setIntentRedelivery(true)
    }

    override fun onHandleIntent(intent: Intent?) {
        val model = intent!!.getParcelableExtra("model") as CheckInModel

        FireStoreInitial.initial().collection(Constants.CheckIn.collection_name)
                .document(model.doc_id)
                .set(mutableMapOf<String, Any>(Constants.CheckIn.checkList to model.checkin_list), SetOptions.mergeFields(Constants.CheckIn.checkList))
                .addOnCompleteListener {
                    if(it.isSuccessful) {
                        Toast.makeText(applicationContext, "ลงเวลาเรียนสำเร็จ", Toast.LENGTH_SHORT).show()
                        Log.d("SUCCESS", "TRUE")
                    } else {
                        Log.d("SUCCESS", "FALSE")
                    }
                }
                .addOnFailureListener {
                    // TODO
                }
    }

}