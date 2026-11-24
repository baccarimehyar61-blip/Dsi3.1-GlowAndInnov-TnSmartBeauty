package com.smartbeauty.classic

// Une prestation proposée par un salon : mêmes champs que l'entité Prestation du backend
data class Prestation(
    var id: Long? = null,
    var nom: String? = null,
    var prix: Double? = null,
    var dureeMinutes: Int? = null,
    var description: String? = null
)
