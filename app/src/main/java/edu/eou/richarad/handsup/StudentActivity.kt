/* StudentActivity Class

Description:
    Allows the student to go the activity that will register them a class or to the activity where they manually check
    that they are present in class.

Created by: Allia Richardson
Course: Software Engineering (CS 380)
Term: Spring 2019
*/

package edu.eou.richarad.handsup

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.View
import android.widget.Button
import java.io.Serializable

class StudentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)

        //retrieves curUser from LoginActivity
        val curUser = intent?.extras?.get("CURRENT_USER") as CurrentUser
        val courseList = intent.getSerializableExtra("COURSE_LIST")
                as MutableList<CurrentCourses>

        //If the user presses the register button
        val registerButton = findViewById<Button>(R.id.registrationButton)
        registerButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(
                this@StudentActivity, StudentRegisterActivity::class.java)
            intent.putExtra("CURRENT_USER", curUser)
            startActivity(intent)
        })//end of setOnClickListener

        //If the user presses the
        val attendanceButton = findViewById<Button>(R.id.attendanceButton)
        attendanceButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(
                this@StudentActivity, StudentAttendanceActivity::class.java)
            intent.putExtra("CURRENT_USER", curUser)
            intent.putExtra("COURSE_LIST", courseList as Serializable)
            startActivity(intent)
        })//end of setOnClickListener

        //sets up navigation view and selectListener
        val navigation = findViewById<View>(R.id.StudentActNavView) as BottomNavigationView
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }//end of AdminActivity

    /*  onNavigationItemSelected
    Description:
        A menu that allows the user to go to about activity
    Parameters:
        MenuItem - is passed a MenuItem object, menu button pressed
    Return
        Boolean - true if back is clicked
    */
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.about -> {
                val intentAbout = Intent(this@StudentActivity, AboutActivity::class.java)
                startActivity(intentAbout)
                return@OnNavigationItemSelectedListener true
            }//end of about
        }//end of when
        false
    }//end of mOnNavigationItemSelectedListener
}//end of TeacherActivity
