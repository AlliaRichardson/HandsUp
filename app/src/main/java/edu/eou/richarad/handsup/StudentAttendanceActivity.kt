/* StudentAttendanceActivity Class

Description:
    Shows a list of the student's classes for the day. The student can then check if they attended or not.

Created by: Allia Richardson
Course: Software Engineering (CS 380)
Term: Spring 2019
*/

package edu.eou.richarad.handsup

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.BottomNavigationView
import android.util.Log
import android.view.View
import android.widget.Button
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_attendance_db.*

class StudentAttendanceActivity : AppCompatActivity() {
    private lateinit var attendanceList: MutableList<Attendance>
    private lateinit var curCourseList: MutableList<CurrentCourses>
    private lateinit var courseList: MutableList<CurrentCourses>
    var increment = 0
    var size = 0
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_attendance)

        //get extras from last activity
        val curUser = intent?.extras?.get("CURRENT_USER") as CurrentUser
        courseList = intent?.extras?.get("COURSE_LIST") as MutableList<CurrentCourses>

        //set mutableList variables
        attendanceList = mutableListOf()
        curCourseList = mutableListOf()

        //set up courses and check boxes
        getDailyCourseList(curUser, courseList)

        //sets up navigation view and selectListener
        val navigation = findViewById<View>(R.id.studentAttendanceNavView) as BottomNavigationView
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
                finish()
                return@OnNavigationItemSelectedListener true
            }//end of back
        }//end of when
        false
    }//end of OnNavigationItemSelectedListener

    /* getDailyCourseList

        Description:
            The method iterates through the current Student's course and checks which course are on that particular
            day of the week.
        Parameters:
            CurrentUser - curUser: current user information
            MutableList<CurrentCourses> -  curCourses: List of CurrentCourses
        Return:
            Not Applicable
    */
    @RequiresApi(Build.VERSION_CODES.N)
    private fun getDailyCourseList(curUser: CurrentUser, curCourses: MutableList<CurrentCourses>) {
        Log.i("MyActivity", "In getDailyCourseList")
        //Clears the currentCourseList
        curCourseList.clear()

        //Goes through each course to check if the day of the week is the same as course day of the week.
        for (current in curCourses) {
            //gets an instance of calendar object
            val calendar = Calendar.getInstance()
            //gets the day of the week
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

            //if the day of the week is Monday and the course is scheduled for Monday; add to curCourseList
            if (dayOfWeek == 2 && current.monday) {
                Log.i("Tag", "I'm in Monsday" + current.monday.toString() + dayOfWeek)
                curCourseList.add(current)
            }//end of if

            //if the day of the week is Tuesday and the course is scheduled for Tuesday; add to curCourseList
            else if (dayOfWeek  == 3 && current.tuesday) {
                Log.i("Tag", "I'm in Tuesday" + current.tuesday.toString() + dayOfWeek)
                curCourseList.add(current)
            }

            //if the day of the week is Wednesday and the course is scheduled for Wednesday; add to curCourseList
            else if (dayOfWeek  == 4 && current.wednesday) {
                Log.i("Tag", "I'm in Wednesday" + current.wednesday.toString() + dayOfWeek)
                curCourseList.add(current)
            }

            //if the day of the week is Thursday and the course is scheduled for Thursday; add to curCourseList
            else if (dayOfWeek  == 5 && current.thursday) {
                Log.i("Tag", "I'm in Thursday" + current.thursday.toString() + dayOfWeek)
                curCourseList.add(current)
            }

            //if the day of the week is Friday and the course is scheduled for Friday; add to curCourseList
            else if (dayOfWeek  == 6 && current.friday) {
                Log.i("Tag", "I'm in Friday" + current.friday.toString() + dayOfWeek)
                curCourseList.add(current)
            }
        }//end of for loop

        //Checks if the course exist since there are no checks in place yet
        checkIfCourseExists(curUser, curCourseList)
    }

    /* checkIfCourseExists

        Description:
            Check to see if the student is already marked present or absent for a course if not set attendance to false.
        Parameters:
            CurrentUser - curUser: current user information
            MutableList<CurrentCourses> -  curCourses: List of CurrentCourses
        Return:
            Not Applicable
    */
    @RequiresApi(Build.VERSION_CODES.N)
    private fun checkIfCourseExists(user: CurrentUser, curCourses: MutableList<CurrentCourses>) {
        Log.i("MyActivity", "In checkIfCourseExists")
        //clears attendanceList
        attendanceList.clear()

        //gets current date
        val date = SimpleDateFormat("MM-dd-yyyy").format(System.currentTimeMillis())

        //gets the number of classes that student has that day
        size = curCourses.size

        //goes through the current courses for that day and checks if they are valid courses
        for (current in curCourses) {
            //sets the database path
            val path = "attendance/${current.courseNumber}/$date"

                        //creates a database quire with the use fo the path
            val databaseRef = FirebaseDatabase.getInstance().getReference(path)
            databaseRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    //if the course exists
                    if (dataSnapshot.exists()) {
                        getPresent(user, current, date)
                    } //end of if

                    //otherwise get course
                    else {
                        getCourses(user, current, date, false)
                    }//end of else
                }//end of onDataChange
                override fun onCancelled(dataSnapshot: DatabaseError) {
                }//end of onCancelled
            })//end of addValueEventListener
        }//end of for
    }//end of checkIfCourseExists


    /* getPresent

        Description:
            If the student already has a attendance record it checks the state of their attendance.
        Parameters:
            CurrentUser - curUser: current user information
            MutableList<CurrentCourses> -  curCourses: List of CurrentCourses
            date - String: The string for the date portion of the database query
        Return:
            Not Applicable
    */
    private fun getPresent(curUser: CurrentUser, course: CurrentCourses, date: String) {
        Log.i("MyActivity", "In getPresent")
        Log.i("MyActivity",  course.courseNumber)
        val path = "attendance/${course.courseNumber}/$date/${curUser.identification}"
        Log.i("path2", path)
        val databaseRef = FirebaseDatabase.getInstance().getReference(path)
        databaseRef.addValueEventListener(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val present = dataSnapshot.child("inAttendance").value.toString().toBoolean()
                    getCourses(curUser, course, date, present)
                } else {
                    getCourses(curUser, course, date,false)
                }
            }//end of DataChange
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }//end of onCancelled
        })//end of addValueEventListener
    }//getPresent

    /* getPresent

        Description:
            If the student already has a attendance record it checks the state of their attendance.
        Parameters:
            CurrentUser - curUser: current user information
            MutableList<CurrentCourses> -  curCourses: List of CurrentCourses
            String - date: The string for the date portion of the database query
            Boolean - present: true if they are present in class; false if they are absent.
        Return:
            Not Applicable
    */
    @RequiresApi(Build.VERSION_CODES.N)
    private fun getCourses(curUser: CurrentUser, course: CurrentCourses, date: String, present: Boolean) {
        //initializes an attendance object
        val attendees = Attendance()

        //sets the variables of attendees to curUser information, attendance, and course information
        attendees.date = date
        attendees.userStatus = curUser.status
        attendees.courseId = course.courseNumber
        attendees.emailId = curUser.email
        attendees.userId = curUser.identification
        attendees.name = curUser.firstName + " " + curUser.lastName
        attendees.inAttendance = present

        //goes through once to set all courses in the mutable list.
        if(increment < size) {
            attendanceList.add(attendees)       //adds attendees to attendanceList
            increment += 1                      //increments
        }//end of if statement

        //sets attendee to the correct location within the mutableList
        else{
            //looks for the postion of the course in the MutableList
            for((index, list) in curCourseList.withIndex()){
                if (list.courseNumber == course.courseNumber){
                    //set element at position index
                    attendanceList[index] = attendees

                }//end of if statement
            }//end of for statement
        }//end of else

        //goes to the attendance adapter
        val myAdapter = AttendanceAdapter(applicationContext, attendanceList)
        listviewTask!!.adapter = myAdapter
    }
}