package br.ufrgs.inf.embarcados.battleship

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import br.ufrgs.inf.embarcados.battleship.databinding.ActivityBattleBinding
import com.google.android.material.navigation.NavigationBarView

class BattleActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {

    private lateinit var binding: ActivityBattleBinding
    private lateinit var your_fleet_fragment: YourFleetFragment
    private lateinit var enemy_fleet_fragment: EnemyFleetFragment

    private var isPlaying: Boolean = true
    private var turn: Int = 0
    private var isPlayerTurn: Boolean = true
    private lateinit var player: Player
    private var playerBoard: FleetBoard = FleetBoard()
    private lateinit var enemy: Player

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBattleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val patrolSerialized = intent.getStringExtra("Patrol").toString()
        val destroyerSerialized = intent.getStringExtra("Destroyer").toString()
        val battleshipSerialized = intent.getStringExtra("Battleship").toString()
        val carrierSerialized = intent.getStringExtra("Carrier").toString()

        player = Player(Ship("Patrol", patrolSerialized),
            Ship("Destroyer", destroyerSerialized),
            Ship("Battleship", battleshipSerialized),
            Ship("Carrier", carrierSerialized))
        turn = 0
        isPlayerTurn = true

        enemy = Player(Ship("Patrol", patrolSerialized),
            Ship("Destroyer", destroyerSerialized),
            Ship("Battleship", battleshipSerialized),
            Ship("Carrier", carrierSerialized))

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
        else {
            Toast.makeText(this, "Enemy firing at cell $cellChosen.", Toast.LENGTH_SHORT).show()
            handleEnemyAction(cellChosen)
        }
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
            enemy_fleet_fragment.drawHits()
        } else {
            enemy_fleet_fragment.addMiss(cell)
            enemy_fleet_fragment.drawMisses()
        }
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

        if (player.hasLost()) {
            binding.textViewGameStatus.text = "You LOST"
            isPlaying = false
            return
        }
        if (enemy.hasLost()) {
            binding.textViewGameStatus.text = "You WON"
            isPlaying = false
            return
        }

        if (isPlayerTurn) {
            binding.textViewGameStatus.text = "Waiting enemy action"
            isPlayerTurn = false
        } else {
            binding.textViewGameStatus.text = "YOUR turn"
            isPlayerTurn = true
        }
        turn += 1
    }

    private fun setActiveFragment(f: Fragment) {
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