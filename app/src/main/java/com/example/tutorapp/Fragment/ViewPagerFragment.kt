package com.example.tutorapp.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tutorapp.R
import kotlinx.android.synthetic.main.fragment_viewpager.*

class ViewPagerFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_viewpager, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        /*val adapter = ViewPagerAdapter(fragmentManager!!)
        viewpager.adapter = adapter*/
    }

    companion object {
        fun newInstance(): Fragment{
            return ViewPagerFragment()
        }
    }
}