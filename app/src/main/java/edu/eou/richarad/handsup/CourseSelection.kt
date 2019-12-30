/* CourseSelectionActivity

Description:
        This activity works with CourseSelectionDBActivity to store the variables that will be pushed up into the
        Firebase database.

Created by: Allia Richardson
Course: Software Engineering (CS 380)
Term: Spring 2019
------------------------------------------------------------------------------------------------------------------------
Altered: May 30
By: Ryan Bailey
Details: Alter variables that had float/int to strings
------------------------------------------------------------------------------------------------------------------------
*/

package edu.eou.richarad.handsup

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class CourseSelection (
        val profFirst: String, val profLast: String, val startTime: String, val endTime: String,
        val startDate: String, val endDate: String, val roomNum: String, val buildingName: String, val courseID: String)