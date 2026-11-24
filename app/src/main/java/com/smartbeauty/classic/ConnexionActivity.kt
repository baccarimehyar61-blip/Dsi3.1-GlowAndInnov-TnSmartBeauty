package com.smartbeauty.classic

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ConnexionActivity : AppCompatActivity() {

    // L'URL de l'API : 10.0.2.2 = le PC (où tourne Spring Boot) vu depuis l'émulateur
    // Vrai téléphone : mettre l'adresse IP du PC (ex: http://192.168.1.15:8081/)
    private val BASE_URL = "http://10.0.2.2:8081/"

    private lateinit var btnConnexion: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connexion)

        // Récupération des vues avec findViewById
        val edtEmail = findViewById<EditText>(R.id.edtEmail)
        val edtMotPasse = findViewById<EditText>(R.id.edtMotPasse)
        btnConnexion = findViewById(R.id.btnConnexion)
        val tvCreerCompte = findViewById<TextView>(R.id.tvCreerCompte)

        // Clic sur "Se connecter"
        btnConnexion.setOnClickListener {
            val email = edtEmail.text.toString().trim()
            val mdp = edtMotPasse.text.toString()

            if (email.isEmpty() || mdp.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            } else {
                connecter(email, mdp)
            }
        }

        // Intent explicite : aller vers InscriptionActivity
        tvCreerCompte.setOnClickListener {
            startActivity(Intent(this, InscriptionActivity::class.java))
        }
    }

    private fun connecter(email: String, mdp: String) {
        btnConnexion.isEnabled = false   // évite le double clic pendant l'appel

        // Créer la variable "instance" qui est une instance de l'interface ApiService
        val instance: ApiService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }

        // Appeler la méthode connexion de l'API
        instance.connexion(LoginRequest(email, mdp))
            .enqueue(object : Callback<LoginResponse> {

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    btnConnexion.isEnabled = true
                    val user = response.body()

                    if (response.isSuccessful && user != null) {
                        // Succès : on envoie les infos à AccueilActivity avec un Bundle
                        val bundle = Bundle().apply {
                            putString("prenom", user.prenom)
                            putString("nom", user.nom)
                            putString("email", user.email)
                            putString("role", user.role)
                        }
                        val i = Intent(this@ConnexionActivity, AccueilActivity::class.java)
                        i.putExtras(bundle)
                        startActivity(i)
                        finish()
                    } else {
                        val msg = messageErreur(response, "Email ou mot de passe incorrect")
                        Toast.makeText(this@ConnexionActivity, msg, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    btnConnexion.isEnabled = true
                    Toast.makeText(
                        this@ConnexionActivity,
                        "Impossible de joindre le serveur", Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}
