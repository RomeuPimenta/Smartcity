package com.example.smartcity.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.provider.Settings.Global.getString
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.smartcity.EditarMarker
import com.example.smartcity.ListaNotas
import com.example.smartcity.MapsActivity
import com.example.smartcity.R
import com.example.smartcity.api.EndPoints
import com.example.smartcity.api.ServiceBuilder
import com.example.smartcity.entities.Nota
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CustomInfoWindowForGoogleMap(context: Context) : GoogleMap.InfoWindowAdapter {

    var mContext = context
    var mWindow = (context as Activity).layoutInflater.inflate(R.layout.info_window, null)

    private fun rendowWindowText(marker: Marker, view: View) {

        val title = view.findViewById<TextView>(R.id.title)
        val descricao = view.findViewById<TextView>(R.id.descricao)


        title.text = marker.title
        descricao.text = marker.snippet


    }

    override fun getInfoContents(marker: Marker): View {
        rendowWindowText(marker, mWindow)
        return mWindow
    }

    override fun getInfoWindow(marker: Marker): View? {
        rendowWindowText(marker, mWindow)
        return mWindow
    }

    interface CustomWindowAdapterListener {
        fun onWindowSelected(marker: Marker?)
    }


}



