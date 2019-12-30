/*  Attendance Class

Description:
    A class that stores the variables for Attendance related class

Created by: Allia Richardson
Course: Software Engineering (CS 380)
Term: Spring 2019
*/

package edu.eou.richarad.handsup

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Attendance ( var inAttendance: Boolean?, var name: String,  var userId: String, var userStatus: String,
                   var emailId: String, var courseId :String, var date: String, var time: String) {

    constructor() :this(false,"","","","","", "","")

    override fun toString(): String {
        return "$inAttendance $name $userId $userStatus $emailId $courseId $date $time"
    }
}