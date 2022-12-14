package br.ufrgs.inf.embarcados.battleship

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import br.ufrgs.inf.embarcados.battleship.databinding.ActivitySetupFleetBinding
import br.ufrgs.inf.embarcados.battleship.network.ServerServiceBuilder
import br.ufrgs.inf.embarcados.battleship.network.setup.SetupRequestInterface
import br.ufrgs.inf.embarcados.battleship.network.setup.SetupRequestModel
import br.ufrgs.inf.embarcados.battleship.network.setup.SetupResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SetupFleetActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetupFleetBinding
    private lateinit var fleetSetupBoard: FleetBoard
    private var yourId = ""
    private var sessionKey = ""
    private var patrolSerialized = ""
    private var destroyerSerialized = ""
    private var battleshipSerialized = ""
    private var carrierSerialized = ""
    private var enemyConfirmationAttempts = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetupFleetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fleetSetupBoard = FleetBoard()

        sessionKey = intent.getStringExtra("Key").toString()
        yourId = intent.getStringExtra("YourId").toString()

        binding.textViewSetupKey.text = sessionKey + " - " + yourId

        setupSpinners();

    }

    public fun handleSetButtons(v: View) {
        var message = "Setting "
        var coord = ""
        var size = 0
        var direction = ""
        when(v.id) {
            binding.buttonSetSetupPatrol.id -> {
                coord = binding.choiceRowSetupPatrol.selectedItem as String + binding.choiceColSetupPatrol.selectedItem as String
                size = 2
                direction = binding.choiceRotationSetupPatrol.selectedItem.toString()
                message += "Patrol on " + coord
                if (fleetSetupBoard.fitsAhead(coord, size, direction)) {
                    val path = fleetSetupBoard.cellsInPath(coord, size, direction)
                    var pathStr = ""
                    for (cell in path) {
                        pathStr += "$cell,"
                    }
                    if (fleetSetupBoard.pathIsFree(path)) {
                        fleetSetupBoard.reservePath(path)
                        for (cell in path) {
                            var iv = cellToView(cell)
                            iv.setImageResource(R.drawable.bw_patrol)
                        }
                        patrolSerialized = path.joinToString(",")
                        binding.buttonSetSetupPatrol.isEnabled = false
                    } else {
                        Toast.makeText(this, "Would go over another ship, please check (needs $size free cells).", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Ship does not fit, please check (needs $size free cells).", Toast.LENGTH_SHORT).show()
                }
            }
            binding.buttonSetSetupDestroyer.id -> {
                coord = binding.choiceRowSetupDestroyer.selectedItem as String + binding.choiceColSetupDestroyer.selectedItem as String
                size = 3
                direction = binding.choiceRotationSetupDestroyer.selectedItem.toString()
                if (fleetSetupBoard.fitsAhead(coord, size, direction)) {
                    val path = fleetSetupBoard.cellsInPath(coord, size, direction)
                    if (fleetSetupBoard.pathIsFree(path)) {
                        fleetSetupBoard.reservePath(path)
                        for (cell in path) {
                            var iv = cellToView(cell)
                            iv.setImageResource(R.drawable.bw_destroyer)
                        }
                        destroyerSerialized = path.joinToString(",")
                        binding.buttonSetSetupDestroyer.isEnabled = false
                    } else {
                        Toast.makeText(this, "Would go over another ship, please check (needs $size free cells).", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Ship does not fit, please check (needs $size free cells).", Toast.LENGTH_SHORT).show()
                }
            }
            binding.buttonSetSetupBattleship.id -> {
                coord = binding.choiceRowSetupBattleship.selectedItem as String + binding.choiceColSetupBattleship.selectedItem as String
                size = 4
                direction = binding.choiceRotationSetupBattleship.selectedItem.toString()
                if (fleetSetupBoard.fitsAhead(coord, size, direction)) {
                    val path = fleetSetupBoard.cellsInPath(coord, size, direction)
                    if (fleetSetupBoard.pathIsFree(path)) {
                        fleetSetupBoard.reservePath(path)
                        for (cell in path) {
                            var iv = cellToView(cell)
                            iv.setImageResource(R.drawable.bw_battleship)
                        }
                        battleshipSerialized = path.joinToString(",")
                        binding.buttonSetSetupBattleship.isEnabled = false
                    } else {
                        Toast.makeText(this, "Would go over another ship, please check (needs $size free cells).", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Ship does not fit, please check (needs $size free cells).", Toast.LENGTH_SHORT).show()
                }
            }
            binding.buttonSetSetupCarrier.id -> {
                coord = binding.choiceRowSetupCarrier.selectedItem as String + binding.choiceColSetupCarrier.selectedItem as String
                size = 5
                direction = binding.choiceRotationSetupCarrier.selectedItem.toString()
                if (fleetSetupBoard.fitsAhead(coord, size, direction)) {
                    val path = fleetSetupBoard.cellsInPath(coord, size, direction)
                    if (fleetSetupBoard.pathIsFree(path)) {
                        fleetSetupBoard.reservePath(path)
                        for (cell in path) {
                            var iv = cellToView(cell)
                            iv.setImageResource(R.drawable.bw_carrier)
                        }
                        carrierSerialized = path.joinToString(",")
                        binding.buttonSetSetupCarrier.isEnabled = false
                    } else {
                        Toast.makeText(this, "Would go over another ship, please check (needs $size free cells).", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Ship does not fit, please check (needs $size free cells).", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    public fun handleBattleButton(v: View) {
        if (patrolSerialized.equals("")
            || destroyerSerialized.equals("")
            || battleshipSerialized.equals("")
            || carrierSerialized.equals("")) {
            Toast.makeText(this, "Please set all your ships.", Toast.LENGTH_SHORT).show()
            return
        }

        informSetupToServer();
    }

    private fun informSetupToServer() {
        val requestModel = SetupRequestModel(sessionKey, yourId, patrolSerialized, destroyerSerialized, battleshipSerialized, carrierSerialized)
        val response = ServerServiceBuilder.buildService(SetupRequestInterface::class.java)
        response.sendRequest(requestModel).enqueue(
            object: Callback<SetupResponseModel> {
                override fun onResponse(
                    call: Call<SetupResponseModel>,
                    response: Response<SetupResponseModel>
                ) {
                    binding.buttonSetupBattle.text = "Connecting..."
                    binding.buttonSetupBattle.isEnabled = false
                    handleServerSetupResponse(response.body()!!)
                }

                override fun onFailure(call: Call<SetupResponseModel>, t: Throwable) {
                    Toast.makeText(this@SetupFleetActivity, "Could not connect to server, try again later", Toast.LENGTH_LONG).show()
                    binding.buttonSetupBattle.text = "BATTLE!"
                    binding.buttonSetupBattle.isEnabled = true
                }
            }
        )
    }

    private fun handleServerSetupResponse(serverResponse: SetupResponseModel) {
        if (serverResponse.player1.equals("READY") && serverResponse.player2.equals("READY")) {
            var enemyPatrolSerialized: String = ""
            var enemyDestroyerSerialized: String = ""
            var enemyBattleshipSerialized: String = ""
            var enemyCarrierSerialized: String = ""

            if (yourId.equals("player1")) {
                enemyPatrolSerialized = serverResponse.playerTwoPatrol
                enemyDestroyerSerialized = serverResponse.playerTwoDestroyer
                enemyBattleshipSerialized = serverResponse.playerTwoBattleship
                enemyCarrierSerialized = serverResponse.playerTwoCarrier
            } else {
                enemyPatrolSerialized = serverResponse.playerOnePatrol
                enemyDestroyerSerialized = serverResponse.playerOneDestroyer
                enemyBattleshipSerialized = serverResponse.playerOneBattleship
                enemyCarrierSerialized = serverResponse.playerOneCarrier
            }

            val intent = Intent(this, BattleActivity::class.java)
            intent.putExtra("Key", sessionKey)
            intent.putExtra("YourId", yourId)
            intent.putExtra("Patrol", patrolSerialized)
            intent.putExtra("Destroyer", destroyerSerialized)
            intent.putExtra("Battleship", battleshipSerialized)
            intent.putExtra("Carrier", carrierSerialized)
            intent.putExtra("EnemyPatrol", enemyPatrolSerialized)
            intent.putExtra("EnemyDestroyer", enemyDestroyerSerialized)
            intent.putExtra("EnemyBattleship", enemyBattleshipSerialized)
            intent.putExtra("EnemyCarrier", enemyCarrierSerialized)


            startActivity(intent)
        } else {
            if (enemyConfirmationAttempts < 10) {
                enemyConfirmationAttempts += 1
                Thread.sleep(3_000)
                informSetupToServer()
            } else {
                enemyConfirmationAttempts += 1
                Toast.makeText(this@SetupFleetActivity, "Enemy is taking too long to setup, try again later", Toast.LENGTH_LONG).show()
                enemyConfirmationAttempts = 0
                binding.buttonSetupBattle.text = "BATTLE!"
                binding.buttonSetupBattle.isEnabled = true
            }
        }
    }

    private fun setupSpinners() {
        // Rotations
        val rotations = fleetSetupBoard.allRotations()
        val rotationsAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, rotations)
        binding.choiceRotationSetupPatrol.adapter = rotationsAdapter
        binding.choiceRotationSetupDestroyer.adapter = rotationsAdapter
        binding.choiceRotationSetupBattleship.adapter = rotationsAdapter
        binding.choiceRotationSetupCarrier.adapter = rotationsAdapter

        // Rows
        val rows = fleetSetupBoard.allRows()
        val rowsAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, rows)
        binding.choiceRowSetupPatrol.adapter = rowsAdapter
        binding.choiceRowSetupDestroyer.adapter = rowsAdapter
        binding.choiceRowSetupBattleship.adapter = rowsAdapter
        binding.choiceRowSetupCarrier.adapter = rowsAdapter

        // Columns
        val columns = fleetSetupBoard.allColumns()
        val columnsAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, columns)
        binding.choiceColSetupPatrol.adapter = columnsAdapter
        binding.choiceColSetupDestroyer.adapter = columnsAdapter
        binding.choiceColSetupBattleship.adapter = columnsAdapter
        binding.choiceColSetupCarrier.adapter = columnsAdapter

    }

    private fun cellToView(cell: String): ImageView {
        return when(cell) {
            "A1" -> binding.ivSetupCellA1
            "A2" -> binding.ivSetupCellA2
            "A3" -> binding.ivSetupCellA3
            "A4" -> binding.ivSetupCellA4
            "A5" -> binding.ivSetupCellA5
            "A6" -> binding.ivSetupCellA6
            "A7" -> binding.ivSetupCellA7
            "A8" -> binding.ivSetupCellA8
            "B1" -> binding.ivSetupCellB1
            "B2" -> binding.ivSetupCellB2
            "B3" -> binding.ivSetupCellB3
            "B4" -> binding.ivSetupCellB4
            "B5" -> binding.ivSetupCellB5
            "B6" -> binding.ivSetupCellB6
            "B7" -> binding.ivSetupCellB7
            "B8" -> binding.ivSetupCellB8
            "C1" -> binding.ivSetupCellC1
            "C2" -> binding.ivSetupCellC2
            "C3" -> binding.ivSetupCellC3
            "C4" -> binding.ivSetupCellC4
            "C5" -> binding.ivSetupCellC5
            "C6" -> binding.ivSetupCellC6
            "C7" -> binding.ivSetupCellC7
            "C8" -> binding.ivSetupCellC8
            "D1" -> binding.ivSetupCellD1
            "D2" -> binding.ivSetupCellD2
            "D3" -> binding.ivSetupCellD3
            "D4" -> binding.ivSetupCellD4
            "D5" -> binding.ivSetupCellD5
            "D6" -> binding.ivSetupCellD6
            "D7" -> binding.ivSetupCellD7
            "D8" -> binding.ivSetupCellD8
            "E1" -> binding.ivSetupCellE1
            "E2" -> binding.ivSetupCellE2
            "E3" -> binding.ivSetupCellE3
            "E4" -> binding.ivSetupCellE4
            "E5" -> binding.ivSetupCellE5
            "E6" -> binding.ivSetupCellE6
            "E7" -> binding.ivSetupCellE7
            "E8" -> binding.ivSetupCellE8
            "F1" -> binding.ivSetupCellF1
            "F2" -> binding.ivSetupCellF2
            "F3" -> binding.ivSetupCellF3
            "F4" -> binding.ivSetupCellF4
            "F5" -> binding.ivSetupCellF5
            "F6" -> binding.ivSetupCellF6
            "F7" -> binding.ivSetupCellF7
            "F8" -> binding.ivSetupCellF8
            "G1" -> binding.ivSetupCellG1
            "G2" -> binding.ivSetupCellG2
            "G3" -> binding.ivSetupCellG3
            "G4" -> binding.ivSetupCellG4
            "G5" -> binding.ivSetupCellG5
            "G6" -> binding.ivSetupCellG6
            "G7" -> binding.ivSetupCellG7
            "G8" -> binding.ivSetupCellG8
            "H1" -> binding.ivSetupCellH1
            "H2" -> binding.ivSetupCellH2
            "H3" -> binding.ivSetupCellH3
            "H4" -> binding.ivSetupCellH4
            "H5" -> binding.ivSetupCellH5
            "H6" -> binding.ivSetupCellH6
            "H7" -> binding.ivSetupCellH7
            "H8" -> binding.ivSetupCellH8
            else -> binding.ivSetupCellA1
        }
    }


}