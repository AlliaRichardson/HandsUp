/*  Alarm Names Class

Description:

Created by: Zachary Johnson and Ryan Bailey
Course: Software Engineering (CS 380)
Term: Spring 2019
*/

package edu.eou.richarad.handsup

import android.app.AlarmManager
import android.content.Context

internal class AlarmNames private constructor() {

    var alarmArray = arrayOfNulls<AlarmManager>(5)
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var altitude: Double = 0.0
    var courseNumber: String = ""
    var identification: String = ""
    var samples:Int = 0
    companion object {
        private var appContext: Context? = null
        private val ourInstance = AlarmNames()

        fun getInstance(context: Context): AlarmNames {
            appContext = context
            return ourInstance
        }
    }
}