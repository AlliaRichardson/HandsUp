/*  Users Class

Description:
    This stores the variables the Admin needs to push up into the Firebase database.

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
class Users(val FirstName: String, val LastName: String, val Email: String, val Status: String,
            val Identification: String)