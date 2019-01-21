package com.example.tutorapp

import com.google.firebase.firestore.DocumentSnapshot

interface DocumentCallback {

    fun onCallback(doc: DocumentSnapshot)
    fun onEmptyCallback()
    fun onErrorCallback(ex: Exception)

}
