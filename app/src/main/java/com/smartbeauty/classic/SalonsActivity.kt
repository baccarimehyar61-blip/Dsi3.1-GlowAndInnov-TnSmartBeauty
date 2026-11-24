package com.smartbeauty.classic

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
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

class SalonsActivity : AppCompatActivity() {

    // L'URL de l'API (10.0.2.2 = le PC vu depuis l'émulateur)
    private val BASE_URL = "http://10.0.2.2:8081/"

    private val TOUTES_VILLES = "Toutes les villes"

    // Les villes proposées : les mêmes que sur le site web
    private val villes = listOf(
        "Toutes les villes",
        "Béja", "Ben Arous", "Bizerte", "Gabès", "Gafsa", "Jendouba", "Kairouan",
        "Kasserine", "Kébili", "Le Kef", "Mahdia", "La Manouba", "Médenine",
        "Monastir", "Nabeul", "Sfax", "Sidi Bouzid", "Siliana", "Sousse",
        "Tataouine", "Tozeur", "Tunis", "Zaghouan"
    )

    // Liste mutable (son contenu change car il vient de la BD)
    private var salons = mutableListOf<Salon>()     // tous les salons reçus du serveur
    private var affiches = mutableListOf<Salon>()   // salons affichés après recherche / filtre

    private lateinit var adapter: SalonAdapter
    private lateinit var spnVille: Spinner
    private lateinit var edtRecherche: EditText
    private lateinit var tvCompteur: TextView
    private lateinit var tvVide: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_salons)

        // Récupération des vues avec findViewById
        val rvSalons = findViewById<RecyclerView>(R.id.rvSalons)
        spnVille = findViewById(R.id.spnVille)
        edtRecherche = findViewById(R.id.edtRecherche)
        tvCompteur = findViewById(R.id.tvCompteur)
        tvVide = findViewById(R.id.tvVide)

        // RecyclerView + Adapter
        adapter = SalonAdapter(affiches, BASE_URL)
        rvSalons.layoutManager = LinearLayoutManager(this)
        rvSalons.adapter = adapter

        // Clic sur un salon : on envoie seulement l'id à SalonDetailActivity (comme TP5, étape 6)
        adapter.onItemClick = { salon ->
            val i = Intent(this, SalonDetailActivity::class.java)
            i.putExtra("id", salon.id ?: -1L)
            startActivity(i)
        }

        // Spinner des villes
        val adapterVilles = ArrayAdapter(this, android.R.layout.simple_spinner_item, villes)
        adapterVilles.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnVille.adapter = adapterVilles

        // Recherche : on filtre à chaque lettre tapée
        edtRecherche.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filtrer()
            }
        })

        // Filtre par ville
        spnVille.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filtrer()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        loadSalons()
    }

    // Charge les salons depuis l'API
    private fun loadSalons() {
        // Créer la variable "instance" qui est une instance de l'interface ApiService
        val instance: ApiService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }

        // Appeler la méthode getSalons pour remplir le RecyclerView
        instance.getSalons()
            .enqueue(object : Callback<List<Salon>> {

                override fun onResponse(call: Call<List<Salon>>, response: Response<List<Salon>>) {
                    if (response.isSuccessful) {
                        salons.clear()
                        salons.addAll(response.body() ?: emptyList())
                        filtrer()
                    } else {
                        Toast.makeText(
                            this@SalonsActivity,
                            "Erreur serveur (" + response.code() + ")", Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<Salon>>, t: Throwable) {
                    Toast.makeText(
                        this@SalonsActivity,
                        "Impossible de joindre le serveur", Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    // Applique la recherche (nom du salon) et le filtre par ville
    private fun filtrer() {
        val texte = edtRecherche.text.toString().trim().lowercase()
        val ville = spnVille.selectedItem?.toString() ?: TOUTES_VILLES

        affiches.clear()
        affiches.addAll(salons.filter { salon ->
            val okVille = ville == TOUTES_VILLES || salon.ville == ville
            val okTexte = texte.isEmpty() || (salon.nom ?: "").lowercase().contains(texte)
            okVille && okTexte
        })
        adapter.notifyDataSetChanged()

        tvCompteur.text = affiches.size.toString() + " salon(s)"
        tvVide.visibility = if (affiches.isEmpty()) View.VISIBLE else View.GONE
    }
}
