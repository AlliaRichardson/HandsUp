/* IntroActivity Class

Description:
        SPLASH SCREEEEEEN!!!! Goes from the introduction screen activity to the login activity

Created by: Allia Richardson
Course: Software Engineering (CS 380)
Term: Spring 2019
Image Icon is owned by Allia Richardson
*/

package edu.eou.richarad.handsup

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class IntroActivity : AppCompatActivity() {
    private val WAIT_TIME = 10000L   //change 10000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        //Waits 2 seconds before switching activities
        Handler().postDelayed(Runnable {
            //goes from IntroActivity to LoginActivity
            val intent = Intent(this@IntroActivity, LoginActivity::class.java)
            //starts the intent
            startActivity(intent)
            //closes the IntroActivity
            finish()
        }, WAIT_TIME) //end of handler
    }//end of onCreate
}//end of IntroActivity