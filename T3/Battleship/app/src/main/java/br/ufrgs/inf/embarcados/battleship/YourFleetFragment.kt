package br.ufrgs.inf.embarcados.battleship

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import br.ufrgs.inf.embarcados.battleship.databinding.FragmentYourFleetBinding
import java.util.*

class YourFleetFragment : Fragment() {

    private lateinit var binding: FragmentYourFleetBinding
    private var patrol: String = ""
    private var destroyer: String = ""
    private var battleship: String = ""
    private var carrier: String = ""
    private var isYourPatrolDead = false
    private var isYourDestroyerDead = false
    private var isYourBattleshipDead = false
    private var isYourCarrierDead = false
    private var hits: LinkedList<String> = LinkedList()
    private var misses: LinkedList<String> = LinkedList()
    private var lastEnemyActionCell: String = "  "
    private var lastEnemyActionResult: String = "    "
    private var lastEnemyActionText: String = "Last action of enemy:    -      "
    private var dummyText = "Nothing set yet"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentYourFleetBinding.inflate(inflater, container, false)
//        binding.dummyYourFleetFragmentTextView.text = "Ships positions: \nPatrol: $patrol \nDestroyer: $destroyer \nBattleship: $battleship \nCarrier: $carrier \nHits: $hits \nMisses: $misses"
//        setDummyText(dummyText)

