/* AdminActivity Class

Description:
    This activity allows the admin to choos if the would like to input a classroom, course, or user into the database.

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

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        //takes the admin to the classroom input database page
        val classroomButton = findViewById<Button>(R.id.classroom)
        classroomButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@AdminActivity, ClassroomDBActivity::class.java)
            startActivity(intent)
        })

        //takes the admin to the course input database page
        val courseButton = findViewById<Button>(R.id.courseSelection)
        courseButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@AdminActivity, CourseSelectionDBActivity::class.java)
            startActivity(intent)
        })

        //takes the admin to the up load user input database page
        val userButton = findViewById<Button>(R.id.userActivity)
        userButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@AdminActivity, UpLoadUsersActivity::class.java)
            startActivity(intent)
        })

        //creates the attendance list for a course
        val attendanceButton = findViewById<Button>(R.id.createAttendanceList)
        attendanceButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@AdminActivity, CreateAttendanceListActivity::class.java)
            startActivity(intent)
        })

        //sets up navigation view and selectListener
        val navigation = findViewById<View>(R.id.AdminActNavView) as BottomNavigationView
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
                val intentAbout = Intent(this@AdminActivity, AboutActivity::class.java)
                startActivity(intentAbout)
                return@OnNavigationItemSelectedListener true
            }//end of about
        }//end of when
        false
    }//end of mOnNavigationItemSelectedListener
}//end of AdminActivity
