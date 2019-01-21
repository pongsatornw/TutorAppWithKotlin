package com.example.tutorapp.DataClass

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class AddPeopleModel(
        val user_id: String,
        val fname: String,
        val lname: String,
        val gender: String,
        val tel_no: String,
        val email: String,
        val role: String) : Parcelable