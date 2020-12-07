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
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory
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
    var tipo_inc: Int = 0
    var temp1: String? ? =null;

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
            Log.e("HOJE1", "entrei")
            call.enqueue(object: Callback<List<Locations>> {
                override fun onResponse(call: Call<List<Locations>>, response: Response<List<Locations>>){
                    //Log.e("tag321", response?.body()!!.toString())

                    if(response.isSuccessful){
                        mMap!!.clear()
                        val sharedPreferences: SharedPreferences =
                            getSharedPreferences("REMEMBER", Context.MODE_PRIVATE);

                        val utilizador_id = sharedPreferences.getString("utilizador_id", null)

                        for(entry in response.body()!!){
                            val loc = LatLng(entry.lat, entry.lng)
                            val completo = resources.getString(R.string.descr, entry.texto, entry.tipo, entry.username, entry.data, entry.lat.toString(), entry.lng.toString())

                            Log.e("descr1", "entrei")
                            val idUser = entry.utilizador_id.toString();
                            Log.e("HOJE", "entrei")
                            if(idUser != utilizador_id){
                                Log.e("HOJE", "entrei")
                                val marker: Marker = mMap!!.addMarker(MarkerOptions().position(loc).title(entry.morada).snippet(completo).icon(
                                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)))
                                marker.tag = entry.id
                            }else{
                                Log.e("HOJE", "entrei")
                                val marker: Marker = mMap!!.addMarker(MarkerOptions().position(loc).title(entry.morada).snippet(completo).icon(
                                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
                                 marker.tag = entry.id
                            }


                        }
                    }
                }

                    override fun onFailure(call: Call<List<Locations>>, t: Throwable) {
                                Log.e("tag3211", t.toString())
                    }
                })
            }

    private fun setMarkersFiltro10(value: String){
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.markers()

        call.enqueue(object: Callback<List<Locations>> {
            override fun onResponse(call: Call<List<Locations>>, response: Response<List<Locations>>){
                if(response.isSuccessful){
                    mMap!!.clear()
                    val sharedPreferences: SharedPreferences =
                        getSharedPreferences("REMEMBER", Context.MODE_PRIVATE);
                    val utilizador_id = sharedPreferences.getString("utilizador_id", null)

                    var filtro = value.toInt()*1000
                    var count: Int = 0

                    for(entry in response.body()!!){
                            val loc = LatLng(entry.lat, entry.lng)
                        val completo = resources.getString(R.string.descr, entry.texto, entry.tipo, entry.username, entry.data, entry.lat.toString(), entry.lng.toString())
                        //val descr = resources.getString(R.string.descr, entry.texto, entry.tipo, entry.username, entry.data, entry.lat.toString(), entry.lng.toString())
                        Log.e("completo", completo.toString())
                        val idUser = entry.utilizador_id.toString();

                            val temp = calculateDistance(lastLocation.latitude, lastLocation.longitude, entry.lat, entry.lng);
                            if(temp < filtro){
                                if(idUser != utilizador_id){
                                    val marker: Marker = mMap!!.addMarker(MarkerOptions().position(loc).title(entry.morada).snippet(completo).icon(
                                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)))
                                    marker.tag = entry.id

                                }else{
                                    val marker: Marker = mMap!!.addMarker(MarkerOptions().position(loc).title(entry.morada).snippet(completo).icon(
                                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
                                    marker.tag = entry.id
                                }
                            count++
                        }
                    }
                    Toast.makeText(applicationContext, resources.getString(R.string.encontrados, count.toString(), (filtro/1000).toString()), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Locations>>, t: Throwable) {

            }
        })
    }

    private fun setMarkersFiltro20(tipo_inc: String){
        Log.e("which20", tipo_inc.toString())
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.markers()

        call.enqueue(object: Callback<List<Locations>> {
            override fun onResponse(call: Call<List<Locations>>, response: Response<List<Locations>>){
                Log.e("tag321", response?.body()!!.toString())

                if(response.isSuccessful){
                    mMap!!.clear()
                    val sharedPreferences: SharedPreferences =
                        getSharedPreferences("REMEMBER", Context.MODE_PRIVATE);

                    val utilizador_id = sharedPreferences.getString("utilizador_id", null)

                    for(entry in response.body()!!){
                        val loc = LatLng(entry.lat, entry.lng)
                        val completo = resources.getString(R.string.descr, entry.texto, entry.tipo, entry.username, entry.data, entry.lat.toString(), entry.lng.toString())

                        Log.e("descr1", "entrei")
                        val idUser = entry.utilizador_id.toString();
                        val idTipo = entry.tipo_id.toString();

                        if(idTipo == tipo_inc.toString()){
                            if(idUser != utilizador_id){
                                val marker: Marker = mMap!!.addMarker(MarkerOptions().position(loc).title(entry.morada).snippet(completo).icon(
                                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)))
                                marker.tag = entry.id
                            }else{
                                val marker: Marker = mMap!!.addMarker(MarkerOptions().position(loc).title(entry.morada).snippet(completo).icon(
                                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
                                marker.tag = entry.id
                            }
                        }

                    }
                }
            }

            override fun onFailure(call: Call<List<Locations>>, t: Throwable) {
                Log.e("tag3211", t.toString())
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap!!.setInfoWindowAdapter(applicationContext?.let { CustomInfoWindowForGoogleMap(this) })
        setUpMap();
        mMap!!.setOnInfoWindowClickListener(object : GoogleMap.OnInfoWindowClickListener {

           override fun onInfoWindowClick(marker: Marker?) {
            Log.e("tag33333333", "teste")
            Log.e("tag33", marker?.tag.toString())

               val request = ServiceBuilder.buildService(EndPoints::class.java)
               val call = request.markersbyiduser(marker?.tag.toString())

               call.enqueue(object: Callback<String> {
                   override fun onResponse(call: Call<String>, response: Response<String>){
                       Log.e("tag3311abc", response.body().toString())
                       val sharedPreferences: SharedPreferences =
                           getSharedPreferences("REMEMBER", Context.MODE_PRIVATE);
                       val utilizador_id = sharedPreferences.getString("utilizador_id", null)

                       if(response.isSuccessful){
                           if(response?.body()!!.toString() == utilizador_id){
                               val intent = Intent(this@MapsActivity, EditarMarker::class.java)
                               intent.putExtra("id_marker", marker?.tag.toString())
                               startActivity(intent)
                           }else{
                               Toast.makeText(applicationContext, getString(R.string.naoPodeEditar), Toast.LENGTH_SHORT).show()
                           }
                       }
                   }

                   override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.e("tag3333", t.toString())
                   }
               })
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
            }

            R.id.botao_filtro ->{
                val alert = AlertDialog.Builder(this)

                alert.setTitle(R.string.tituloFiltro)
                alert.setMessage(R.string.mensagemFiltro)


                        val customLayout: View = layoutInflater
                        .inflate(
                            R.layout.dialog_window,
                            null
                        )
                        alert.setView(customLayout)

                alert.setPositiveButton(
                    R.string.filtrar
                ) { dialog, whichButton ->
                    val editText: EditText = customLayout
                        .findViewById(
                            R.id.editText
                        )
                    setMarkersFiltro10(editText.text.toString())
                }

                alert.setNegativeButton(
                    R.string.cancelar
                ) { dialog, whichButton ->
                    // Canceled.
                }

                alert.show()

                true
            }

            R.id.botao_filtro2->{    ////ALTERAR AQUII ----------------------------------------------------!!!!!------
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Choose an animal")

// add a radio button list
                val tipo_incidente = arrayOf("Obras", "Danos na Via", "Outros")
                val checkedItem = 0
                builder.setSingleChoiceItems(tipo_incidente, checkedItem) { dialog, which ->
                    if(which == 0){
                        Log.e("which", which.toString())
                        tipo_inc = which+1
                    }else if(which == 1){
                        Log.e("which2", which.toString())
                        tipo_inc = which+1
                    }else if (which == 2){
                        Log.e("which3", which.toString())
                        tipo_inc = which+1
                    }

                }
// add OK and Cancel buttons

                builder.setPositiveButton("OK") { dialog, which ->
                    temp1 = tipo_inc.toString()
                    setMarkersFiltro20(temp1.toString());

                }
                builder.setNegativeButton("Cancel", null)

// create and show the alert dialog
                val dialog = builder.create()
                dialog.show()

                true
            }

            R.id.botao_normal ->{
                onResume()
                true
            }
            else -> super.onOptionsItemSelected(item)

        }
    }
    override fun onWindowSelected(marker: Marker?) {
        Log.e("Teste", marker.toString())

    }

    fun calculateDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lng1, lat2, lng2, results)
        // distance in meter
        return results[0]
    }

}
