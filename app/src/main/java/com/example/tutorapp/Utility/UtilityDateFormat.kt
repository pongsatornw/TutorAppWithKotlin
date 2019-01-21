package com.example.tutorapp.Utility

import android.icu.text.DateFormat
import android.os.Build
import android.util.Log
import com.google.firebase.Timestamp
import java.util.*

class UtilityDateFormat {

    companion object {

        fun dateTxtToDate(txtTime: String): Date{
            return if( Build.VERSION.SDK_INT < 24) {
                val dateFormat = java.text.DateFormat.getDateInstance(java.text.DateFormat.DEFAULT)
                dateFormat.parse(txtTime)
            }
            else {
                val dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT)

                dateFormat.parse(txtTime)
            }
            Log.d("", "")
        }

        fun dateToDateTxt(timestamp: Timestamp): String {

            return if (Build.VERSION.SDK_INT < 24) {
                val dateFormat = java.text.DateFormat.getDateInstance(java.text.DateFormat.DEFAULT)
                dateFormat.format(timestamp.toDate())
            } else {
                val dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT)
                dateFormat.format(timestamp.toDate())
            }
        }

    }

}