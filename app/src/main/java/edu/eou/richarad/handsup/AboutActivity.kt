/* AboutActivity Class

Description:
    This activity explains the purpose of the app, and stats who the creators of the application

Created by: Malcolm Zoon
Course: Software Engineering (CS 380)
Term: Spring 2019
------------------------------------------------------------------------------------------------------------------------
Altered: June 6, 2019
By: Allia Richardson
Details: Edited the text information, moved text information to string.xml, and created the layout.
------------------------------------------------------------------------------------------------------------------------
*/

package edu.eou.richarad.handsup

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.View

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        //sets up navigation view and selectListener
        val navigation = findViewById<View>(R.id.aboutNavView) as BottomNavigationView
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    /*  onNavigationItemSelected

        Description:
            A menu that allows the user to go to about activity
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
    }//end of mOnNavigationItemSelectedListener
}//end of AboutActivity