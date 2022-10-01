package br.ufrgs.inf.embarcados.battleship

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import br.ufrgs.inf.embarcados.battleship.databinding.FragmentEnemyFleetBinding
import java.util.*

class EnemyFleetFragment : Fragment() {

    private lateinit var binding: FragmentEnemyFleetBinding
    private var hits: LinkedList<String> = LinkedList()
    private var misses: LinkedList<String> = LinkedList()
    private var isEnemyPatrolDead = false
    private var isEnemyDestroyerDead = false
    private var isEnemyBattleshipDead = false
    private var isEnemyCarrierDead = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEnemyFleetBinding.inflate(inflater, container, false)
//        binding.dummyEnemyFleetFragmentTextView.text = "Hits: $hits \nMisses: $misses"

        drawHits()
        drawMisses()
        drawHealthIndicators()
        setupSpinners()
        return binding.root
    }

    public fun addHit(cell: String) {
        hits.add(cell)
    }

    public fun addMiss(cell: String) {
        misses.add(cell)
    }

    public fun setIsEnemyPatrolDead(isIt: Boolean) {
        isEnemyPatrolDead = isIt
    }

    public fun setIsEnemyDestroyerDead(isIt: Boolean) {
        isEnemyDestroyerDead = isIt
    }

    public fun setIsEnemyBattleshipDead(isIt: Boolean) {
        isEnemyBattleshipDead = isIt
    }

    public fun setIsEnemyCarrierDead(isIt: Boolean) {
        isEnemyCarrierDead = isIt
    }

    public fun setShipHealth(type: String, isDead: Boolean) {
        when(type) {
            "Patrol" -> setIsEnemyPatrolDead(isDead)
            "Destroyer" -> setIsEnemyDestroyerDead(isDead)
            "Battleship" -> setIsEnemyBattleshipDead(isDead)
            "Carrier" -> setIsEnemyCarrierDead(isDead)
            else -> {}
        }
        drawHealthIndicators()
    }

    public fun getNextActionChoice(): String {
        val row = binding.choiceNextActionRow.selectedItem.toString()
        val column = binding.choiceNextActionColumn.selectedItem.toString()
        return row + column
    }

    public fun drawHits() {
        for (cell in hits) {
            cellToView(cell).setImageResource(R.drawable.dummyexplosion)
        }
    }

    public fun drawMisses() {
        for (cell in misses) {
            cellToView(cell).setImageResource(R.drawable.dummymiss)
        }
    }

    private fun drawHealthIndicators() {
        if (isEnemyPatrolDead) {
            binding.containerEnemyPatrolHealth.setBackgroundResource(R.color.dead)
        }
        if (isEnemyDestroyerDead) {
            binding.containerEnemyDestroyerHealth.setBackgroundResource(R.color.dead)
        }
        if (isEnemyBattleshipDead) {
            binding.containerEnemyBattleshipHealth.setBackgroundResource(R.color.dead)
        }
        if (isEnemyCarrierDead) {
            binding.containerEnemyCarrierHealth.setBackgroundResource(R.color.dead)
        }
    }

    private fun setupSpinners() {
        var board = FleetBoard()
        val rows = board.allRows()
        val columns = board.allColumns()
        val rowsAdapter = ArrayAdapter(activity as Context, android.R.layout.simple_spinner_dropdown_item, rows)
        val columnsAdapter = ArrayAdapter(activity as Context, android.R.layout.simple_spinner_dropdown_item, columns)
        binding.choiceNextActionRow.adapter = rowsAdapter
        binding.choiceNextActionColumn.adapter = columnsAdapter
    }

    private fun cellToView(cell: String): ImageView {
        return when(cell) {
            "A1" -> binding.ivEnemyCellA1
            "A2" -> binding.ivEnemyCellA2
            "A3" -> binding.ivEnemyCellA3
            "A4" -> binding.ivEnemyCellA4
            "A5" -> binding.ivEnemyCellA5
            "A6" -> binding.ivEnemyCellA6
            "A7" -> binding.ivEnemyCellA7
            "A8" -> binding.ivEnemyCellA8
            "B1" -> binding.ivEnemyCellB1
            "B2" -> binding.ivEnemyCellB2
            "B3" -> binding.ivEnemyCellB3
            "B4" -> binding.ivEnemyCellB4
            "B5" -> binding.ivEnemyCellB5
            "B6" -> binding.ivEnemyCellB6
            "B7" -> binding.ivEnemyCellB7
            "B8" -> binding.ivEnemyCellB8
            "C1" -> binding.ivEnemyCellC1
            "C2" -> binding.ivEnemyCellC2
            "C3" -> binding.ivEnemyCellC3
            "C4" -> binding.ivEnemyCellC4
            "C5" -> binding.ivEnemyCellC5
            "C6" -> binding.ivEnemyCellC6
            "C7" -> binding.ivEnemyCellC7
            "C8" -> binding.ivEnemyCellC8
            "D1" -> binding.ivEnemyCellD1
            "D2" -> binding.ivEnemyCellD2
            "D3" -> binding.ivEnemyCellD3
            "D4" -> binding.ivEnemyCellD4
            "D5" -> binding.ivEnemyCellD5
            "D6" -> binding.ivEnemyCellD6
            "D7" -> binding.ivEnemyCellD7
            "D8" -> binding.ivEnemyCellD8
            "E1" -> binding.ivEnemyCellE1
            "E2" -> binding.ivEnemyCellE2
            "E3" -> binding.ivEnemyCellE3
            "E4" -> binding.ivEnemyCellE4
            "E5" -> binding.ivEnemyCellE5
            "E6" -> binding.ivEnemyCellE6
            "E7" -> binding.ivEnemyCellE7
            "E8" -> binding.ivEnemyCellE8
            "F1" -> binding.ivEnemyCellF1
            "F2" -> binding.ivEnemyCellF2
            "F3" -> binding.ivEnemyCellF3
            "F4" -> binding.ivEnemyCellF4
            "F5" -> binding.ivEnemyCellF5
            "F6" -> binding.ivEnemyCellF6
            "F7" -> binding.ivEnemyCellF7
            "F8" -> binding.ivEnemyCellF8
            "G1" -> binding.ivEnemyCellG1
            "G2" -> binding.ivEnemyCellG2
            "G3" -> binding.ivEnemyCellG3
            "G4" -> binding.ivEnemyCellG4
            "G5" -> binding.ivEnemyCellG5
            "G6" -> binding.ivEnemyCellG6
            "G7" -> binding.ivEnemyCellG7
            "G8" -> binding.ivEnemyCellG8
            "H1" -> binding.ivEnemyCellH1
            "H2" -> binding.ivEnemyCellH2
            "H3" -> binding.ivEnemyCellH3
            "H4" -> binding.ivEnemyCellH4
            "H5" -> binding.ivEnemyCellH5
            "H6" -> binding.ivEnemyCellH6
            "H7" -> binding.ivEnemyCellH7
            "H8" -> binding.ivEnemyCellH8
            else -> binding.ivEnemyCellA1
        }
    }

}