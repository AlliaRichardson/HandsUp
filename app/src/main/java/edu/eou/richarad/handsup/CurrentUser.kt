/* CourseSelectionActivity

Description:
        Stores the variables for the current user.

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

import java.io.Serializable

class CurrentUser (
    var firstName: String,
    var lastName: String,
    var identification: String,
    var email: String,
    var status: String,
    var course: MutableList<String>): Serializable{
    constructor() : this("", "", "", "", "", MutableList<String>(0){""})

    override fun toString(): String {
        return "$firstName $lastName $identification $email $status $course"
    }
}