package com.smartbeauty.classic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Adapter des prestations d'un salon (affichées dans SalonDetailActivity)
class PrestationAdapter(
    private val prestations: List<Prestation>
) : RecyclerView.Adapter<PrestationAdapter.PrestationViewHolder>() {

    class PrestationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNomPrestation: TextView = itemView.findViewById(R.id.tvNomPrestation)
        val tvDuree: TextView = itemView.findViewById(R.id.tvDuree)
        val tvDescriptionPrestation: TextView = itemView.findViewById(R.id.tvDescriptionPrestation)
        val tvPrix: TextView = itemView.findViewById(R.id.tvPrix)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrestationViewHolder {
        val vue = LayoutInflater.from(parent.context).inflate(R.layout.item_prestation, parent, false)
        return PrestationViewHolder(vue)
    }

    override fun getItemCount(): Int = prestations.size

    override fun onBindViewHolder(holder: PrestationViewHolder, position: Int) {
        val p = prestations[position]
        holder.tvNomPrestation.text = p.nom ?: ""
        holder.tvDuree.text = "⏱ " + (p.dureeMinutes ?: 0) + " min"
        holder.tvDescriptionPrestation.text = p.description ?: ""
        holder.tvPrix.text = formatNote(p.prix) + " DT"
    }
}
