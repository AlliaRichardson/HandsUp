/* LoginActivity Class

Description:
        Oh, the login activity. This activity receives the user's email address, if the are in the system, it will
        determine if the are Student, Teacher, or Admin. If they are either a Teacher or a Student it will the proceed
        to create two MutableList, alarmList and CourseList, and a user class object which has their information.
        It will then proceed to take them to their respective activities (TeacherActivity, StudentActivity).
        The admin goes strait to the AdminActivity.


        Note: Why are the methods called within the various onDataChange and not in the onCreate?  I had to flow with
        the thread, due to the asynchronous nature of addValueEventListener, or things would get special.

Created by: Allia Richardson
Course: Software Engineering (CS 380)
Term: Spring 2019
------------------------------------------------------------------------------------------------------------------------
Altered: May 30
By: Zachary Johnson
Details: The function 'addAlarms' was added to the class.
------------------------------------------------------------------------------------------------------------------------
*/

package edu.eou.richarad.handsup

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.database.*
import java.io.Serializable
import java.util.*
import android.content.Context.ALARM_SERVICE
import android.os.Build
import android.support.annotation.RequiresApi



class LoginActivity : AppCompatActivity() {
    private lateinit var databaseRef: DatabaseReference
    private lateinit var emailEditText: EditText
    lateinit var errorText: TextView
    lateinit var user: CurrentUser
    private lateinit var courseList: MutableList<CurrentCourses>
    private val nameAlarms: AlarmNames = AlarmNames.getInstance(this)

    //   private val nameAlarms: AlarmNames = AlarmNames.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        errorText = findViewById(R.id.errorText)

        //initializes courseList
        courseList = mutableListOf()
        //sets variable for email input
        emailEditText = this.findViewById(R.id.emailInput)
        //Initializes currentUser
        user = CurrentUser()

