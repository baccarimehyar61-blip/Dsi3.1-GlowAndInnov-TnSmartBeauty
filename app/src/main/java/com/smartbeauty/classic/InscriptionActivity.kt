package com.smartbeauty.classic

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

class InscriptionActivity : AppCompatActivity() {

    // L'URL de l'API (voir ConnexionActivity)
    private val BASE_URL = "http://10.0.2.2:8081/"

    private lateinit var btnInscription: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inscription)

        // Récupération des vues avec findViewById
        val edtNom = findViewById<EditText>(R.id.edtNom)
        val edtPrenom = findViewById<EditText>(R.id.edtPrenom)
        val edtEmail = findViewById<EditText>(R.id.edtEmail)
        val edtTelephone = findViewById<EditText>(R.id.edtTelephone)
        val edtMotPasse = findViewById<EditText>(R.id.edtMotPasse)
        val edtConfirmation = findViewById<EditText>(R.id.edtConfirmation)
        btnInscription = findViewById(R.id.btnInscription)
        val tvSeConnecter = findViewById<TextView>(R.id.tvSeConnecter)

        btnInscription.setOnClickListener {
            val nom = edtNom.text.toString().trim()
            val prenom = edtPrenom.text.toString().trim()
            val email = edtEmail.text.toString().trim()
            val tel = edtTelephone.text.toString().trim()
            val mdp = edtMotPasse.text.toString()
            val confirmation = edtConfirmation.text.toString()

            // Validations (mêmes règles que le backend et le site web)
            if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || mdp.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs obligatoires", Toast.LENGTH_SHORT).show()
            } else if (mdp.length < 8) {
                Toast.makeText(this, "Le mot de passe doit contenir au moins 8 caractères", Toast.LENGTH_SHORT).show()
            } else if (mdp != confirmation) {
                Toast.makeText(this, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show()
            } else {
                // Le téléphone est facultatif : null s'il est vide
                val utilisateur = RegisterRequest(prenom, nom, email, if (tel.isEmpty()) null else tel, mdp)
                inscrire(utilisateur)
            }
        }

        // "Se connecter" : on ferme cette activité pour revenir à la connexion
        tvSeConnecter.setOnClickListener { finish() }
    }

    private fun inscrire(utilisateur: RegisterRequest) {
        btnInscription.isEnabled = false

        // Créer la variable "instance" qui est une instance de l'interface ApiService
        val instance: ApiService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }

        // Appeler la méthode inscription de l'API
        instance.inscription(utilisateur)
            .enqueue(object : Callback<RegisterResponse> {

                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    btnInscription.isEnabled = true
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@InscriptionActivity,
                            "Compte créé, vous pouvez vous connecter", Toast.LENGTH_LONG
                        ).show()
                        finish()   // retour à ConnexionActivity
                    } else {
                        val msg = messageErreur(response, "Inscription impossible (email déjà utilisé ?)")
                        Toast.makeText(this@InscriptionActivity, msg, Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    btnInscription.isEnabled = true
                    Toast.makeText(
                        this@InscriptionActivity,
                        "Impossible de joindre le serveur", Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}
