package com.example.tutorapp.Fragment.History

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tutorapp.Constants
import com.example.tutorapp.DataClass.AddCourseModelExtend
import com.example.tutorapp.ListCallbackWithErrorHandler
import com.example.tutorapp.R
import com.example.tutorapp.Utility.FireStoreInitial
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.cardview_universal.view.*
import kotlinx.android.synthetic.main.fragment_recycler_view.view.*
import java.lang.StringBuilder
import java.text.DateFormat

class RealTeachingHistoryFragment: Fragment() {

    private lateinit var model: AddCourseModelExtend

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recycler_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = this.arguments
        view.prgBar04.visibility = View.GONE
        view.prgBar04?.clearAnimation()
        if(args != null) {
            model = args["model"] as AddCourseModelExtend
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(savedInstanceState != null) {
            model = savedInstanceState["model"] as AddCourseModelExtend
        }
    }


    override fun onResume() {
        super.onResume()

        view?.prgBar04?.visibility = View.VISIBLE
        view?.prgBar04?.animate()
        FireStoreInitial.initial().collection(Constants.CheckIn.collection_name)
                .whereEqualTo(Constants.CheckIn.UUID, model.UUID)
                .orderBy(Constants.CheckIn.time, Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener {
                    if(it.size() > 0) {
                        mCallback.onCallback(it.documents)
                        view?.prgBar04?.visibility = View.GONE
                        view?.prgBar04?.clearAnimation()
                    } else {
                        mCallback.onEmptyCallback()
                        view?.prgBar04?.visibility = View.GONE
                        view?.prgBar04?.clearAnimation()
                    }
                }
                .addOnFailureListener {
                    mCallback.onErrorCallback(it)
                    view?.prgBar04?.visibility = View.GONE
                    view?.prgBar04?.clearAnimation()
                }
    }

    private fun recyclerView(list: List<DocumentSnapshot>) {
        view!!.recycler_view.apply {
            setHasFixedSize(true)
            adapter = Adapter(context, list, model.course_name)
            layoutManager = LinearLayoutManager(context)
        }
    }

    private val mCallback = object: ListCallbackWithErrorHandler<List<DocumentSnapshot>, Exception>{

        override fun onCallback(data: List<DocumentSnapshot>) {
            recyclerView(data)
        }

        override fun onErrorCallback(exception: Exception) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onEmptyCallback() {
            Toast.makeText(context, "ไม่พบข้อมูลประวัติการสอน", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("model", model)
    }

    private class Adapter(
            private val context: Context,
            private val list: List<DocumentSnapshot>,
            private val course_name: String
    ): RecyclerView.Adapter<Adapter.ViewHolder>(){

        inner class ViewHolder(view:View): RecyclerView.ViewHolder(view)

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.cardview_universal , viewGroup, false)
            val holder = ViewHolder(view)

            holder.itemView.elevation = 10F
            holder.itemView.layout_id04.visibility = View.GONE
            holder.itemView.setOnLongClickListener {

                (context as FragmentActivity).supportFragmentManager
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.root_main, HistoryClassFragment.newInstance(
                                list[holder.adapterPosition]["UUID"] as String,
                                list[holder.adapterPosition]["time"] as Long
                        ))
                        .commit()
                return@setOnLongClickListener true
            }
            return holder
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val miniView = holder.itemView
            if(position == 0) {
                miniView.card_background.background = ContextCompat
                        .getDrawable(holder.itemView.context, R.drawable.cardview_background_v2)
            } else {
                miniView.card_background.background = ContextCompat
                        .getDrawable(holder.itemView.context, R.drawable.cardview_background)
            }
            miniView.txt_id01.text = course_name
            miniView.txt_id02.text = StringBuilder().apply {
                append("คาบเรียนที่ ")
                append( (list.size - position).toString())
            }.toString()
            miniView.txt_id03.text = DateFormat.getDateTimeInstance(
                    DateFormat.DEFAULT, DateFormat.DEFAULT).format(list[position]["time"] as Long)
        }

        override fun getItemCount() = list.size

    }

    companion object {
        fun newInstance(model: AddCourseModelExtend): Fragment {
            val fragment = RealTeachingHistoryFragment()
            val bundle = Bundle()
            bundle.putParcelable("model", model)
            fragment.arguments = bundle
            return fragment
        }
    }

}