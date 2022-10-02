package br.ufrgs.inf.embarcados.battleship

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import br.ufrgs.inf.embarcados.battleship.databinding.ActivityBattleBinding
import br.ufrgs.inf.embarcados.battleship.network.ServerServiceBuilder
import br.ufrgs.inf.embarcados.battleship.network.match.MatchRequestInterface
import br.ufrgs.inf.embarcados.battleship.network.match.MatchRequestModel
import br.ufrgs.inf.embarcados.battleship.network.match.MatchResponseModel
import com.google.android.material.navigation.NavigationBarView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.concurrent.schedule

class BattleActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {

    private lateinit var binding: ActivityBattleBinding
    private lateinit var your_fleet_fragment: YourFleetFragment
    private lateinit var enemy_fleet_fragment: EnemyFleetFragment
    private lateinit var currentFragment: Fragment

    private var sessionKey = ""
    private var yourId = ""

    private var isPlaying: Boolean = true
    private var turn: Int = 0
    private var myLastAction: String = ""
    private var isPlayerTurn: Boolean = true
    private lateinit var player: Player
    private var playerBoard: FleetBoard = FleetBoard()
    private lateinit var enemy: Player

    private var enemyWaitTimer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBattleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionKey = intent.getStringExtra("Key").toString()
        yourId = intent.getStringExtra("YourId").toString()

        val patrolSerialized = intent.getStringExtra("Patrol").toString()
        val destroyerSerialized = intent.getStringExtra("Destroyer").toString()
        val battleshipSerialized = intent.getStringExtra("Battleship").toString()
        val carrierSerialized = intent.getStringExtra("Carrier").toString()
        val enemyPatrolSerialized = intent.getStringExtra("EnemyPatrol").toString()
        val enemyDestroyerSerialized = intent.getStringExtra("EnemyDestroyer").toString()
        val enemyBattleshipSerialized = intent.getStringExtra("EnemyBattleship").toString()
        val enemyCarrierSerialized = intent.getStringExtra("EnemyCarrier").toString()

        player = Player(Ship("Patrol", patrolSerialized),
            Ship("Destroyer", destroyerSerialized),
            Ship("Battleship", battleshipSerialized),
            Ship("Carrier", carrierSerialized))
        turn = 0
        isPlayerTurn = yourId.equals("player1")
        if (!isPlayerTurn) {
            setEnemyTurn()
        }

        enemy = Player(Ship("Patrol", enemyPatrolSerialized),
            Ship("Destroyer", enemyDestroyerSerialized),
            Ship("Battleship", enemyBattleshipSerialized),
            Ship("Carrier", enemyCarrierSerialized))

        binding.bottomNavigationMenu.setOnItemSelectedListener(this)

        your_fleet_fragment = YourFleetFragment()

        setActiveFragment(your_fleet_fragment)
        your_fleet_fragment.setPatrol(patrolSerialized)
        your_fleet_fragment.setDestroyer(destroyerSerialized)
        your_fleet_fragment.setBattleship(battleshipSerialized)
        your_fleet_fragment.setCarrier(carrierSerialized)

