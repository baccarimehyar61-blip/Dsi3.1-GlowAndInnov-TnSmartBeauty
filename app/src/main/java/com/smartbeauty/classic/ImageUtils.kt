package com.smartbeauty.classic

import android.widget.ImageView
import com.squareup.picasso.Picasso

// Affiche l'image d'un salon à partir de son URL (Picasso).
// Si le salon n'a pas d'image, on affiche un fond rose (placeholder).
fun chargerImage(img: ImageView, url: String?, baseUrl: String) {
    if (url.isNullOrBlank()) {
        img.setImageResource(R.drawable.bg_salon_placeholder)
        return
    }
    // Dans l'émulateur, "localhost" désigne l'émulateur lui-même : on utilise 10.0.2.2 (le PC)
    var u = url.trim().replace("localhost", "10.0.2.2").replace("127.0.0.1", "10.0.2.2")
    // URL relative (ex: /uploads/photo.jpg) : on ajoute l'adresse du serveur
    if (!u.startsWith("http")) {
        u = baseUrl + u.trimStart('/')
    }
    Picasso.get()
        .load(u)
        .placeholder(R.drawable.bg_salon_placeholder)
        .error(R.drawable.bg_salon_placeholder)
        .into(img)
}

// Note affichée sous la forme "4.5" ou "0" (sans ".0")
fun formatNote(note: Double?): String {
    val n = note ?: 0.0
    return if (n % 1.0 == 0.0) n.toInt().toString() else n.toString()
}

// Comme dans le TP : l'image est dans res/drawable, retrouvée avec getIdentifier.
// Le fichier doit s'appeler salon_<id>.jpg (salon_1.jpg, salon_2.jpg, ...), <id> = id du salon dans la base.
// Retourne true si l'image a été trouvée et affichée.
fun imageLocale(img: ImageView, idSalon: Long?): Boolean {
    val nom = "salon_" + idSalon
    val resId = img.context.resources.getIdentifier(nom, "drawable", img.context.packageName)
    if (resId == 0) return false      // pas d'image locale pour ce salon
    img.setImageResource(resId)
    return true
}
