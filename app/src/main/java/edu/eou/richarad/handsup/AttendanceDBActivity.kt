/*  AttendanceDBActivity Class

Description:
    This activity will show the list of student in attendance for any given day by working with the AttendanceAdapter
    class.

Created by: Allia Richardson
Course: Software Engineering (CS 380)
Term: Spring 2019
*/

package edu.eou.richarad.handsup

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ListView
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.DataSnapshot
import kotlinx.android.synthetic.main.activity_attendance_db.*

class AttendanceDBActivity : AppCompatActivity(){
    private lateinit var databaseRef: DatabaseReference
    private lateinit var attendeesList: MutableList<Attendance>
    private lateinit var myListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance_db)

        //gets the intent extras from previous activity
        val curUser = intent?.extras?.get("CURRENT_USER") as CurrentUser
        val courseNum = intent?.extras?.get("COURSE_NUM") as String
        val date = intent?.extras?.get("DATE") as String

        //gets the layout for attendance
        myListView = findViewById(R.id.listviewTask)

        //initializes attendeeList
          attendeesList = mutableListOf()

        //gets a list of all the students in the class
        getAttendanceRecord(curUser.status, courseNum, date)
    }

    private fun getAttendanceRecord(status: String, courseNum:String, date: String){
        //creates a path variable to be used with database
        val path = "attendance/$courseNum/$date"
        //initializes databaseRef to database path location
        databaseRef = FirebaseDatabase.getInstance().getReference(path)
        //gets database information based on path
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                attendeesList.clear()
                if (dataSnapshot.exists()) {
                    for(user in dataSnapshot.children.iterator()) {
                        //creates and Attendance object
                        val attendees = Attendance()

                        //sets variables for attendee object
                        attendees.courseId = courseNum
                        attendees.userStatus = status
                        attendees.date = date
                        attendees.userId = user.key.toString()
                        attendees.inAttendance = user.child("inAttendance").value.toString().toBoolean() /* as Boolean */
                        attendees.name = user.child("name").value.toString()
                        attendees.emailId = user.child("emailId").value.toString()

                        //adds attendee to attendeeList
                        attendeesList.add(attendees)

                        //sends the attendee information to the AttendanceAdapter class
                        val myAdapter = AttendanceAdapter(applicationContext, attendeesList)
                        listviewTask!!.adapter = myAdapter
                    }//end of for statement
                }//end of if statement
            }//end of onDataChange
            override fun onCancelled(p0: DatabaseError) {
            }//end of onCancelled
        })//end of addValueEventListener

        //sets up navigation view and selectListener
        val navigation = findViewById<View>(R.id.attendanceDBNavView) as BottomNavigationView
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

}//end of AttendanceDBActivity