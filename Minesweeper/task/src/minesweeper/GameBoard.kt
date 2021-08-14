package minesweeper

import java.lang.IllegalArgumentException
import kotlin.math.min
import kotlin.math.max
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
        for ((i, row) in grid.withIndex()) {
//            System.err.println(row.joinToString("") { it.ch })
            for ((j, cell) in row.withIndex()) {
                print(
                    if (cell != CELL_STATE.MINE) {
                        val mines = getMinesAround(i, j)
                        if (mines > 0) {
                            mines
                        } else {
                            cell.ch
                        }
                    } else {
                        cell.ch
                    }
                )
            }
            println()
        }
    }

    private fun getMinesAround(row: Int, col: Int): Int {
        var count = 0
        for (i in max(0, row - 1)..min(height - 1, row + 1)) {
            for (j in max(0, col - 1)..min(width - 1, col + 1)) {
                if (grid[i][j] == CELL_STATE.MINE) {
                    count++
                }
            }
        }
        return count
    }
}