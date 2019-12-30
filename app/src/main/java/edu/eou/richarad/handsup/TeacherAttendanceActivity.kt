/*TeacherAttendanceActivity Class

Description:
    This class allows for the professor/teacher to input the course and date they would like to check attendance for.

Created by: Allia Richardson
Course: Software Engineering (CS 380)
Term: Spring 2019
*/
package edu.eou.richarad.handsup

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.BottomNavigationView
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.util.*

class TeacherAttendanceActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_attendace)

        //curUser's Information
        val curUser = intent?.extras?.get("CURRENT_USER") as CurrentUser

        //sets variable for DateInput edit text box
        val date: TextView = findViewById<EditText>(R.id.DateInput)

        //sets the current date in the DatInput text box
        date.text = SimpleDateFormat("MM-dd-yyyy").format(System.currentTimeMillis())

        //gets a calender instance
        val cal = Calendar.getInstance()

        //sets a listener for start date
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)                        //sets the year
            cal.set(Calendar.MONTH, monthOfYear)                //sets the month
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)          //sets day of the month

            val myFormat = "MM-dd-yyyy"                         //formats the date
            val sdf = SimpleDateFormat(myFormat, Locale.US)     //create SimpleDateFormat string
            date.text = sdf.format(cal.time)                    //sets the date in the edit text box
        }//end of OnDateSetListener

        //gets an onclick listener for start date
        date.setOnClickListener {
            DatePickerDialog(
                this@TeacherAttendanceActivity, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }//end of setOnClickListener

        //takes the professor the the attendance page where they can view students and their attendance state
        val attendanceButton = findViewById<Button>(R.id.attendanceButton)
        attendanceButton.setOnClickListener(View.OnClickListener {
            //gets the date to see attendance
            val dayOfWeek = date.text.toString()
            //gets courseNumber for attendance
            val courseNum = findViewById<EditText>(R.id.courseIdInput).text.toString()
            //creates intent
            val intent = Intent(
                this@TeacherAttendanceActivity, AttendanceDBActivity::class.java
            )
            //sends activity variables to attendanceDBActivity
            intent.putExtra("CURRENT_USER", curUser)
            intent.putExtra("COURSE_NUM", courseNum)
            intent.putExtra("DATE", dayOfWeek)

            //starts activity
            startActivity(intent)
        })//end of setOnClickListener

        //sets up navigation view and selectListener
        val navigation = findViewById<View>(R.id.TeacherAttCourseNav) as BottomNavigationView
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }//end of setOnClickListener

    /*  onNavigationItemSelected
        Description:
            A menu that allows the user to go back to the previous screen.
        Parameters:
            MenuItem - is passed a MenuItem object, menu button pressed
        Return
            Boolean - true if back is clicked
     */
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.back -> {
                //closes this activity
                finish()
                return@OnNavigationItemSelectedListener true
            }//end of back
        }//end of when
        false
    }//end of OnNavigationItemSelectedListener
}//end of TeacherAttendanceActivity
