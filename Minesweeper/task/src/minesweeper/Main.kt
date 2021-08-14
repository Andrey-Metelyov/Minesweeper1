package minesweeper

fun main() {
    print("How many mines do you want on the field? ")
    val mines = readLine()!!.toInt()
    val board = GameBoard(9, 9, mines)
    while (true) {
        board.print()
        print("Set/unset mines marks or claim a cell as free: ")
        val (x, y, command) = readLine()!!.split(" ")
        if (board.mark(y.toInt(), x.toInt(), command)) {
            board.print(true)
            println("You stepped on a mine and failed!")
            break
        }
        if (board.marked) {
            board.print()
            println("Congratulations! You found all the mines!")
            break
        }
    }
}
