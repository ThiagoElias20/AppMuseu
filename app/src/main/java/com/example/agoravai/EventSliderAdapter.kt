package com.example.agoravai

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class EventSliderAdapter(private val events: List<Event>) : RecyclerView.Adapter<EventSliderAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event_slider, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int {
        return events.size
    }

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgEvent: ImageView = itemView.findViewById(R.id.imgEventSlider)
        private val tvNome: TextView = itemView.findViewById(R.id.tvNomeEventSlider)
        private val tvDescricao: TextView = itemView.findViewById(R.id.tvDescricaoEventSlider)

        fun bind(event: Event) {
            tvNome.text = event.nome
            tvDescricao.text = event.descricao
            Glide.with(itemView.context).load(event.imagem).into(imgEvent)
        }
    }
}