        drawShips()
        drawEnemyActions()
        drawLastEnemyActionIndicator()
        drawHealthIndicators()
        return binding.root
    }

    public fun setDummyText(text: String) {
//        binding.dummyYourFleetFragmentTextView.text = "Text set from code: $text"
        dummyText = text
    }

    public fun setPatrol(serialized: String) {
        patrol = serialized
    }

    public fun setDestroyer(serialized: String) {
        destroyer = serialized
    }

    public fun setBattleship(serialized: String) {
        battleship = serialized
    }

    public fun setCarrier(serialized: String) {
        carrier = serialized
    }

    public fun addHit(cell: String) {
        hits.add(cell)
    }

    public fun addMiss(cell: String) {
        misses.add(cell)
    }

    public fun setIsYourPatrolDead(isIt: Boolean) {
        isYourPatrolDead = isIt
    }

    public fun setIsYourDestroyerDead(isIt: Boolean) {
        isYourDestroyerDead = isIt
    }

    public fun setIsYourBattleshipDead(isIt: Boolean) {
        isYourBattleshipDead = isIt
    }

    public fun setIsYourCarrierDead(isIt: Boolean) {
        isYourCarrierDead = isIt
    }

    public fun setShipHealth(type: String, isDead: Boolean) {
        when(type) {
            "Patrol" -> setIsYourPatrolDead(isDead)
            "Destroyer" -> setIsYourDestroyerDead(isDead)
            "Battleship" -> setIsYourBattleshipDead(isDead)
            "Carrier" -> setIsYourCarrierDead(isDead)
            else -> {}
        }
        drawHealthIndicators()
    }

    public fun setLastEnemyAction(cell: String, result: String) {
        lastEnemyActionCell = cell
        lastEnemyActionResult = result
    }

    private fun drawShips() {
        for (cell in patrol.split(",")) {
            cellToView(cell).setImageResource(R.drawable.dummypatrol)
        }
        for (cell in destroyer.split(",")) {
            cellToView(cell).setImageResource(R.drawable.dummydestroyer)
        }
        for (cell in battleship.split(",")) {
            cellToView(cell).setImageResource(R.drawable.dummybattleship)
        }
        for (cell in carrier.split(",")) {
            cellToView(cell).setImageResource(R.drawable.dummycarrier)
        }
    }

    private fun drawEnemyActions() {
        for (cell in hits) {
            cellToView(cell).setImageResource(R.drawable.dummyexplosion)
        }
        for (cell in misses) {
            cellToView(cell).setImageResource(R.drawable.dummymiss)
        }
    }

    private fun drawLastEnemyActionIndicator() {
        binding.lastActionOfEnemy.text = "Last action of enemy: $lastEnemyActionCell - $lastEnemyActionResult "
    }

    private fun drawHealthIndicators() {
        if (isYourPatrolDead) {
            binding.containerYourPatrolHealth.setBackgroundResource(R.color.dead)
        }
        if (isYourDestroyerDead) {
            binding.containerYourDestroyerHealth.setBackgroundResource(R.color.dead)
        }
        if (isYourBattleshipDead) {
            binding.containerYourBattleshipHealth.setBackgroundResource(R.color.dead)
        }
        if (isYourCarrierDead) {
            binding.containerYourCarrierHealth.setBackgroundResource(R.color.dead)
        }
    }

    private fun cellToView(cell: String): ImageView {
        return when(cell) {
            "A1" -> binding.ivPlayerCellA1
            "A2" -> binding.ivPlayerCellA2
            "A3" -> binding.ivPlayerCellA3
            "A4" -> binding.ivPlayerCellA4
            "A5" -> binding.ivPlayerCellA5
            "A6" -> binding.ivPlayerCellA6
            "A7" -> binding.ivPlayerCellA7
            "A8" -> binding.ivPlayerCellA8
            "B1" -> binding.ivPlayerCellB1
            "B2" -> binding.ivPlayerCellB2
            "B3" -> binding.ivPlayerCellB3
            "B4" -> binding.ivPlayerCellB4
            "B5" -> binding.ivPlayerCellB5
            "B6" -> binding.ivPlayerCellB6
            "B7" -> binding.ivPlayerCellB7
            "B8" -> binding.ivPlayerCellB8
            "C1" -> binding.ivPlayerCellC1
            "C2" -> binding.ivPlayerCellC2
            "C3" -> binding.ivPlayerCellC3
            "C4" -> binding.ivPlayerCellC4
            "C5" -> binding.ivPlayerCellC5
            "C6" -> binding.ivPlayerCellC6
            "C7" -> binding.ivPlayerCellC7
            "C8" -> binding.ivPlayerCellC8
            "D1" -> binding.ivPlayerCellD1
            "D2" -> binding.ivPlayerCellD2
            "D3" -> binding.ivPlayerCellD3
            "D4" -> binding.ivPlayerCellD4
            "D5" -> binding.ivPlayerCellD5
            "D6" -> binding.ivPlayerCellD6
            "D7" -> binding.ivPlayerCellD7
            "D8" -> binding.ivPlayerCellD8
            "E1" -> binding.ivPlayerCellE1
            "E2" -> binding.ivPlayerCellE2
            "E3" -> binding.ivPlayerCellE3
            "E4" -> binding.ivPlayerCellE4
            "E5" -> binding.ivPlayerCellE5
            "E6" -> binding.ivPlayerCellE6
            "E7" -> binding.ivPlayerCellE7
            "E8" -> binding.ivPlayerCellE8
            "F1" -> binding.ivPlayerCellF1
            "F2" -> binding.ivPlayerCellF2
            "F3" -> binding.ivPlayerCellF3
            "F4" -> binding.ivPlayerCellF4
            "F5" -> binding.ivPlayerCellF5
            "F6" -> binding.ivPlayerCellF6
            "F7" -> binding.ivPlayerCellF7
            "F8" -> binding.ivPlayerCellF8
            "G1" -> binding.ivPlayerCellG1
            "G2" -> binding.ivPlayerCellG2
            "G3" -> binding.ivPlayerCellG3
            "G4" -> binding.ivPlayerCellG4
            "G5" -> binding.ivPlayerCellG5
            "G6" -> binding.ivPlayerCellG6
            "G7" -> binding.ivPlayerCellG7
            "G8" -> binding.ivPlayerCellG8
            "H1" -> binding.ivPlayerCellH1
            "H2" -> binding.ivPlayerCellH2
            "H3" -> binding.ivPlayerCellH3
            "H4" -> binding.ivPlayerCellH4
            "H5" -> binding.ivPlayerCellH5
            "H6" -> binding.ivPlayerCellH6
            "H7" -> binding.ivPlayerCellH7
            "H8" -> binding.ivPlayerCellH8
            else -> binding.ivPlayerCellA1
        }
    }


}