package com.smartbeauty.classic

// Classes qui représentent le JSON échangé avec le backend Spring Boot.
// Les noms des champs sont les mêmes que ceux du backend (motDePasse, prenom...).

// Corps de la requête de connexion
data class LoginRequest(
    val email: String,
    val motDePasse: String
)

// Réponse du serveur après connexion
data class LoginResponse(
    val message: String?,
    val id: Long?,
    val prenom: String?,
    val nom: String?,
    val email: String?,
    val role: String?
)

// Corps de la requête d'inscription
data class RegisterRequest(
    val prenom: String,
    val nom: String,
    val email: String,
    val telephone: String?,
    val motDePasse: String
)

// Réponse du serveur après inscription
data class RegisterResponse(
    val message: String?,
    val id: Long?,
    val prenom: String?,
    val email: String?
)