        //sets button to a variable
        val loginButton = findViewById<Button>(R.id.loginButton)
        //listens for when the Login button is pressed
        loginButton.setOnClickListener(View.OnClickListener {
            //if the email edit text isn't empty
            if (emailEditText.text.toString() != "") {
                //runs loadUser
                loadUser(emailEditText.text.toString())
            }
            //if empty print error message
            else{
                errorText.text = "Email text box is empty."
            }

        })
    }

    /*  loadUser

        Description:
            Checks to see if the database has user. If the user exists, checks if the user name matches the user email
            address. If it does it checks the user status. If they are a teacher or Admin the switch to TeacherActivity
            or AdminActivity, respectively. If the user is a student it loads the student's courses. If the email
            does not match a user or does not match an email address an error message pops up letting the user know,
            they need to try again.
        Parameters:
            String email input given by user
        Return:
            Not Applicable
    */
    private fun loadUser(email: String) {
        //gets the username part of the email address
        val domain = email.substringBefore("@")

        //The path for the database to follow
        val path = "User/$domain"

        //get an instance of the database
        databaseRef = FirebaseDatabase.getInstance().getReference(path)
        //listen for database change
        databaseRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //if there is information
                if (dataSnapshot.exists()) {
                    //gets user's email
                    user.email = dataSnapshot.child("email").value.toString()
                    //if email entered is the same as database email
                    if (email == user.email) {

                        //gets the information from the data base and sets it to a variable in CurrentUser
                        user.firstName = dataSnapshot.child("firstName").value.toString()
                        user.lastName = dataSnapshot.child("lastName").value.toString()
                        user.identification = dataSnapshot.child("identification").value.toString()
                        user.status = dataSnapshot.child("status").value.toString()
                        val courseStr =
                            dataSnapshot.child("courses").value.toString().split(",").toMutableList()

                        //Goes through the list and grabs only the integers from the string
                        for ((a, h) in courseStr.withIndex()) {
                            courseStr[a] =
                                (h.substringAfter("{").substringBefore("=")).replace("\\s".toRegex(), "")
                        }//end of for
                        //sets the course string to the mutableList in CurrentUser
                        user.course = courseStr

                        //if the current user is an admin or teacher skip to switchActivities function
                        if (user.status == "Admin" || user.status == "Teacher") {
                            switchActivities()
                        }//end of if

                        //otherwise get their course list information
                        else {
                            loadCourseList(user)
                        }//end of else
                    }//end of onDataChange

                    //if the email input does not match database email print error message to screen
                    else {
                        errorText.text = "Email is incorrect. Please enter your email."
                    }//end of else
                }
                //if the email input does not match database email print error message to screen
                else {
                    errorText.text = "User not found. Please enter your email."
                }//end of else
            }//end of addValueEventListener

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Item failed, log a message
                Log.i("MainActivity", "loadItem:onCancelled", databaseError.toException())
            }//end of onCancelled
        })//end of addValueEventListener
    }


    /*  loadCourseList

    Description:
        Checks if the student has any courses, if the do it loads the course information based on the current students
        course list.
    Parameters:
        CurrentUser - set current user information
    Return:
        Not Applicable
    */
    private fun loadCourseList(curUser: CurrentUser) {
        //iterates through the student's course list
        for ((i, courseNum) in curUser.course.withIndex()) {

            //sets database path
            val path = "CourseSection/$courseNum"
            //creates an instance to the database based on the path given
            databaseRef = FirebaseDatabase.getInstance().getReference(path)
            //checks the database
            databaseRef.addValueEventListener(object : ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    //if the location in the database exists
                    if (dataSnapshot.exists()) {
                        //clears the mutable course list
                        courseList.clear()

                        //gets the information from the data base and sets it to a variable in CurrentUser
                        val curCourse = CurrentCourses()

                        //sets all of current course's variables from snapshot to variables in CurrentCourse Class
                        curCourse.courseNumber = courseNum
                        curCourse.monday =
                            dataSnapshot.child("DayOfWeek").child("Mon").value.toString().toBoolean()
                        curCourse.tuesday =
                            dataSnapshot.child("DayOfWeek").child("Tues").value.toString().toBoolean()
                        curCourse.wednesday =
                            dataSnapshot.child("DayOfWeek").child("Wed").value.toString().toBoolean()
                        curCourse.thursday =
                            dataSnapshot.child("DayOfWeek").child("Thur").value.toString().toBoolean()
                        curCourse.friday =
                            dataSnapshot.child("DayOfWeek").child("Fri").value.toString().toBoolean()
                        curCourse.building = dataSnapshot.child("building").value.toString()
                        curCourse.roomNum = dataSnapshot.child("roomNum").value.toString()
                        curCourse.classroomId = curCourse.building + curCourse.roomNum
                        curCourse.startDate = dataSnapshot.child("startDate").value.toString()
                        curCourse.startTime = dataSnapshot.child("startTime").value.toString()
                        curCourse.endDate = dataSnapshot.child("endDate").value.toString()
                        curCourse.endTime = dataSnapshot.child("endTime").value.toString()
                        //gets the number of courses the student is currently enrolled in.
                        val size = curUser.course.size

                        //get the classroom information for the course
                        loadClassroomInfo(curCourse.classroomId, curCourse, size, i)
                    }//end of if

                    //If nothing is in the snap shot state that no data was found
                    else {
                        errorText.text = "No Data Found"
                    }//end of else
                }//end of onDataChange

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Item failed, log a message
                    Log.w("MainActivity", "loadItem:onCancelled", databaseError.toException())
                }
            })
        }
    }


    /*  loadClassroomInfo

        Description:
             Gets the classroom's information and sets within the curCourse class object
        Parameters:
            String - classroomId - The currentC course being looked at
            CurrentCourse - curCourse: the currentCourse object to store room information
            Int - size: The number of courses the student is in
            Int - i: What number the course list is on
        Return:
            Not Applicable
*/
    private fun loadClassroomInfo(classroomId: String, curCourse: CurrentCourses, size: Int, i: Int) {
        //path for the database to follow to get information
        val path = "Classroom/$classroomId"

        //gets an instance of the database
        databaseRef = FirebaseDatabase.getInstance().getReference(path)
        //checks the database
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    //gets the data snapshots need for classroom and sets them in the curCourse CurrentCourse object
                    curCourse.latitude = dataSnapshot.child("latitude").value.toString()
                    curCourse.longitude = dataSnapshot.child("longitude").value.toString()
                    curCourse.altitude = dataSnapshot.child("altitude").value.toString()
                    curCourse.radius = dataSnapshot.child("radius").value.toString()
                    //adds curCourse to course List
                    courseList.add(curCourse)

                    //Sets the alarms for the courses
                    addAlarms(curCourse, i+1)

                    //it is 2 since i starts at 0 and we need it to start at one and to add one more
                    if (i + 2 > size) {
                        //switch activity function call
                        switchActivities()
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Item failed, log a message
                Log.w("MainActivity", "loadItem:onCancelled", databaseError.toException())
            }
        })
    }


    /*  switchActivities

    Description:
         Sends the user to their respective activity location based on their status.
    Parameters:
        Not Applicable
    Return:
        Not Applicable
*/
    private fun switchActivities() {
        //switch, when, statement
        when (user.status) {
            //if the current user is a student send them to studentActivity
            "Student" -> {
                val intent = Intent(
                    this@LoginActivity,
                    StudentActivity::class.java
                )
                intent.putExtra("CURRENT_USER", user)
                intent.putExtra("COURSE_LIST", courseList as Serializable)
                // intent.putExtra("ALARM_LIST", alarmList as Serializable)
                startActivity(intent)
                // finish()
            }
            //if the current user is a teacher send them to studentActivity
            "Teacher" -> {
                val intent = Intent(
                    this@LoginActivity,
                    TeacherActivity::class.java
                )
                intent.putExtra("CURRENT_USER", user as Serializable)
                intent.putExtra("COURSE_LIST", courseList as Serializable)
          //      intent.putExtra("ALARM_LIST", alarmList as Serializable)
                startActivity(intent)
                finish()
            }
            //if the current user is an Admin send them to studentActivity
            "Admin" -> {
                val intent = Intent(
                    this@LoginActivity,
                    AdminActivity::class.java
                )
                startActivity(intent)
                finish()
            }
        }
    }


    private fun addAlarms(curCourse: CurrentCourses, i: Int) {
        var time: Long
        //query the database to receive the courses for the student

        val timeStr = curCourse.startTime.split(":")
        val hours: Int = timeStr[0].toInt()
        val minutes: Int = timeStr[1].toInt()
        Log.i("MyActivity", "$hours $minutes")

        //sends the course information to the singleton
        nameAlarms.alarmArray[i - 1] = getSystemService(Activity.ALARM_SERVICE) as AlarmManager
        nameAlarms.altitude=curCourse.altitude.toDouble()
        nameAlarms.latitude=curCourse.latitude.toDouble()
        nameAlarms.longitude=curCourse.longitude.toDouble()
        nameAlarms.courseNumber=curCourse.courseNumber
        nameAlarms.identification=user.identification

        if (curCourse.monday) {

            Log.i("MyActivity", "Setting alarm for Monday")
            val calendarMonday = Calendar.getInstance()
            calendarMonday.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            calendarMonday.set(Calendar.HOUR_OF_DAY, hours)
            calendarMonday.set(Calendar.MINUTE, minutes)
            calendarMonday.set(Calendar.SECOND, 0)

            //to ensure an alarm doesnt go off for a date in the past
            time = calendarMonday.timeInMillis - calendarMonday.timeInMillis % 60000
            if (System.currentTimeMillis() > time) {
                time += 1000 * 60 * 60 * 24
            }
            //register the receiver to get the location for attendance
            val myIntent = Intent(this@LoginActivity, MyBroadcastReceiver::class.java)
            myIntent.flags=Intent.FLAG_INCLUDE_STOPPED_PACKAGES
            myIntent.flags=Intent.FLAG_ACTIVITY_NEW_TASK
            Log.i("myactivity",curCourse.altitude)
            Log.i("myactivity",curCourse.latitude)
            Log.i("myactivity",curCourse.longitude)



            val pendingIntent =
                PendingIntent.getBroadcast(this@LoginActivity, i, myIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            //set the alarm to fire at classtime
            nameAlarms.alarmArray[i - 1]?.setRepeating(
                AlarmManager.RTC_WAKEUP,
                time,
                AlarmManager.INTERVAL_DAY * 7,
                pendingIntent
            )
        }
    }
}
