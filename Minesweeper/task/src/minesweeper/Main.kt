package minesweeper

fun main() {
    print("How many mines do you want on the field? ")
    val mines = readLine()!!.toInt()
    val board = GameBoard(9, 9, mines)
    board.print()
}
