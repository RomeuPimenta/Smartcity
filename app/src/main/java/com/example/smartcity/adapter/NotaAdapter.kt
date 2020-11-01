package com.example.smartcity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartcity.R
import com.example.smartcity.entities.Nota

class NotaAdapter internal constructor (
    context: Context
) : RecyclerView.Adapter<NotaAdapter.BlocoViewHolder>(){

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var notas = emptyList<Nota>()

    class BlocoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tituloItemView: TextView = itemView.findViewById(R.id.titulo)
        val subtituloItemView: TextView = itemView.findViewById(R.id.subtitulo)
        val dataItemView: TextView = itemView.findViewById(R.id.data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlocoViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return BlocoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BlocoViewHolder, position: Int) {
        val current = notas[position]
        holder.tituloItemView.text = current.titulo.toString()
        holder.subtituloItemView.text = current.subtitulo.toString()
        holder.dataItemView.text = current.data.toString()
    }

    internal fun setNotas(notas: List<Nota>) {
        this.notas = notas
        notifyDataSetChanged()
    }

    override fun getItemCount() = notas.size
}
