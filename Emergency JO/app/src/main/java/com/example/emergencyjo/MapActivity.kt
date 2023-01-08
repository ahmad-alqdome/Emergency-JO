package com.example.emergencyjo

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_map.*
import java.text.SimpleDateFormat

class MapActivity : BaseActivity() ,OnMapReadyCallback , GoogleMap.OnMarkerClickListener{

    private lateinit var mMap: GoogleMap
    private lateinit var lastLocation : Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private  var  database = Firebase.database
    private  var  mRefRequest = database.getReference("Requests")
    private  var  mRefCar = database.getReference("Car")



    companion object{
        private const val LOCATION_REQUEST_CODE = 1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true

        mMap.setOnMarkerClickListener(this)
        setUpMap()
    }
    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE)
            return
        }
        mMap.isMyLocationEnabled = true
        fusedLocationProviderClient.lastLocation.addOnSuccessListener(this) { location ->
            if(location != null) {
                lastLocation = location
                    val currentLatLong = LatLng(location.latitude, location.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 12f))
               RequestLocation.setOnClickListener {


                   val bundle: Bundle? =intent.extras
                   val description:String= bundle?.get("description") as String
                   val typeCar:String=bundle.getString("type_car","")
                   val alertBuilder=AlertDialog.Builder(this)
                   alertBuilder.setMessage(R.string.message_sure_request)
                   alertBuilder.setPositiveButton(R.string.yes,null)
                   alertBuilder.setNegativeButton(R.string.no,null)
                   val alert=alertBuilder.create()
                   alert.show()
                   alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener()
                   {
                       placeMarkerOnMap(currentLatLong)
                       val key = getInfo(UserProperties.USER_PERSONAL_ID)


                       mRefRequest.child(key).addListenerForSingleValueEvent(object:ValueEventListener{
                           override fun onDataChange(snapshot: DataSnapshot) {

                               if(snapshot.exists())
                               {
                                   showAlertFound()
                               }
                               else
                               {
                                   val request = RequestData(location.latitude, location.longitude,getInfo(UserProperties.USER_PERSONAL_ID),
                                       getInfo(UserProperties.USER_NAME),getInfo(UserProperties.USER_PHONE)
                                       ,getInfo(UserProperties.USER_GOVERNORATE),description,getCurrentDate(),typeCar)
                                   mRefRequest.child(key).setValue(request)
                                   Toast.makeText(applicationContext,"شكرا لك , سوف يتم التعامل مع الحاله بأسرع وقت",Toast.LENGTH_LONG).show()
                                   finish()

                               }
                           }
                           override fun onCancelled(error: DatabaseError) {
                           }

                       })






                }
                   alert.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener()
                   {
                       alert.dismiss()
                   }
               }
            }
        }
    }

    private fun showAlertFound() {


        val alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setMessage(R.string.have_request)
        alertBuilder.setPositiveButton(R.string.ok, null)
        val alertDialog = alertBuilder.create()
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener()
        {
            finish()
        }
    }

    private fun placeMarkerOnMap(currentLatLong: LatLng) {

        val markerOptions = MarkerOptions().position(currentLatLong)
        markerOptions.title("$currentLatLong")
        mMap.addMarker(markerOptions)
    }

    private fun getInfo(data:String): String
    {
        val sharedPreferences=getSharedPreferences(UserProperties.FILE_NAME_SHARED_INFORMATION, Context.MODE_PRIVATE)
        return sharedPreferences.getString(data,"").toString()
    }


    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val format = SimpleDateFormat("EEEE hh:mm a")

        return format.format(calendar.time)

    }

    override fun onMarkerClick(p0: Marker) = false

}