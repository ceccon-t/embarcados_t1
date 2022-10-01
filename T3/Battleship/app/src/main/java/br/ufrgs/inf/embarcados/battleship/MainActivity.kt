package br.ufrgs.inf.embarcados.battleship

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.ufrgs.inf.embarcados.battleship.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.buttonJoin.setOnClickListener {
            val sessionKey = binding.editTextSessionKey.text.toString()
            //Toast.makeText(this, "Joining session with key $sessionKey", Toast.LENGTH_LONG).show()
            val intent = Intent(this, SetupFleetActivity::class.java)
            intent.putExtra("Key", sessionKey)
            startActivity(intent)
        }
    }

}