package com.example.agoravai

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MapaVisitanteAdapter(
    private var mapas: List<Mapa>
) : RecyclerView.Adapter<MapaVisitanteAdapter.MapaVisitanteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapaVisitanteViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mapa_visitante, parent, false)
        return MapaVisitanteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MapaVisitanteViewHolder, position: Int) {
        val mapa = mapas[position]
        holder.bind(mapa)
    }

    override fun getItemCount(): Int {
        return mapas.size
    }

    fun updateMapas(newMapas: List<Mapa>) {
        this.mapas = newMapas
        notifyDataSetChanged()
    }

    inner class MapaVisitanteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgMapa: ImageView = itemView.findViewById(R.id.imgMapa)
        private val tvDescricaoMapa: TextView = itemView.findViewById(R.id.tvDescricaoMapa)

        fun bind(mapa: Mapa) {
            tvDescricaoMapa.text = mapa.descricaoDoMapa
            Glide.with(itemView.context).load(mapa.imagemDoMapa).into(imgMapa)
        }
    }
}