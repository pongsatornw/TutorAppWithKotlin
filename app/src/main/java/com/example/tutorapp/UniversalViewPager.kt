package com.example.tutorapp

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.util.Log
import java.lang.Exception

class UniversalViewPager(fm: FragmentManager, private val fList: MutableList<Fragment>): FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when(position) {
            in IntRange(0, fList.size - 1) -> fList[position]
            else -> throw Exception("")
        }
    }

    override fun getCount() = fList.size

}