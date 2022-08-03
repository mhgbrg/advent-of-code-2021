package se.hgbrg.adventofcode2021

val input = readInput(4)

val numbers = input[0].split(",").map { it.toInt() }

val boards = input
    .drop(2)
    .flatMap { it.split(" ") }
    .filter { it.isNotEmpty() }
    .map { it.toInt() }
    .chunked(5)
    .chunked(5)

boards
    .map { evaluate(it, numbers) }
    .sortedBy { it?.second }
    .first()

boards
    .map { evaluate(it, numbers) }
    .sortedBy { it?.second }
    .last()

fun evaluate(board: List<List<Int>>, numbers: List<Int>): Pair<Int, Int>? {
    val mutableBoard = createMutableBoard(board)
    for (index in numbers.indices) {
        val number = numbers[index]
        val pos = getPos(board, number)
        if (pos != null) {
            mutableBoard[pos.first][pos.second] = Pair(number, true)
        }
        if (wins(mutableBoard)) {
            return Pair(score(mutableBoard, number), index)
        }
    }
    return null
}

fun createMutableBoard(board: List<List<Int>>): MutableList<MutableList<Pair<Int, Boolean>>> {
    return board.map { row ->
        row
            .map { number -> Pair(number, false) }
            .toMutableList()
    }
        .toMutableList()
}

fun getPos(board: List<List<Int>>, number: Int): Pair<Int, Int>? {
    return board
        .mapIndexed { index, row -> Pair(index, row.indexOf(number)) }
        .filter { it.second != -1 }
        .getOrNull(0)
}

fun wins(board: List<List<Pair<Int, Boolean>>>): Boolean {
    return winsAux(board) || winsAux(transpose(board))
}

fun <T> transpose(board: List<List<T>>): List<List<T>> {
    return (0..4).map {
        i -> (0..4).map {
            j -> board[j][i]
        }
    }
}

fun winsAux(board: List<List<Pair<Int, Boolean>>>): Boolean {
    return board
        .map { row ->
            row
                .map { it.second }
                .all { it }
        }
        .contains(true)
}

fun score(board: List<List<Pair<Int, Boolean>>>, number: Int): Int {
    val unmarked = board
        .flatten()
        .filter { !it.second }
        .map { it.first }
    return unmarked.sum() * number
}

