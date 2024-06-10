package com.example.agoravai

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MapaAdapter(
    private var mapas: List<Mapa>,
    private val onActionClick: (Mapa, String) -> Unit
) : RecyclerView.Adapter<MapaAdapter.MapaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapaViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_mapa, parent, false)
        return MapaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MapaViewHolder, position: Int) {
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

    inner class MapaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDescricaoMapa: TextView = itemView.findViewById(R.id.tvDescricaoMapa)
        private val imgMapa: ImageView = itemView.findViewById(R.id.imgMapa)
        private val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        private val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)

        fun bind(mapa: Mapa) {
            tvDescricaoMapa.text = mapa.descricaoDoMapa
            Glide.with(itemView.context).load(mapa.imagemDoMapa).into(imgMapa)

            btnEdit.setOnClickListener {
                onActionClick(mapa, "edit")
            }

            btnDelete.setOnClickListener {
                onActionClick(mapa, "delete")
            }
        }
    }
}
