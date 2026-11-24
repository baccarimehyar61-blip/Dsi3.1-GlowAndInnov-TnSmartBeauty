package com.smartbeauty.classic

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AccueilActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accueil)

        // Récupération des données envoyées par ConnexionActivity (Bundle)
        val prenom = intent.getStringExtra("prenom") ?: ""
        val nom = intent.getStringExtra("nom") ?: ""
        val email = intent.getStringExtra("email") ?: ""
        val role = intent.getStringExtra("role") ?: ""

        findViewById<TextView>(R.id.tvBonjour).text = "Bienvenue $prenom"
        findViewById<TextView>(R.id.tvNom).text = "Nom : $prenom $nom"
        findViewById<TextView>(R.id.tvEmail).text = "Email : $email"
        findViewById<TextView>(R.id.tvRole).text = "Rôle : $role"

        // Intent explicite : aller vers la liste des salons
        findViewById<Button>(R.id.btnSalons).setOnClickListener {
            startActivity(Intent(this, SalonsActivity::class.java))
        }

        findViewById<Button>(R.id.btnDeconnexion).setOnClickListener {
            startActivity(Intent(this, ConnexionActivity::class.java))
            finish()
        }
    }
}
