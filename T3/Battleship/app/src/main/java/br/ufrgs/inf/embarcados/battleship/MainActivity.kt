package br.ufrgs.inf.embarcados.battleship

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.ufrgs.inf.embarcados.battleship.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var tentativeKey: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()

    }

    private fun setupListeners() {
        binding.buttonJoin.setOnClickListener {
            val sessionKey = binding.editTextSessionKey.text.toString()
            tentativeKey = binding.editTextSessionKey.text.toString()
            //Toast.makeText(this, "Joining session with key $sessionKey", Toast.LENGTH_LONG).show()

//            val intent = Intent(this, SetupFleetActivity::class.java)
//            intent.putExtra("Key", sessionKey)
//            startActivity(intent)

            //
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
            Toast.makeText(this@MainActivity, "Connected to server with session key $tentativeKey.", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this@MainActivity, "This key is not available, please choose another", Toast.LENGTH_LONG).show()
        }
    }

}