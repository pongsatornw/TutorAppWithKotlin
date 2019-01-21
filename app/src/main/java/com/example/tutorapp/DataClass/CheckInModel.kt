package com.example.tutorapp.DataClass

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class CheckInModel(
        internal val doc_id : String,
        internal val checkin_list : Map<String, Boolean>
): Parcelable