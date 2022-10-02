package br.ufrgs.inf.embarcados.battleship

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.ufrgs.inf.embarcados.battleship.databinding.ActivityMainBinding
import br.ufrgs.inf.embarcados.battleship.network.ServerServiceBuilder
import br.ufrgs.inf.embarcados.battleship.network.session.SessionRequestInterface
import br.ufrgs.inf.embarcados.battleship.network.session.SessionRequestModel
import br.ufrgs.inf.embarcados.battleship.network.session.SessionResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

            requestSessionToServer(sessionKey)
        }
    }

    private fun requestSessionToServer(sessionKey: String) {
        val requestModel = SessionRequestModel(sessionKey)
        val response = ServerServiceBuilder.buildService(SessionRequestInterface::class.java)
        response.sendRequest(requestModel).enqueue(
            object: Callback<SessionResponseModel> {
                override fun onResponse(
                    call: Call<SessionResponseModel>,
                    response: Response<SessionResponseModel>
                ) {
                    handleServerResponse(response.body()!!)
                }

                override fun onFailure(call: Call<SessionResponseModel>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Could not connect to server, try again later", Toast.LENGTH_LONG).show()
                }
            }
        )
    }

    private fun handleServerResponse(serverResponse: SessionResponseModel) {
        if (serverResponse.granted) {
            val intent = Intent(this, SetupFleetActivity::class.java)
            intent.putExtra("Key", serverResponse.sessionKey)
            intent.putExtra("YourId", serverResponse.yourId)
            startActivity(intent)
        } else {
            Toast.makeText(this@MainActivity, "This key is not available, please choose another", Toast.LENGTH_LONG).show()
        }
    }

}