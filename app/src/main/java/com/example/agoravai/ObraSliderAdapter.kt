package com.example.agoravai

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ObraSliderAdapter(private val obras: List<Obra>) : RecyclerView.Adapter<ObraSliderAdapter.ObraViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObraViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_obra_slider, parent, false)
        return ObraViewHolder(view)
    }

    override fun onBindViewHolder(holder: ObraViewHolder, position: Int) {
        holder.bind(obras[position])
    }

    override fun getItemCount(): Int {
        return obras.size
    }

    class ObraViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgObra: ImageView = itemView.findViewById(R.id.imgObraSlider)
        private val tvNome: TextView = itemView.findViewById(R.id.tvNomeSlider)
        private val tvAutor: TextView = itemView.findViewById(R.id.tvAutorSlider)

        fun bind(obra: Obra) {
            tvNome.text = obra.nome
            tvAutor.text = obra.autor
            Glide.with(itemView.context).load(obra.imagem).into(imgObra)
        }
    }
}