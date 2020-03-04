package com.example.pockemonandroid

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.Exception

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        checkPermission()
        loadPockemon()
    }
    var ACCESSLOCATIONCODE=123
    fun checkPermission()
    {
        if(Build.VERSION.SDK_INT>=23){
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),ACCESSLOCATIONCODE)
                return
            }
        }
        GetUserLocation()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            ACCESSLOCATIONCODE->{
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    GetUserLocation()
                }
                else{
                    Toast.makeText(this,"We cannot access your location",Toast.LENGTH_LONG).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
@SuppressLint("MissingPermission")
fun GetUserLocation()
    {
        Toast.makeText(this,"User location access on",Toast.LENGTH_LONG).show()

        var myLocation=myLocationListener()
        var locationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3,3f,myLocation)
        var myThreadO=myThread()
        myThreadO.start()
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

        // Add a marker in Sydney and move the camera

    }
    var LocationSuper:Location?=null

    inner class  myLocationListener:LocationListener{

        constructor(){
            LocationSuper=Location("Start")
            LocationSuper!!.longitude=0.0
            LocationSuper!!.latitude=0.0
        }
        override fun onLocationChanged(location: Location?) {
            ////To change body of created functions use File | Settings | File Templates.
            LocationSuper=location
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderEnabled(provider: String?) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderDisabled(provider: String?) {
//            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }
var oldLocation:Location?=null
    inner  class myThread:Thread{
        constructor():super(){
            oldLocation= Location("start")
            oldLocation!!.longitude=0.0
            oldLocation!!.latitude=0.0
        }
        override fun run(){
            while(true){
                try{
                    if(oldLocation!!.distanceTo(LocationSuper)==0f){
                        continue
                    }
                    oldLocation=LocationSuper
                    runOnUiThread(){
                        mMap!!.clear()
                        //show me
                        val currPos = LatLng(LocationSuper!!.latitude, LocationSuper!!.longitude)
                        mMap.addMarker(MarkerOptions().position(currPos).title("Me").snippet("Here is my location").icon(BitmapDescriptorFactory.fromResource(R.drawable.mario)))
                        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currPos,10f))
                        //show the pokemons
                        for( i in 0..listOfPockemons.size-1){
                            var currPockemon=listOfPockemons[i]
                            if(!currPockemon.isCaught){
                                val currPockemonPos = LatLng(currPockemon.locationOfPock!!.latitude!!, currPockemon.locationOfPock!!.longitude!!)
                                mMap.addMarker(MarkerOptions().position(currPockemonPos).title(currPockemon.name).snippet(currPockemon.des +" , power is ${currPockemon.power}").icon(BitmapDescriptorFactory.fromResource(currPockemon.image!!)))
                                //mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(currPockemonPos,7f))
                                if(LocationSuper!!.distanceTo(currPockemon!!.locationOfPock)<2){
                                    currPockemon.isCaught=true
                                    listOfPockemons[i]=currPockemon
                                    playerPower+= currPockemon!!.power!!
                                    Toast.makeText(applicationContext,"You caught:${currPockemon.name}.\nNew power:$playerPower",Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                    Thread.sleep(1000)
                }catch (ex:Exception){
                    println(ex)
                }
            }
        }
    }
    var playerPower:Double=0.0
    var listOfPockemons=ArrayList<Pockemon>()
    fun loadPockemon(){
        listOfPockemons.add(Pockemon("Here from japan",R.drawable.charmander,72.8,23.50,"Charmander",55.0))
        listOfPockemons.add(Pockemon("Here from India",R.drawable.bulbasaur,65.7,-122.3,"Bulbasaur",67.0))
        listOfPockemons.add(Pockemon("Here from America",R.drawable.squirtle,10.7,12.3,"Squirtle",37.0))

    /**/}
}
