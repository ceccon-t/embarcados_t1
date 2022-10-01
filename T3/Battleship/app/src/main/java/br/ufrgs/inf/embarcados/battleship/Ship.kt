package br.ufrgs.inf.embarcados.battleship

class Ship(val shipType: String, val cells: String) {

    private var aliveParts = 0

    init {
        aliveParts = sizeForType(shipType)
    }

    public fun isDead(): Boolean {
        return aliveParts < 1
    }

    public fun hits(cell: String): Boolean {
        if (cells.contains(cell)) {
            aliveParts -= 1
            return true
        } else {
            return false
        }
    }

    public fun getType(): String {
        return shipType
    }

    private fun sizeForType(type: String): Int {
        return when(type) {
            "Patrol" -> 2
            "Destroyer" -> 3
            "Battleship" -> 4
            "Carrier" -> 5
            else -> 0
        }
    }
}