/* CourseSelectionActivity

Description:
        Stores the variables for the current user's courses and the classroom information for those courses.

Created by: Allia Richardson
Course: Software Engineering (CS 380)
Term: Spring 2019
*/
package edu.eou.richarad.handsup

import java.io.Serializable

class CurrentCourses(
    var courseNumber: String,
    var building: String,
    var roomNum: String,
    var classroomId: String,
    var monday: Boolean,
    var tuesday: Boolean,
    var wednesday: Boolean,
    var thursday: Boolean,
    var friday: Boolean,
    var startTime: String,
    var endTime: String,
    var startDate: String,
    var endDate: String,
    var latitude: String,
    var longitude: String,
    var altitude: String,
    var radius: String): Serializable{
    constructor() : this("", "", "", "", false, false,
        false, false, false, "", "","","","",
        "", "", "")

    override fun toString(): String {
        return "$courseNumber $building $roomNum $classroomId $monday $tuesday $wednesday $thursday $friday $startTime"+
        "$endTime $startDate $endDate $latitude $longitude $altitude $radius"
    }

    
}
