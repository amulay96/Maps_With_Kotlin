package com.example.mapswithkotlin

import android.content.ContentProviderClient
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_maps.*
import java.util.jar.Manifest

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private var latitude:Double=0.toDouble()
    private var longitude:Double=0.toDouble()

    private lateinit var mLastLocation:Location
    private var mMarker:Marker?=null

    lateinit var fusedLocationProviderClient: ContentProviderClient

    companion object{
        private val MY_PERMISSION_CODE:Int = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkLocationPermission();
            
        }

        latitude = mLastLocation.latitude
        longitude = mLastLocation.longitude

        val latLng = LatLng(latitude,longitude)
        val markerOptions = MarkerOptions().position(latLng).title("My Loc").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        mMarker = mMap!!.addMarker(markerOptions)

        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap!!.animateCamera(CameraUpdateFactory.zoomBy(11f))

    }

    private fun checkLocationPermission():Boolean {
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this, arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ),MY_PERMISSION_CODE)
            }
            else
            {
                ActivityCompat.requestPermissions(this, arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ),MY_PERMISSION_CODE)
            }
            return false
        }
        else {
            return true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode)
        {
            MY_PERMISSION_CODE->{
                if(grantResults.size>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
                {
                    if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        if(checkLocationPermission())
                        {
                            mMap!!.isMyLocationEnabled=true
                        }
                    }
                }
                else
                {
                    Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
            {
                mMap!!.isMyLocationEnabled=true
            }
        }
        else
        {
            mMap!!.isMyLocationEnabled=true
        }
        mMap.uiSettings.isZoomControlsEnabled=true

    }
}