        enemy_fleet_fragment = EnemyFleetFragment()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.nav_your_fleet -> loadYourFleetView()
            R.id.nav_enemy_fleet -> loadEnemyFleetView()
            else -> false
        }
    }

    public fun handleFireButton(v: View) {
        if (!isPlaying) return
        val cellChosen = enemy_fleet_fragment.getNextActionChoice()
        if (isPlayerTurn) {
            if (!playerBoard.isAvailable(cellChosen)) {
                Toast.makeText(this, "You already fired at cell $cellChosen...", Toast.LENGTH_SHORT).show()
                return
            }

            Toast.makeText(this, "Firing at cell $cellChosen.", Toast.LENGTH_SHORT).show()
            handlePlayerAction(cellChosen)
        }
//        else {
//            Toast.makeText(this, "Enemy firing at cell $cellChosen.", Toast.LENGTH_SHORT).show()
//            handleEnemyAction(cellChosen)
//        }
    }

    private fun handlePlayerAction(cell: String) {
        playerBoard.reserve(cell)
        var hit = false
        for (ship: Ship in enemy.getFleet()) {
            if (ship.hits(cell)) {
                enemy_fleet_fragment.setShipHealth(ship.getType(), ship.isDead())
                hit = true
            }
        }

        if (hit) {
            enemy_fleet_fragment.addHit(cell)
        } else {
            enemy_fleet_fragment.addMiss(cell)
        }

        myLastAction = cell
        advanceTurn()
    }

    private fun handleEnemyAction(cell: String) {
        var result = "Miss"
        var hit = false
        for (ship: Ship in player.getFleet()) {
            if (ship.hits(cell)) {
                your_fleet_fragment.setShipHealth(ship.getType(), ship.isDead())
                hit = true
            }
        }

        if (hit) {
            your_fleet_fragment.addHit(cell)
            result = "HIT"
        } else {
            your_fleet_fragment.addMiss(cell)
        }
        your_fleet_fragment.setLastEnemyAction(cell, result)
        advanceTurn()
    }

    private fun advanceTurn() {

        if (currentFragment == your_fleet_fragment) {
            your_fleet_fragment.drawUiVariableElements()
        } else {
            enemy_fleet_fragment.drawUiVariableElements()
        }

        if (player.hasLost()) {
            binding.textViewGameStatus.text = "You LOST"
            isPlaying = false
            return
        }
        if (enemy.hasLost()) {
            binding.textViewGameStatus.text = "You WON"
            isPlaying = false
            turn += 1
            waitForEnemyAction()
            return
        }

        turn += 1
        if (isPlayerTurn) {
            setEnemyTurn()
        } else {
            setPlayerTurn()
        }
    }

    private fun setEnemyTurn() {
        binding.textViewGameStatus.text = "Waiting enemy action"
        isPlayerTurn = false
        waitForEnemyAction()
    }

    private fun setPlayerTurn() {
        binding.textViewGameStatus.text = "YOUR turn"
        isPlayerTurn = true
    }

    private fun waitForEnemyAction() {
        runOnUiThread {

            val requestModel = MatchRequestModel(sessionKey, yourId, turn, myLastAction)
            val response = ServerServiceBuilder.buildService(MatchRequestInterface::class.java)
            response.sendRequest(requestModel).enqueue(
                object: Callback<MatchResponseModel> {
                    override fun onResponse(
                        call: Call<MatchResponseModel>,
                        response: Response<MatchResponseModel>
                    ) {
                        handleServerMatchResponse(response.body()!!)
                    }

                    override fun onFailure(call: Call<MatchResponseModel>, t: Throwable) {
                        Toast.makeText(this@BattleActivity, "Having trouble to connect with server, trying again...", Toast.LENGTH_SHORT).show()
                        enemyWaitTimer.schedule(10_000) {
                            waitForEnemyAction()
                        }
                    }
                }
            )
        }
    }

    private fun handleServerMatchResponse(serverResponse: MatchResponseModel) {
        if (!isPlaying) return
        if (serverResponse.lastActor.equals(yourId))  {
//            Toast.makeText(this, "Waiting...", Toast.LENGTH_SHORT).show()
            enemyWaitTimer.schedule(10_000) {
                waitForEnemyAction()
            }
        } else {
            handleEnemyAction(serverResponse.lastAction)
        }
    }

    private fun setActiveFragment(f: Fragment) {
        currentFragment = f
        supportFragmentManager.commit {
            replace(R.id.active_fragment, f)
        }
    }

    private fun loadEnemyFleetView(): Boolean {
        setActiveFragment(enemy_fleet_fragment)
        return true
    }

    private fun loadYourFleetView(): Boolean {
        setActiveFragment(your_fleet_fragment)
        return true
    }

}