package com.example.tutorapp

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.example.tutorapp.Fragment.CourseManagement.CourseManagementFragment
import com.example.tutorapp.Fragment.CourseManagement.CheckIn.SelecCourseToCheckInFragment
import com.example.tutorapp.Fragment.History.TeachingHistoryFragment
import com.example.tutorapp.Fragment.MainFragment
import com.example.tutorapp.Fragment.PeopleManagement.PeopleManagementFragment
import com.example.tutorapp.Fragment.Schedule.StudyingScheduleFragment
import com.example.tutorapp.Fragment.Schedule.TeachingScheduleFragment
import com.example.tutorapp.Fragment.UserInformationFragment.UserInfoFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import com.google.firebase.firestore.FirebaseFirestore
import com.yarolegovich.lovelydialog.LovelyCustomDialog
import kotlinx.android.synthetic.main.alert_course_dialog.*
import kotlinx.android.synthetic.main.nav_header_main.*
import org.w3c.dom.Text

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val BARCODE_READER_REQUEST_CODE = 1234
    private lateinit var db: FirebaseFirestore
    private var timer: CountDownTimer?=null
    private lateinit var role: String
    private lateinit var userId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        nav_view.menu.getItem(0).isChecked = true
        //nav_view.menu.getItem(0).isVisible = false

        val header: View = nav_view.getHeaderView(0)
        //val header: View = nav_view.inflateHeaderView(R.layout.nav_header_main)
        val namee = header.findViewById<TextView>(R.id.user_name)

        /*val emaill = header.findViewById<TextView>(R.id.user_email)*/

        //emaill.text = email1

        supportFragmentManager.beginTransaction()
                //.addToBackStack(null)
                .replace( R.id.root_main, MainFragment.newInstance())
                .commit()

    }

    private var email1 = ""

    override fun onBackPressed() {
        updateTimer()
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            val backStack = supportFragmentManager.backStackEntryCount
            Log.d("BACK_STACK", " " + backStack.toString())
            if( backStack != 0){
                supportFragmentManager.popBackStack()
            } else {
                // TODO("Ask if not main fragment")
                if(supportFragmentManager.findFragmentById(R.id.root_main)!!::class.java.simpleName == MainFragment.newInstance()::class.java.simpleName){
                    Dialog(this@MainActivity).apply{
                        setContentView(R.layout.alert_course_dialog)
                        setCanceledOnTouchOutside(true)
                        CompatBtn01.visibility = View.GONE
                        CompatBtn02.text = "ใช่"
                        CompatBtn03.text = "ไม่ใช่"
                        textView3.text = "ท่านต้องการออกจากระบบใช่หรือไม่"
                        CompatBtn02.setOnClickListener {
                            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                            finish()
                            dismiss()
                        }
                        CompatBtn03.setOnClickListener {
                            dismiss()
                        }
                    }.show()
                }
                else {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.root_main, MainFragment.newInstance())
                        .commit()
                    Log.d("MIAN", "NOT_MAIN")
                }
                //super.onBackPressed()
            }
        }
    }

    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }*/

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, LoginActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.

        when (item.itemId) {
            /**
             *  Not complete, main fragment still have no content!!!
             * */
            R.id.nav_main -> {
                supportFragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace( R.id.root_main, MainFragment.newInstance())
                        .commit()
            }
            /** Complete, can change password **/
            R.id.self_info -> {
                for (i in 0 until supportFragmentManager!!.backStackEntryCount) {
                    supportFragmentManager?.popBackStack()
                }
                supportFragmentManager.beginTransaction()
                        //.addToBackStack(null)
                        .replace( R.id.root_main, UserInfoFragment.newInstance())
                        .commit()
            }
            /** Completed, can add, view, edit, delete data!!! **/
            R.id.nav_people_management -> {
                for (i in 0 until supportFragmentManager!!.backStackEntryCount) {
                    supportFragmentManager?.popBackStack()
                }
                supportFragmentManager.beginTransaction()
                        //.addToBackStack(null)
                        .replace( R.id.root_main, PeopleManagementFragment.newInstance())
                        .commit()
            }
            /** Completed, can add, view, edit, delete data!!! **/
            R.id.nav_course_management -> {
                for (i in 0 until supportFragmentManager!!.backStackEntryCount) {
                    supportFragmentManager?.popBackStack()
                }
                supportFragmentManager.beginTransaction()
                        //.addToBackStack(null)
                        .replace( R.id.root_main, CourseManagementFragment.newInstance())
                        .commit()
            }
            /**
             *      Content complete,
             * */
            R.id.nav_schedule_st -> {
                for (i in 0 until supportFragmentManager!!.backStackEntryCount) {
                    supportFragmentManager?.popBackStack()
                }
                supportFragmentManager.beginTransaction()
                        //.addToBackStack(null)
                        .replace( R.id.root_main, StudyingScheduleFragment.newInstance())
                        .commit()
            }
            /**
             *      Content complete,
             * */
            R.id.nav_schedule_in -> {
                for (i in 0 until supportFragmentManager!!.backStackEntryCount) {
                    supportFragmentManager?.popBackStack()
                }
                supportFragmentManager.beginTransaction()
                        //.addToBackStack(null)
                        .replace( R.id.root_main, TeachingScheduleFragment.newInstance())
                        .commit()
            }
            /**
             *      TODO("CheckIn with QR Code")
             *      Content semi complete,
             * */
            R.id.nav_checkin -> {
                for (i in 0 until supportFragmentManager!!.backStackEntryCount) {
                    supportFragmentManager?.popBackStack()
                }
                supportFragmentManager.beginTransaction()
                        //.addToBackStack(null)
                        .replace( R.id.root_main, SelecCourseToCheckInFragment.newInstance())
                        .commit()
            }
            R.id.nav_studying_history -> {
                for (i in 0 until supportFragmentManager!!.backStackEntryCount) {
                    supportFragmentManager?.popBackStack()
                }
                supportFragmentManager.beginTransaction()
                        //.addToBackStack(null)
                        .replace( R.id.root_main, /**temp use*/Fragment())
                        .commit()
            }
            /**
             *      Successful
             * */
            R.id.nav_teaching_history -> {
                for (i in 0 until supportFragmentManager!!.backStackEntryCount) {
                    supportFragmentManager?.popBackStack()
                }
                supportFragmentManager.beginTransaction()
                        //.addToBackStack(null)
                        .replace( R.id.root_main, TeachingHistoryFragment.newInstance())
                        .commit()
            }
            R.id.nav_log_out -> {
                Dialog(this@MainActivity).apply{
                    setContentView(R.layout.alert_course_dialog)
                    setCanceledOnTouchOutside(true)
                    CompatBtn01.visibility = View.GONE
                    CompatBtn02.text = "ใช่"
                    CompatBtn03.text = "ไม่ใช่"
                    textView3.text = "ท่านต้องการออกจากระบบใช่หรือไม่"
                    CompatBtn02.setOnClickListener {
                        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                        finish()
                        dismiss()
                    }
                    CompatBtn03.setOnClickListener {
                        dismiss()
                    }
                }.show()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onResume(){
        super.onResume()
        startTimer()

        role = intent.getStringExtra(Constants.User_Profile.role)
        userId = intent.getStringExtra(Constants.User_Profile.user_id)
        val header: View = nav_view.getHeaderView(0)
        //val header: View = nav_view.inflateHeaderView(R.layout.nav_header_main)
        val namee = header.findViewById<TextView>(R.id.user_name)
        val rolee = header.findViewById<TextView>(R.id.user_email)
        namee.text = userId
        rolee.text = role
        initialMenu(nav_view.menu)

    }

    private fun updateTimer() {
        if (timer != null) {
            timer?.cancel()
            timer = null
            startTimer()
        } else {
            startTimer()
        }

    }

    private fun startTimer() {
        timer = object: CountDownTimer(1000*60*30, 1000*30){

            override fun onTick(millisUntilFinished: Long) {
                Log.d("Tick", "Tick")
            }

            override fun onFinish() {
                Log.d("Tick", "Finish")
                finish()
            }
        }.start()
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d("ACTION", "ACTION DOWN")
                updateTimer()
            }
            MotionEvent.ACTION_MOVE -> {
                updateTimer()
            }
            MotionEvent.ACTION_UP -> {
                Log.d("ACTION", "ACTION UP")
                updateTimer()
            }
        }
        return super.dispatchTouchEvent(event)
    }

    fun getCurrentID() = userId

    fun getRole() = role

    private fun initialMenu(menu: Menu) {
        menu.getItem(7).isVisible = false
        when (getRole()) {
            "นักเรียน"   -> {
                menu.getItem(2).isVisible = false
                menu.getItem(3).isVisible = false
                menu.getItem(5).isVisible = false
                menu.getItem(6).isVisible = false
                menu.getItem(8).isVisible = false
            }
            "อาจารย์"   -> {
                menu.getItem(2).isVisible = false
                menu.getItem(3).isVisible = false
                menu.getItem(4).isVisible = false
                menu.getItem(7).isVisible = false
            }
            "พนักงาน"   -> {
                menu.getItem(4).isVisible = false
                menu.getItem(5).isVisible = false
                menu.getItem(6).isVisible = false
                menu.getItem(7).isVisible = false
                menu.getItem(8).isVisible = false
            }
            else    -> Log.d("Role", "None")
        }
    }

    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)

        savedInstanceState.putString("userId", getCurrentID())
        savedInstanceState.putString("role", getRole())
    }

    public override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        userId = savedInstanceState?.getString("userId")!!
        role = savedInstanceState.getString("role")!!
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    private var dataa = ""

    private fun getDataa() = dataa


}
