package com.smartbeauty.classic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Adapter du RecyclerView : il remplit chaque carte avec les infos d'un salon (comme StudentAdapter du TP5)
class SalonAdapter(
    private val salons: List<Salon>,
    private val baseUrl: String
) : RecyclerView.Adapter<SalonAdapter.SalonViewHolder>() {

    // Fonction appelée quand l'utilisateur clique sur un salon (le onItemClick du TP5)
    var onItemClick: ((Salon) -> Unit)? = null

    // Le ViewHolder garde les références des vues d'une carte (item_salon.xml)
    class SalonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgSalon: ImageView = itemView.findViewById(R.id.imgSalon)
        val tvNote: TextView = itemView.findViewById(R.id.tvNote)
        val tvBadge: TextView = itemView.findViewById(R.id.tvBadge)
        val tvVille: TextView = itemView.findViewById(R.id.tvVille)
        val tvNom: TextView = itemView.findViewById(R.id.tvNom)
        val tvAdresse: TextView = itemView.findViewById(R.id.tvAdresse)
        val tvCategorie: TextView = itemView.findViewById(R.id.tvCategorie)
    }

    // Crée une carte à partir du layout item_salon.xml
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalonViewHolder {
        val vue = LayoutInflater.from(parent.context).inflate(R.layout.item_salon, parent, false)
        return SalonViewHolder(vue)
    }

    override fun getItemCount(): Int = salons.size

    // Remplit la carte numéro "position" avec les données du salon
    override fun onBindViewHolder(holder: SalonViewHolder, position: Int) {
        val salon = salons[position]

        holder.tvNom.text = salon.nom ?: ""
        holder.tvVille.text = "📍 " + (salon.ville ?: "")
        holder.tvAdresse.text = salon.adresse ?: ""
        holder.tvBadge.text = salon.service ?: ""
        holder.tvCategorie.text = (salon.emoji ?: "") + " " + (salon.service ?: "")
        holder.tvNote.text = "★ " + formatNote(salon.note)

        // Image : d'abord celle de res/drawable (salon_<id>), sinon l'URL du salon, sinon fond rose
        if (!imageLocale(holder.imgSalon, salon.id)) {
            chargerImage(holder.imgSalon, salon.image, baseUrl)
        }

        // Clic sur la carte
        holder.itemView.setOnClickListener { onItemClick?.invoke(salon) }
    }
}
