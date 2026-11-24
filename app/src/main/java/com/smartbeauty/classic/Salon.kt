package com.smartbeauty.classic

// Un salon : mêmes champs que l'entité Salon du backend Spring Boot
// (comme la classe Student du TP5). "service" = la catégorie (ex: Coiffure & beauté).
data class Salon(
    var id: Long? = null,
    var nom: String? = null,
    var ville: String? = null,
    var adresse: String? = null,
    var note: Double? = null,
    var service: String? = null,
    var emoji: String? = null,
    var image: String? = null
)
