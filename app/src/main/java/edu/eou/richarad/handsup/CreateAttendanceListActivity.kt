package edu.eou.richarad.handsup

import android.app.DatePickerDialog
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
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.database.*
import java.util.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.DataSnapshot

class CreateAttendanceListActivity : AppCompatActivity() {
    private lateinit var databaseRef: DatabaseReference

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_attendance_list)

          //sets variable for DateInput edit text box
        val date: TextView = findViewById<EditText>(R.id.DateInput)

        //sets the current date in the DatInput text box
        date.text = SimpleDateFormat("MM-dd-yyyy").format(System.currentTimeMillis())

        //gets a calender instance
        val calendar = Calendar.getInstance()

        //sets a listener for start date
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)                        //sets the year
            calendar.set(Calendar.MONTH, monthOfYear)                //sets the month
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)          //sets day of the month

            val myFormat = "MM-dd-yyyy"                         //formats the date
            val sdf = SimpleDateFormat(myFormat, Locale.US)     //create SimpleDateFormat string
            date.text = sdf.format(calendar.time)                    //sets the date in the edit text box
        }//end of OnDateSetListener

        //gets an onclick listener for start date
        date.setOnClickListener {
            DatePickerDialog(
                this@CreateAttendanceListActivity, dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }//end of setOnClickListener


        val createList = findViewById<Button>(R.id.attendanceButton)
            createList.setOnClickListener(View.OnClickListener {
            //gets the date to see attendance
            val dateOfMonth = date.text.toString()
            //gets courseNumber for attendance
            val courseNum = findViewById<EditText>(R.id.courseIdInput).text.toString()
            //creates intent
                courseNum.getStudentList(dateOfMonth)
        })

        //sets up navigation view and selectListener
        val navigation = findViewById<View>(R.id.AttListCreatorCourseNav) as BottomNavigationView
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

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


    /* getStudentList
        Description:
            Gets the list of students in the course selected.
        Parameters:
            String - date: date for attendance list
        Return
            Not Applicable
     */
    private fun String.getStudentList(date: String) {
        //sets the path to be used for the database
        val path = "CourseSection/${this}/student_list"
        //gets instance of the database with the use fo the path
        databaseRef = FirebaseDatabase.getInstance().getReference(path)
        //calls the data from the database
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //if there are any students
                if (dataSnapshot.exists()) {
                    //get list of students
                    val studentStr =
                        dataSnapshot.value.toString().split(",").toMutableList()
                    //iterates through the students and grabs the necessary information
                    for (h in studentStr) {
                        //initializes attendance object
                        val attendees = Attendance()
                        attendees.courseId = this@getStudentList
                        attendees.date = date
                        attendees.userId =(h.substringAfter("{").substringBefore("=")).replace(
                            "\\s".toRegex(), "")
                        val emailId = (h.substringAfter("=").substringBefore("}")).replace(
                            "\\s".toRegex(), "")
                        attendees.emailId = emailId
                        //goes to loadAttendanceList
                        loadStudentAttendance(attendees, emailId)
                    }//end of for loop
                }//end of if
            }//end of onDataChange

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Item failed, log a message
                Log.w("MainActivity", "loadItem:onCancelled", databaseError.toException())
            }//end of onCancelled
        })//end of addValueEventListener
    }//end of makeNewList


    /*  loadStudentAttendance
        Description:
            This method gets the student's name, and then it sets up the attendance for that student.
        Parameters:
            Attendance - attendee: attendance object for the student information
            String - emailDomain: The id need for the User database
        Return
            Not Applicable
     */
    private fun loadStudentAttendance(attendees: Attendance, emailDomain: String) {
        val path = "User/$emailDomain"
        databaseRef = FirebaseDatabase.getInstance().getReference(path)
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val firstName = dataSnapshot.child("firstName").value.toString()
                val lastName = dataSnapshot.child("lastName").value.toString()
                attendees.name = "$firstName $lastName"
                val date = attendees.date
                val database = FirebaseDatabase.getInstance().reference
                database.child("attendance").child(attendees.courseId).child(date).child(
                    attendees.userId).child("emailId").setValue(attendees.emailId)
                database.child("attendance").child(attendees.courseId).child(attendees.date).child(
                    attendees.userId).child("inAttendance").setValue(false)
                database.child("attendance").child(attendees.courseId).child(attendees.date).child(
                    attendees.userId).child("name").setValue(attendees.name)
            }//end of onDataChange

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Item failed, log a message
                Log.w("MainActivity", "loadItem:onCancelled", databaseError.toException())
            }//end of onCancelled
        })//end of addValueEventListener
    }//end of loadAttendanceList
}//end of CreateAttendanceListActivity
