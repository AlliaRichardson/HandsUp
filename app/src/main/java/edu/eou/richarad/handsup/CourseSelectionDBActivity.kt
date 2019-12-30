/* CourseSelectionDBActivity

Description:
        This activity works with CourseSelectionActivity and DaysOfWeek to store the variables that will be pushed up
        into the Firebase database.

Created by: Allia Richardson
Course: Software Engineering (CS 380)
Term: Spring 2019
------------------------------------------------------------------------------------------------------------------------
Altered: May 30
By: Ryan Bailey
Details: Alter variables that had float and/or int to strings
------------------------------------------------------------------------------------------------------------------------
*/

package edu.eou.richarad.handsup

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.BottomNavigationView
import android.view.View
import android.widget.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*


class CourseSelectionDBActivity : AppCompatActivity() {

    lateinit var databaseRef: DatabaseReference

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_selection_db)

        //initializes databaseRef
        databaseRef = FirebaseDatabase.getInstance().reference

        //variables for the TextViews for Start and end for dates and times
        val startDateTextView: TextView  = findViewById<EditText>(R.id.startDateInput)
        val endDateTextView: TextView  = findViewById<EditText>(R.id.endDateInput)
        val startTimeTextView: TextView  = findViewById<EditText>(R.id.startTimeInput)
        val endTimeTextView: TextView  = findViewById<EditText>(R.id.endTimeInput)

        //gets a calender instance
        val cal = Calendar.getInstance()

        //sets a listener for start date
        val startDateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear,
                                                                        dayOfMonth ->
            cal.set(Calendar.YEAR, year)                        //sets year
            cal.set(Calendar.MONTH, monthOfYear)                //sets month
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)          //sets day of the month

            val myFormat = "MM-dd-yyyy"                         //sets format
            val sdf = SimpleDateFormat(myFormat, Locale.US)     //sets format to local
            startDateTextView.text = sdf.format(cal.time)       //sets the date to text box
        }//end of startDateSetListener

        //sets a listener for end date
        val endDateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear,
                                                                      dayOfMonth ->
            cal.set(Calendar.YEAR, year)                        //sets year
            cal.set(Calendar.MONTH, monthOfYear)                //sets month
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)          //sets day of the month

            val myFormat = "MM-dd-yyyy"                          //sets format
            val sdf = SimpleDateFormat(myFormat, Locale.US)      //sets format to local
            endDateTextView.text = sdf.format(cal.time)          //sets the date to text box
        }//end of endDateSetListener

        //gets an onclick listener for start date
        startDateTextView.setOnClickListener {
            DatePickerDialog(this@CourseSelectionDBActivity, startDateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }//end of setOnClickListener

        //gets an onclick listener for end date
        endDateTextView.setOnClickListener {
            DatePickerDialog(this@CourseSelectionDBActivity, endDateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }//end of setOnClickListener

        //sets a listener for start time
        val startTimeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hourOfDay)                    //sets hours
            cal.set(Calendar.MINUTE, minute)                            //sets minutes
            //val myFormat = "KK:mm aa"                                 // gives the 12 hour format
            val myFormat = "HH:mm"                                      // gives military time
            val sdf = SimpleDateFormat(myFormat, Locale.US)             //sets format to local
            startTimeTextView.text = sdf.format(cal.time)               //sets the time to text box
        }//end of startTimeSetListener

        //sets a listener for end time
        val endTimeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hourOfDay)                    //sets hours
            cal.set(Calendar.MINUTE, minute)                            //sets minutes
            //val myFormat = "KK:mm aa"                                 // gives the 12 hour format
            val myFormat = "HH:mm"                                      // gives military time
            val sdf = SimpleDateFormat(myFormat, Locale.US)             //sets format to local
            endTimeTextView.text = sdf.format(cal.time)               //sets the time to text box
        }//end of endTimeSetListener

        //gets an onclick listener for start time
        startTimeTextView.setOnClickListener {
            TimePickerDialog(this@CourseSelectionDBActivity,
                startTimeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                false
            ).show()
        }//end of setOnClickListener

        //gets an onclick listener for end time
        endTimeTextView.setOnClickListener {
            TimePickerDialog(
                this@CourseSelectionDBActivity,
                endTimeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                false
            ).show()
        }//end of setOnClickListener

        //initializes button variable on onClickListener
        val button = findViewById <Button>(R.id.commitClassroomDB)
        button.setOnClickListener(View.OnClickListener {

            //variables to their respective editText fields
            val profFirst = (findViewById<EditText>(R.id.profFirstNameInput)).text.toString()
            val profLast = (findViewById<EditText>(R.id.profLastNameInput)).text.toString()
            val startTime = (findViewById<EditText>(R.id.startTimeInput)).text.toString()
            val endTime = (findViewById<EditText>(R.id.endTimeInput)).text.toString()
            val startDate = (findViewById<EditText>(R.id.startDateInput)).text.toString()
            val endDate = (findViewById<EditText>(R.id.endDateInput)).text.toString()
            val roomNum = (findViewById<EditText>(R.id.roomInput)).text.toString()
            val buildingName = (findViewById<EditText>(R.id.buildingInput)).text.toString()
            val courseID = (findViewById<EditText>(R.id.courseIdInput)).text.toString()
            val checkMonday = findViewById<CheckBox>(R.id.Monday).isChecked
            val checkTuesday = findViewById<CheckBox>(R.id.Tuesday).isChecked
            val checkWednesday = findViewById<CheckBox>(R.id.Wednesday).isChecked
            val checkThursday = findViewById<CheckBox>(R.id.Thursday).isChecked
            val checkFriday = findViewById<CheckBox>(R.id.Friday).isChecked

            //sets a CourseSelection Object
            val newClassroom = CourseSelection(profFirst, profLast, startTime, endTime, startDate, endDate, roomNum,
                buildingName, courseID)
            //sends data to database @courseId
            databaseRef.child("CourseSection").child(courseID).setValue(newClassroom)

            //sets a DayOfWeek Object
            val dOW = DaysOfWeek(checkMonday, checkTuesday, checkWednesday, checkThursday, checkFriday)
            //sends data to database @courseId
            databaseRef.child("CourseSection").child(courseID).child("DaysOfWeek").setValue(dOW)
            Toast.makeText(this@CourseSelectionDBActivity, "Data Sent.", Toast.LENGTH_SHORT).show()
        })

        //sets up navigation view and selectListener
        val navigation = findViewById<View>(R.id.CourseDBNav) as BottomNavigationView
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
                val intent = Intent(this@CourseSelectionDBActivity, AdminActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }//end of back
        }//end of when
        false
    }//end of OnNavigationItemSelectedListener
}//end of CourseSelectionDB Activity




