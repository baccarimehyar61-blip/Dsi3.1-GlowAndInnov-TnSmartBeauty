package com.smartbeauty.classic

import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// Interface qui déclare les endpoints de l'API (backend Spring Boot)
interface ApiService {

    @POST("api/auth/inscription")
    fun inscription(@Body utilisateur: RegisterRequest): Call<RegisterResponse>

    @POST("api/auth/connexion")
    fun connexion(@Body identifiants: LoginRequest): Call<LoginResponse>

    // Liste de tous les salons (consultation publique)
    @GET("api/salons")
    fun getSalons(): Call<List<Salon>>

    // Prestations d'un salon (consultation publique)
    @GET("api/prestations/salon/{salonId}")
    fun getPrestations(@Path("salonId") salonId: Long): Call<List<Prestation>>
}

// Lit le message d'erreur envoyé par le backend ({"message": "..."}).
// Si le serveur n'envoie pas de message, on affiche le message par défaut.
fun messageErreur(response: Response<*>, parDefaut: String): String {
    val brut = response.errorBody()?.string().orEmpty()
    val msg = try {
        Gson().fromJson(brut, Map::class.java)?.get("message") as? String
    } catch (e: Exception) {
        null
    }
    return if (msg.isNullOrBlank()) parDefaut else msg
}
