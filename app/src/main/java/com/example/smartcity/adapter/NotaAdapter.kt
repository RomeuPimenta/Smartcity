package com.example.smartcity.adapter

import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.smartcity.R
import com.example.smartcity.entities.Nota


public class NotaAdapter(notaList: MutableList<Nota>, listener: NotasAdapterListener, context : Context) : RecyclerView.Adapter<NotaAdapter.MyViewHolder>() {
    private val notaList: MutableList<Nota>
    private val listener: NotasAdapterListener
    var context : Context = context

    inner class MyViewHolder internal constructor(view: View) :
        RecyclerView.ViewHolder(view) {
        var titulo: TextView
        var subtitulo: TextView
        var data: TextView


        init {

            titulo = view.findViewById(R.id.titulo)
            subtitulo = view.findViewById(R.id.subtitulo)
            data = view.findViewById(R.id.data)
            view.setOnClickListener { // send selected contact in callback
                listener.onNotaSelected(notaList[adapterPosition], adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val nota = notaList[position]
        holder.titulo.text = nota.titulo
        holder.subtitulo.text = nota.subtitulo
        holder.data.text = nota.data
    }



    fun removeItem(position: Int) {
        notaList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int {
        return notaList.size
    }

    override fun getItemId(position: Int): Long {
        return notaList[position].id?.toLong()!!
    }

    //para nao utilizar o event list de forma a ir buscar os dados, usamos o proprio adaptador (FF)
    fun getItem(position: Int): Nota {
        return notaList[position]
    }

    interface NotasAdapterListener {
        fun onNotaSelected(nota: Nota?, position: Int)
    }

    init {
        setHasStableIds(true)
        this.listener = listener
        this.notaList = notaList!!
    }
}
