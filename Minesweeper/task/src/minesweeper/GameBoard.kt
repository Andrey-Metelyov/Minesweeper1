package minesweeper

import java.lang.IllegalArgumentException
import kotlin.random.Random

class GameBoard(val height: Int, val width: Int, mines: Int) {
    enum class CELL_STATE(val ch: String) {
        EMPTY("."),
        MINE("X"),
        MARK("?")
    }
    val grid = Array<Array<CELL_STATE>>(height) {
        Array(width) { CELL_STATE.EMPTY }
    }
    init {
        if (mines > height * width) {
            throw IllegalArgumentException("Number of mines ($mines) more than can hold ($height x $width = ${height * width}")
        }
        repeat(mines) {
            while (true) {
                val row = Random.nextInt(0, height - 1)
                val col = Random.nextInt(0, width - 1)
                if (grid[row][col] != CELL_STATE.MINE) {
                    grid[row][col] = CELL_STATE.MINE
                    break
                }
            }
        }
    }
    fun print() {
        for (row in grid) {
            println(row.joinToString("") { it.ch })
        }
    }
}