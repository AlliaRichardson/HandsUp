/*  UpLoadUsersActivity Class

Description:
    This allows the Admin to manually create a user and pushes it to the database with the use of a User class object

Created by: Allia Richardson
Course: Software Engineering (CS 380)
Term: Spring 2019
------------------------------------------------------------------------------------------------------------------------
Altered: May 30
By: Ryan Bailey
Details: Altered latitude, longitude, altitude, radius, and buildingNum from float/int to strings
------------------------------------------------------------------------------------------------------------------------
*/

package edu.eou.richarad.handsup

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UpLoadUsersActivity : AppCompatActivity() {
    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_up_load_users)

        //initialize databaseRef
        database = FirebaseDatabase.getInstance().reference

        val button = findViewById<Button>(R.id.commitClassroomDB)
        button.setOnClickListener(View.OnClickListener {

            //set the Edit Text into put to variables
            val userId = findViewById<EditText>(R.id.userIdInput).text.toString()
            val firstName = findViewById<EditText>(R.id.firstNameInput).text.toString()
            val lastName = findViewById<EditText>(R.id.lastNameInput).text.toString()
            val email = findViewById<EditText>(R.id.emailInput).text.toString()
            val status = findViewById<EditText>(R.id.authorizationInput).text.toString()
            val newUser = Users(firstName, lastName, email, status, userId)
            Toast.makeText(this@UpLoadUsersActivity, "You clicked me.", Toast.LENGTH_SHORT).show()
            database.child("User").child(email.substringBefore("@")).setValue(newUser)
        })
        //sets up navigation view and selectListener
        val navigation = findViewById<View>(R.id.userDBNav) as BottomNavigationView
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
                val intent = Intent(this@UpLoadUsersActivity, AdminActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }//end of back
        }//end of when
        false
    }//end of OnNavigationItemSelectedListener
}//end of UpLoadUserActivity
