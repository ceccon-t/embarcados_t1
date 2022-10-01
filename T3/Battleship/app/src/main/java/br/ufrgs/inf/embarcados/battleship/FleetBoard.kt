package br.ufrgs.inf.embarcados.battleship

class FleetBoard {

    var board = Array(8) {Array(8) { true } }

    public fun isAvailable(cell: String): Boolean {
        val row = rowToIndex(cell[0].toString())
        val column = columnToIndex(cell[1].toString())
        return board[row][column]
    }

    public fun reserve(cell:String) {
        val row = rowToIndex(cell[0].toString())
        val column = columnToIndex(cell[1].toString())
        board[row][column] = false
    }

    public fun reservePath(path: Array<String>) {
        for (cell in path) {
            reserve(cell)
        }
    }

    public fun fitsAhead(initialCell: String, size: Int, direction: String): Boolean {
        var fits = false
        if (direction.equals("Vertical")) {
            val initialRow = rowToIndex(initialCell[0].toString())
            fits = initialRow + (size-1) < 8
        } else {
            val initialColumn = columnToIndex(initialCell[1].toString())
            fits = initialColumn + (size-1) < 8
        }
        return fits
    }

    public fun nextCell(currentCell: String, direction: String): String {
        return if (direction.equals("Vertical")) {
            val nextRow = indexToRow(rowToIndex(currentCell[0].toString())+1)
            nextRow + currentCell[1].toString()
        } else {
            val nextColumn = indexToColumn(columnToIndex(currentCell[1].toString())+1)
            currentCell[0].toString() + nextColumn
        }
    }

    public fun cellsInPath(initialCell: String, size: Int, direction: String): Array<String> {
        var path = Array(size) { "" }
        var currentCell = initialCell
        path[0] = currentCell
        for(i in 1 until size) {
            currentCell = nextCell(currentCell, direction)
            path[i] = currentCell
        }
        return path
    }

    public fun pathIsFree(path: Array<String>): Boolean {
        var row = 0
        var column = 0
        for (cell in path) {
            row = rowToIndex(cell[0].toString())
            column = columnToIndex(cell[1].toString())
            if (!board[row][column]) {
                return false
            }
        }
        return true
    }

    public fun allRotations(): Array<String> {
        return arrayOf("Horizontal", "Vertical")
    }

    public fun allRows(): Array<String> {
        return arrayOf("A", "B", "C", "D", "E", "F", "G", "H")
    }

    public fun allColumns(): Array<String> {
        return arrayOf("1", "2", "3", "4", "5", "6", "7", "8")
    }

    private fun rowToIndex(row: String): Int {
        return when(row) {
            "A" -> 0
            "B" -> 1
            "C" -> 2
            "D" -> 3
            "E" -> 4
            "F" -> 5
            "G" -> 6
            "H" -> 7
            else -> -1
        }
    }

    private fun indexToRow(index: Int): String {
        return when(index) {
            0 -> "A"
            1 -> "B"
            2 -> "C"
            3 -> "D"
            4 -> "E"
            5 -> "F"
            6 -> "G"
            7 -> "H"
            else -> "Z"
        }
    }

    private fun columnToIndex(column: String): Int {
        return when(column) {
            "1" -> 0
            "2" -> 1
            "3" -> 2
            "4" -> 3
            "5" -> 4
            "6" -> 5
            "7" -> 6
            "8" -> 7
            else -> -1
        }
    }

    private fun indexToColumn(index: Int): String {
        return when(index) {
            0 -> "1"
            1 -> "2"
            2 -> "3"
            3 -> "4"
            4 -> "5"
            5 -> "6"
            6 -> "7"
            7 -> "8"
            else -> "Z"
        }
    }

}