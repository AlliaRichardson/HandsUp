/*  TeacherActivity Class

Description:
    This activity allows the professor to register either themselves or a student for a course.

Created by: Allia Richardson
Course: Software Engineering (CS 380)
Term: Spring 2019
*/

package edu.eou.richarad.handsup

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class TeacherRegisterStudentActivity : AppCompatActivity() {
    lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_register_student)

        //initializes database
        databaseRef = FirebaseDatabase.getInstance().reference

        //gets checked box id
        val studentCheckBox = findViewById<CheckBox>(R.id.chkDone)

        //initializes button id and sets onClickListen
        val button = findViewById <Button>(R.id.registerButton)
        button.setOnClickListener(View.OnClickListener {
            val studentId = (findViewById<EditText>(R.id.studentIdInput)).text.toString()
            val email = (findViewById<EditText>(R.id.stdntEmailInput)).text.toString()
            val studentEmail = email.substringBefore("@")
            val courseId = (findViewById<EditText>(R.id.courseIdInput)).text.toString()

            //if the student box is checked
            if(studentCheckBox.isChecked) {
                //sends registers the student for a course in the database
                databaseRef.child("CourseSection").child(courseId).child(
                    "student_list"
                ).child(studentId).setValue(studentEmail)
            }//end of if

            //Adds the course number to the teachers course list in the database
            databaseRef.child("User").child(studentEmail).child("Courses").child(
                courseId).setValue(true)
        })

        //sets up navigation view and selectListener
        val navigation = findViewById<View>(R.id.TeacherRegNav) as BottomNavigationView
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
}//end of TeacherRegisterActivity
