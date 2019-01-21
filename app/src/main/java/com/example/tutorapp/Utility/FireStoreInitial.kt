package com.example.tutorapp.Utility

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class FireStoreInitial {

    companion object {
        fun initial(): FirebaseFirestore{
            val db = FirebaseFirestore.getInstance()
            val settings = FirebaseFirestoreSettings.Builder()
                    .setTimestampsInSnapshotsEnabled(true)
                    .build()
            db.firestoreSettings = settings
            return db
        }
    }
}