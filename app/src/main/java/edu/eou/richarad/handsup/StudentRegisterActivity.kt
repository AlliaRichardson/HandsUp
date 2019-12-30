package edu.eou.richarad.handsup

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.lang.Exception

class StudentRegisterActivity : AppCompatActivity() {
    lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_register)

        //initializes databaseRef
        databaseRef = FirebaseDatabase.getInstance().reference

        //retrieves curUser from LoginActivity
        val curUser = intent?.extras?.get("CURRENT_USER") as CurrentUser

        //initializes button and sets up onClickListener
        val button = findViewById<Button>(R.id.registerButton)
        button.setOnClickListener(View.OnClickListener {
            //gets username from email address
            val email = curUser.email.substringBefore("@")
            //gets course Id from edit text
            val courseId = (findViewById<EditText>(R.id.courseIdInput)).text.toString()

            //register student in the course section
            databaseRef.child("CourseSection").child(courseId).child("student_list").child(
                curUser.identification
            ).setValue(email)

            //register course in student's user database
            databaseRef.child("User").child(email).child("courses").child(
                courseId
            ).setValue(true)
        })//end of setOnClickListener

        //sets up navigation view and selectListener
        val navigation = findViewById<View>(R.id.studentRegNav) as BottomNavigationView
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
}//end of StudentRegisterActivity
