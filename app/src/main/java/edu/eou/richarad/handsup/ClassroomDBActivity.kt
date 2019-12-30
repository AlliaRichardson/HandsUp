/*  ClassroomDBActivity

Description:
    This activity allows the admin to input classroom information and push it up into Firebase, working with the
    Classroom class.

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
import com.google.firebase.database.DatabaseReference


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.widget.Button
import android.widget.EditText
import android.view.View
import com.google.firebase.database.FirebaseDatabase

class ClassroomDBActivity : AppCompatActivity() {
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_classroom_db)

        //initializes database
        databaseRef = FirebaseDatabase.getInstance().reference

        //sets the button to the ui button and then sets onClickListener
        val button = findViewById<Button>(R.id.commitClassroomDB)
        button.setOnClickListener(View.OnClickListener {
            //grabs the information in the EditText boxes and sets them to String variables.
            val buildingNameVal = (findViewById<EditText>(R.id.buildingNameInput)).text.toString()
            val buildingNumVal = (findViewById<EditText>(R.id.buildingNumInput)).text.toString()
            val latitudeVal = (findViewById<EditText>(R.id.latitudeInput)).text.toString()
            val longitudeVal = (findViewById<EditText>(R.id.longitudeInput)).text.toString()
            val altitudeVal = (findViewById<EditText>(R.id.altitudeInput)).text.toString()
            val radiusVal = (findViewById<EditText>(R.id.radiusInput)).text.toString()
            val buildingIDVal = buildingNameVal + buildingNumVal
            val newClassroom = Classroom(
                buildingIDVal, buildingNameVal, buildingNumVal, latitudeVal, longitudeVal,
                altitudeVal, radiusVal
            )

            //commits it to the database
            databaseRef.child("Classroom").child(buildingIDVal).setValue(newClassroom)
        })

        //sets up navigation view and selectListener
        val navigation = findViewById<View>(R.id.ClassroomDBNav) as BottomNavigationView
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
                val intent = Intent(this@ClassroomDBActivity, AdminActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }//end of bacl
        }//end of when
        false
    }//end of OnNavigationItemSelectedListener
}//end of ClassroomDbActivity