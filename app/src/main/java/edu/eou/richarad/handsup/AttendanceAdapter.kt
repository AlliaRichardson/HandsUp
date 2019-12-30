/*  Attendance Adapter Class

Description:
    This is an adapter for the teacher attendance page. It will show a list of students in the course, and it will allow
    the professor the option to check if a student is absent or present

Created by: Allia Richardson
Course: Software Engineering (CS 380)
Term: Spring 2019
*/
package edu.eou.richarad.handsup

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import com.google.firebase.database.FirebaseDatabase

class AttendanceAdapter(context: Context, mTaskList: MutableList<Attendance>) : BaseAdapter() {
    //initializes inflater view
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    //initializes attendaceList
    private var attendanceList = mTaskList

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        //gets variables from AttendanceList
        val inAttendance = attendanceList[position].inAttendance
        val studentName: String = attendanceList[position].name
        val userId: String = attendanceList[position].userId
        val date: String = attendanceList[position].date
        val courseId: String = attendanceList[position].courseId
        val status: String = attendanceList[position].userStatus
        val view: View
        val listRowHolder: ListRowHolder

        //if convert view is null don't set layout
        if (convertView == null) {
            view = inflater.inflate(R.layout.task_rows, parent, false)
            listRowHolder = ListRowHolder(view)
            view.tag = listRowHolder
        }

        //if view is true set layout
        else {
            view = convertView
            listRowHolder = view.tag as ListRowHolder
        }

        //if the user is a student state course id
        if(status == "Student") {
            listRowHolder.userNames.text = courseId
        }//end of if statement

        //if they are a teacher state the students name
        else if(status == "Teacher") {
            listRowHolder.userNames.text = studentName
        }//end of else if

        //set check box state
        listRowHolder.present.isChecked = inAttendance!!

        //listener for the checkbox
        listRowHolder.present.setOnClickListener(View.OnClickListener { view ->
            //if the box is check mark present in the database
            if ((view as CompoundButton).isChecked) {
                val databaseRef =
                    FirebaseDatabase.getInstance().getReference("attendance/$courseId/$date/$userId/inAttendance")
                databaseRef.setValue(true)
            }//end of if statement
            //if the box is unchecked mark as absent in the database
            else {
                val databaseRef =
                    FirebaseDatabase.getInstance().getReference("attendance/$courseId/$date/$userId/inAttendance")
                databaseRef.setValue(false)
            }//end of else
        })//end of setOnClickListener
        //return the view
        return view
    }//end of getView

    override fun getItem(index: Int): Any {
        //return the attendanceList index number
        return attendanceList[index]
    }//end of getItem

    override fun getItemId(index: Int): Long {
        //convert the index to long
        return index.toLong()
    }//end of getItemId

    override fun getCount(): Int {
        //get attendance size
        return attendanceList.size
    }//end of getCount

    private class ListRowHolder(row: View?) {
        //set the layout ids variables
        val userNames: TextView = row!!.findViewById(R.id.txtTaskDesc) as TextView
        val present: CheckBox = row!!.findViewById(R.id.chkDone) as CheckBox
    }//end of ListRowHolder
}//end of AttendanceAdapter
