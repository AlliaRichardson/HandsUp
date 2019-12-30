/*  Classroom Class

Description:
    This activity will show the list of student in attendance for any given day by working with the AttendanceAdapter
    class.

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
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Classroom (val buildingId: String, val buildingName: String, val buildingNumber: String,
                 val latitude: String, val longitude: String, val altitude: String, val radius: String)