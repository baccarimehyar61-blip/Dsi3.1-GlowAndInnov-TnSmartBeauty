package com.smartbeauty.classic

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SalonDetailActivity : AppCompatActivity() {

    // L'URL de l'API (voir SalonsActivity)
    private val BASE_URL = "http://10.0.2.2:8081/"

    // Instance de l'interface ApiService
    private val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    // Prestations du salon (liste mutable remplie par l'API)
    private var prestations = mutableListOf<Prestation>()
    private lateinit var adapter: PrestationAdapter
    private lateinit var tvAucunePrestation: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_salon_detail)

        // On reçoit seulement l'id du salon cliqué (comme TP5, étape 6)
        val id = intent.getLongExtra("id", -1L)
        if (id == -1L) {
            Toast.makeText(this, "Salon introuvable", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        tvAucunePrestation = findViewById(R.id.tvAucunePrestation)

        // RecyclerView des prestations
        val rvPrestations = findViewById<RecyclerView>(R.id.rvPrestations)
        adapter = PrestationAdapter(prestations)
        rvPrestations.layoutManager = LinearLayoutManager(this)
        rvPrestations.isNestedScrollingEnabled = false
        rvPrestations.adapter = adapter

        findViewById<Button>(R.id.btnRetour).setOnClickListener { finish() }

        loadSalon(id)
    }

    // Comme sur le site web : on récupère les salons, puis on garde celui qui a cet id
    private fun loadSalon(id: Long) {
        instance.getSalons()
            .enqueue(object : Callback<List<Salon>> {

                override fun onResponse(call: Call<List<Salon>>, response: Response<List<Salon>>) {
                    val salon = response.body()?.find { it.id == id }
                    if (response.isSuccessful && salon != null) {
                        afficher(salon)
                        loadPrestations(id)
                    } else {
                        Toast.makeText(
                            this@SalonDetailActivity,
                            "Salon introuvable", Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<Salon>>, t: Throwable) {
                    Toast.makeText(
                        this@SalonDetailActivity,
                        "Impossible de joindre le serveur", Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    // Charge les prestations du salon depuis l'API
    private fun loadPrestations(salonId: Long) {
        instance.getPrestations(salonId)
            .enqueue(object : Callback<List<Prestation>> {

                override fun onResponse(call: Call<List<Prestation>>, response: Response<List<Prestation>>) {
                    prestations.clear()
                    prestations.addAll(response.body() ?: emptyList())
                    adapter.notifyDataSetChanged()
                    tvAucunePrestation.visibility = if (prestations.isEmpty()) View.VISIBLE else View.GONE
                }

                override fun onFailure(call: Call<List<Prestation>>, t: Throwable) {
                    Toast.makeText(
                        this@SalonDetailActivity,
                        "Impossible de charger les prestations", Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    // Affiche les informations du salon
    private fun afficher(salon: Salon) {
        val imgDetail = findViewById<ImageView>(R.id.imgDetail)
        if (!imageLocale(imgDetail, salon.id)) {
            chargerImage(imgDetail, salon.image, BASE_URL)
        }
        findViewById<TextView>(R.id.tvDetailNom).text = salon.nom ?: ""
        findViewById<TextView>(R.id.tvDetailService).text = (salon.emoji ?: "") + " " + (salon.service ?: "")
        findViewById<TextView>(R.id.tvDetailNote).text = "⭐ " + formatNote(salon.note) + "/5"
        findViewById<TextView>(R.id.tvDetailAdresse).text = "📍 " + (salon.adresse ?: "") + ", " + (salon.ville ?: "")
    }
}
