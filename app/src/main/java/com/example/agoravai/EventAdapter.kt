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

class EventAdapter(
    private val onDeleteClick: (String) -> Unit,
    private val onEditClick: (Event) -> Unit,
    private val onItemClick: (Event) -> Unit
) : ListAdapter<Event, EventAdapter.EventViewHolder>(EventDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgEvent: ImageView = itemView.findViewById(R.id.imgEvent)
        private val tvNome: TextView = itemView.findViewById(R.id.tvNome)
        private val tvDescricao: TextView = itemView.findViewById(R.id.tvDescricao)
        private val btnDelete: ImageView = itemView.findViewById(R.id.btnDelete)
        private val btnEdit: ImageView = itemView.findViewById(R.id.btnEdit)

        fun bind(event: Event) {
            tvNome.text = event.nome
            tvDescricao.text = event.descricao
            Glide.with(itemView.context).load(event.imagem).into(imgEvent)

            btnDelete.setOnClickListener {
                onDeleteClick(event.id)
            }

            btnEdit.setOnClickListener {
                onEditClick(event)
            }

            itemView.setOnClickListener {
                onItemClick(event)
            }
        }
    }

    class EventDiffCallback : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }
    }
}


