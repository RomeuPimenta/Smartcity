package com.example.smartcity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.smartcity.adapter.CustomInfoWindowForGoogleMap
import com.example.smartcity.api.EndPoints
import com.example.smartcity.api.Locations
import com.example.smartcity.api.ServiceBuilder
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, CustomInfoWindowForGoogleMap.CustomWindowAdapterListener {

    var fab: FloatingActionButton? =null;
    var locationRequest: LocationRequest?=null;
    private var mMap: GoogleMap?=null;
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    var fusedLocationClient: FusedLocationProviderClient?=null;
    var locationCallback: LocationCallback?=null;
    private lateinit var lastLocation: Location
    var address: String? =null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {

            override fun onLocationResult(p0: LocationResult) {

                super.onLocationResult(p0)
                lastLocation = p0.lastLocation
                var loc = LatLng(lastLocation.latitude, lastLocation.longitude)

                mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15.0f))
                address = getAddress(lastLocation.latitude, lastLocation.longitude)

            }
        }

        setMarkers()
        createLocationRequest()
        setViews()
        setClickListener()
    }

        private fun setMarkers(){
                val request = ServiceBuilder.buildService(EndPoints::class.java)
                val call = request.markers()

            call.enqueue(object: Callback<List<Locations>> {
                override fun onResponse(call: Call<List<Locations>>, response: Response<List<Locations>>){
                    if(response.isSuccessful){
                        mMap!!.clear()
                        for(entry in response.body()!!){
                            val loc = LatLng(entry.lat, entry.lng)
                            val completo = "Descrição: " + entry.texto + "\nId_Utilizador: " + entry.id_utilziador + "\nData inserção: " + entry.data +
                                "\nLat: " + entry.lat + "\nLng: " + entry.lng
                            val marker: Marker = mMap!!.addMarker(MarkerOptions().position(loc).title(entry.morada).snippet(completo))
                            marker.tag = entry.id
                        }
                    }
                }

                    override fun onFailure(call: Call<List<Locations>>, t: Throwable) {

                    }
                })
            }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap!!.setInfoWindowAdapter(applicationContext?.let { CustomInfoWindowForGoogleMap(this) })
        setUpMap();
        mMap!!.setOnInfoWindowClickListener(object : GoogleMap.OnInfoWindowClickListener {
           override fun onInfoWindowClick(marker: Marker?) {

                val intent = Intent(this@MapsActivity, EditarMarker::class.java)
               Log.e("Teste20", marker?.tag.toString())
               intent.putExtra("id_marker", marker?.tag.toString())
                startActivity(intent)
            }
        })
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap!!.isMyLocationEnabled = true;
        }

    }

    fun setUpMap() {

        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        fusedLocationClient?.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest()
// interval specifies the rate at which your app will like to receive updates.
        locationRequest?.interval = 10000
        locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private fun getAddress(lat: Double, lng: Double): String {
        val geocoder = Geocoder(this)
        val list = geocoder.getFromLocation(lat, lng, 1)
        return list[0].getAddressLine(0)
    }

    fun setViews(){
        fab=findViewById<FloatingActionButton>(R.id.fab)
    }

    fun setClickListener() {
        fab?.setOnClickListener {
            val intent = Intent(this@MapsActivity, InserirDados::class.java)
            intent.putExtra("morada", address );
            intent.putExtra("lat", lastLocation.latitude.toString());
            intent.putExtra("lng", lastLocation.longitude.toString())
            startActivity(intent)
        }

    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient?.removeLocationUpdates(locationCallback)

    }

    public override fun onResume() {
        super.onResume()
        setUpMap()
        Log.e("Teste", "Aqui estou eu")
        setMarkers()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_mapa, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.botao_logout -> {
                val intent = Intent(this@MapsActivity, MainActivity::class.java)
                val sharedPreferences: SharedPreferences =
                    getSharedPreferences("REMEMBER", Context.MODE_PRIVATE);
                val remember = sharedPreferences.edit();
                remember.clear()
                remember.apply();
                startActivity(intent)
                finish()
                true
            } else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onWindowSelected(marker: Marker?) {
        Log.e("Teste", marker.toString())
        Toast.makeText(applicationContext, "Teste", Toast.LENGTH_SHORT).show()
    }

    fun onInfoWindowClick(marker: Marker?) {
        Log.e("Teste2", "Teste")
    }

}
