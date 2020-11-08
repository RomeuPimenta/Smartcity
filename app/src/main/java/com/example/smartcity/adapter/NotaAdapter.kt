package com.example.smartcity.adapter

import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.smartcity.R
import com.example.smartcity.entities.Nota


public class NotaAdapter(notaList: MutableList<Nota>, listener: NotasAdapterListener, context : Context) : RecyclerView.Adapter<NotaAdapter.MyViewHolder>(), Filterable {
    private val notaList: MutableList<Nota>
    private val listener: NotasAdapterListener
    private var notaListFiltered: MutableList<Nota>
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
                listener.onNotaSelected(notaListFiltered[adapterPosition], adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val nota = notaListFiltered[position]
        holder.titulo.text = nota.titulo
        holder.subtitulo.text = nota.subtitulo
        holder.data.text = nota.data
    }



    fun removeItem(position: Int) {
        notaListFiltered.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int {
        return notaListFiltered.size
    }

    override fun getItemId(position: Int): Long {
        return notaListFiltered[position].id?.toLong()!!
    }

    //para nao utilizar o event list de forma a ir buscar os dados, usamos o proprio adaptador (FF)
    fun getItem(position: Int): Nota {
        return notaListFiltered[position]
    }

    interface NotasAdapterListener {
        fun onNotaSelected(nota: Nota?, position: Int)
    }

    init {
        setHasStableIds(true)
        this.listener = listener
        this.notaList = notaList!!
        this.notaListFiltered = notaList!!
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                notaListFiltered = if (charString.isEmpty()) {
                    notaList
                } else {
                    val filteredList: MutableList<Nota> =
                        ArrayList()
                    for (row in notaList) {
                        if (row.titulo.toLowerCase().contains(charSequence) || row.subtitulo.toLowerCase().contains(charSequence) || row.data.toLowerCase().contains(charSequence)) {
                            filteredList.add(row)
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = notaListFiltered
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence,
                filterResults: FilterResults
            ) {
                notaListFiltered = filterResults.values as MutableList<Nota>
                notifyDataSetChanged()
            }
        }
    }
}
