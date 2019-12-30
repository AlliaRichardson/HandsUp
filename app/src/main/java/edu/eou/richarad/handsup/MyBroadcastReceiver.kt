/* MyBroadCastReceiver Class

Description:    The receiver is fired by the alarm manager
for the starting time of every course. The receiver automatically
compares the phone location to the classroom and writes to the database
if the student is present (within a certain radius)


Created by: Zachary Johnson Ryan Bailey
Course: Software Engineering (CS 380)
Term: Spring 2019
*/

package edu.eou.richarad.handsup

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.support.annotation.RequiresApi
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

private const val TAG = "MyBroadcastReceiver"

class MyBroadcastReceiver : BroadcastReceiver() {
    var lat: Double = 0.0
    var lon: Double = 0.0
    var alt: Double = 0.0
    var courseNumber: String = ""
    var identification: String = ""
    private lateinit var nameAlarms: AlarmNames
    var locationManager: LocationManager? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onReceive(context: Context, intent: Intent) {
        nameAlarms= AlarmNames.getInstance(context)//getting the singleton

        Log.i("MyActivity", "In Receiver")

        //retrieving the course information
        alt=nameAlarms.altitude
        lat=nameAlarms.latitude
        lon=nameAlarms.longitude
        identification = nameAlarms.identification
        courseNumber = nameAlarms.courseNumber
        Log.i("MyActivity", "$alt")
        Log.i("MyActivity", "$lat")
        Log.i("MyActivity", "$lon")

        locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager?

        try {
            // Request location updates
            //gps and network requests, to update more frequently
            locationManager?.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0,
                0.0f,
                locationListener
            )
            locationManager?.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0,
                0.0f,
                locationListener
            )
            locationManager?.requestLocationUpdates(
                LocationManager.GPS_PROVIDER ,
                0,
                0.0f,
                locationListener
            )
            Log.i("myactivity","requested location")
        } catch (ex: SecurityException) {
            //location is turned off
            Log.d("myactivity", "Location is turned off, no location available")
            Log.d("myactivity", ex.toString())
        }
    }

    //define the listener
    @RequiresApi(Build.VERSION_CODES.N)
    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Log.i("MyActivity", "In location listener")
            //day of the attendance in question
            val date = SimpleDateFormat("MM-dd-yyyy").format(System.currentTimeMillis())

            //get the distance between the phone and the center of the classroom
            val distance = FloatArray(1) { 0.0f }
            Location.distanceBetween(location.latitude, location.longitude, lat, lon, distance)

            //get the attendance for the course in question
            val databaseRef =
                FirebaseDatabase.getInstance().getReference("attendance/$courseNumber/$date/$identification/inAttendance")

            if (distance[0] < 7 && Math.abs(location.altitude - alt) < 2) {
                databaseRef.setValue(true)//student is marked present
            } else {
                databaseRef.setValue(false)//student is still absent
            }

            nameAlarms.samples ++
            removeUpdates()//this method will remove the listener after so many samples
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun removeUpdates(){
        if(nameAlarms.samples>20) {
            locationManager?.removeUpdates(locationListener)
            Log.i("myactivity", "times up, removing")
        }

    }
}