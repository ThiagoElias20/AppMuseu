package com.example.agoravai

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ObraVisitanteAdapter(
    private val onItemClick: (Obra) -> Unit
) : ListAdapter<Obra, ObraVisitanteAdapter.ObraViewHolder>(ObraDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObraViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_obra_visitante, parent, false)
        return ObraViewHolder(view)
    }

    override fun onBindViewHolder(holder: ObraViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ObraViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgObra: ImageView = itemView.findViewById(R.id.imgObra)
        private val tvNome: TextView = itemView.findViewById(R.id.tvNome)
        private val tvAno: TextView = itemView.findViewById(R.id.tvAno)
        private val tvAutor: TextView = itemView.findViewById(R.id.tvAutor)

        fun bind(obra: Obra) {
            tvNome.text = obra.nome
            tvAno.text = obra.ano
            tvAutor.text = obra.autor
            Glide.with(itemView.context).load(obra.imagem).into(imgObra)

            itemView.setOnClickListener {
                onItemClick(obra)
            }
        }
    }

    class ObraDiffCallback : DiffUtil.ItemCallback<Obra>() {
        override fun areItemsTheSame(oldItem: Obra, newItem: Obra): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Obra, newItem: Obra): Boolean {
            return oldItem == newItem
        }
    }
}
