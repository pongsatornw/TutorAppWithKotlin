package com.example.tutorapp.Fragment.PeopleManagement

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tutorapp.R
import kotlinx.android.synthetic.main.fragment_people.view.*

class PeopleManagementFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_people, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /** Completed, can view, edit, and delete data. **/
        view.bt_viewpeople.setOnClickListener {
            fragmentManager!!.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.root_main, ViewPeopleFragment.newInstance())
                    .commit()
        }

        /** Complete **/
        view.bt_adddpeople.setOnClickListener {
            fragmentManager!!.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.root_main, AddPeopleFragment.newInstance())
                    .commit()
        }

    }

    companion object {
        fun newInstance(): Fragment {
            return PeopleManagementFragment()
        }
    }

}