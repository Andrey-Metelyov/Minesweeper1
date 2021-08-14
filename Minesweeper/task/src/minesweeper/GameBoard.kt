package minesweeper

import java.lang.IllegalArgumentException
import kotlin.math.min
import kotlin.math.max
import kotlin.random.Random

class GameBoard(private val height: Int, private val width: Int, mines: Int) {
    enum class CellState(val ch: String) {
        EMPTY("."),
        OPEN("/"),
        MINE("X"),
        OPEN_MARKED("*"),
        EMPTY_MARKED("*"),
        MINE_MARKED("*"),
    }
    private val grid = Array(height) {
        Array(width) { CellState.EMPTY }
    }
    val marked: Boolean
        get() = isCorrectlyMarked()

    private fun isCorrectlyMarked(): Boolean {
        for (row in grid) {
            for (cell in row) {
                if (cell == CellState.OPEN_MARKED || cell == CellState.EMPTY_MARKED || cell == CellState.MINE) {
                    return false
                }
            }
        }
        return true
    }

    init {
        if (mines > height * width) {
            throw IllegalArgumentException("Number of mines ($mines) more than can hold ($height x $width = ${height * width}")
        }
        repeat(mines) {
            while (true) {
                val row = Random.nextInt(0, height)
                val col = Random.nextInt(0, width)
                if (grid[row][col] != CellState.MINE) {
                    grid[row][col] = CellState.MINE
//                    System.err.println("mine placed at $row $col")
                    break
                }
            }
        }
    }

    fun print(showMines: Boolean = false) {
        print(" |")
        for (i in 1..width) {
            print(i)
        }
        println("|")
        print("-|")
        for (i in 1..width) {
            print("-")
        }
        println("|")
        for ((i, row) in grid.withIndex()) {
//            System.err.println(row.joinToString("") { it.ch })
            print("${i+1}|")
            for ((j, cell) in row.withIndex()) {
                print(
                    if (cell == CellState.OPEN) {
                        val mines = getMinesAround(i, j)
                        if (mines > 0) {
                            mines
                        } else {
                            cell.ch
                        }
                    } else if (!showMines && cell == CellState.MINE) {
                        CellState.EMPTY.ch
                    } else {
                        cell.ch
                    }
                )
            }
            println("|")
        }
        print("-|")
        for (i in 1..width) {
            print("-")
        }
        println("|")
    }

    private fun getMinesAround(row: Int, col: Int): Int {
        var count = 0
        for (i in max(0, row - 1)..min(height - 1, row + 1)) {
            for (j in max(0, col - 1)..min(width - 1, col + 1)) {
                if (grid[i][j] == CellState.MINE || grid[i][j] == CellState.MINE_MARKED) {
                    count++
                }
            }
        }
        return count
    }

    fun mark(x: Int, y: Int, command: String): Boolean {
//        if (command == "free" && (grid[x - 1][y - 1] == CELL_STATE.MINE || grid[x - 1][y - 1] == CELL_STATE.MINE_MARKED)) {
//            return true
//        }
        System.err.println(grid[x - 1][y - 1])
        if (command == "free") {
            grid[x - 1][y - 1] = when (grid[x - 1][y - 1]) {
                CellState.MINE_MARKED -> return true
                CellState.MINE -> return true
                CellState.EMPTY -> CellState.OPEN
                CellState.OPEN -> CellState.OPEN
                CellState.EMPTY_MARKED -> CellState.OPEN
                CellState.OPEN_MARKED -> CellState.OPEN
            }
        } else {
            grid[x - 1][y - 1] = when (grid[x - 1][y - 1]) {
                CellState.MINE_MARKED -> CellState.MINE
                CellState.MINE -> CellState.MINE_MARKED
                CellState.EMPTY -> CellState.EMPTY_MARKED
                CellState.OPEN -> CellState.OPEN_MARKED
                CellState.EMPTY_MARKED -> CellState.EMPTY
                CellState.OPEN_MARKED -> CellState.OPEN
            }
        }
        if (grid[x - 1][y - 1] == CellState.OPEN) {
            markAllEmptyNeighbours(x - 1, y - 1)
        }
        System.err.println(grid[x - 1][y - 1])
        return false
    }

    private fun markAllEmptyNeighbours(row: Int, col: Int) {
        for (i in max(0, row - 1)..min(height - 1, row + 1)) {
            for (j in max(0, col - 1)..min(width - 1, col + 1)) {
                markEmptyNeighbours(i, j)
            }
        }
    }

    private fun markEmptyNeighbours(row: Int, col: Int) {
//        System.err.println("mark $row $col from ${grid[row][col]}")
        if (grid[row][col] != CellState.EMPTY && grid[row][col] != CellState.EMPTY_MARKED) {
            return
        }
        grid[row][col] = CellState.OPEN
        for (i in max(0, row - 1)..min(height - 1, row + 1)) {
            for (j in max(0, col - 1)..min(width - 1, col + 1)) {
                if (grid[i][j] == CellState.EMPTY || grid[i][j] == CellState.EMPTY_MARKED) {
//                    System.err.println("mark $i $j to ${CellState.OPEN}")
                    grid[i][j] = CellState.OPEN
                    markAllEmptyNeighbours(i, j)
                }
            }
        }
    }
}