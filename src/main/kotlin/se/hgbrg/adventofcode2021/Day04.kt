package se.hgbrg.adventofcode2021

private class Board(val board: Array<Array<Int>>) {
    private var marked: Array<Array<Boolean>> = board
        .map { row ->
            row
                .map { false }
                .toTypedArray()
        }
        .toTypedArray()

    fun mark(number: Int) {
        for (i in 0..4) {
            for (j in 0..4) {
                if (board[i][j] == number) {
                    marked[i][j] = true
                }
            }
        }
    }

    fun wins(): Boolean {
        outer@ for (i in 0..4) {
            for (j in 0..4) {
                if (!marked[i][j]) {
                    continue@outer
                }
            }
            return true
        }

        outer@ for (j in 0..4) {
            for (i in 0..4) {
                if (!marked[i][j]) {
                    continue@outer
                }
            }
            return true
        }

        return false
    }

    fun score(number: Int): Int {
        var sum = 0
        for (i in 0..4) {
            for (j in 0..4) {
                if (!marked[i][j]) {
                    sum += board[i][j]
                }
            }
        }
        return sum * number
    }
}

object Day04 {
    fun part1(input: List<String>): Int {
        val (numbers, boards) = parseInput(input)
        var minIndex = Int.MAX_VALUE
        var winningScore = 0
        for (board in boards) {
            val (score, index) = evaluate(board, numbers)
            if (index < minIndex) {
                minIndex = index
                winningScore = score
            }
        }
        return winningScore
    }

    fun part2(input: List<String>): Int {
        val (numbers, boards) = parseInput(input)
        var maxIndex = -1
        var winningScore = 0
        for (board in boards) {
            val (score, index) = evaluate(board, numbers)
            if (index > maxIndex) {
                maxIndex = index
                winningScore = score
            }
        }
        return winningScore
    }

    private fun parseInput(input: List<String>): Pair<List<Int>, List<Board>> {
        val numbers = input[0].split(",").map { it.toInt() }
        val boards = input
            .drop(2)
            .flatMap { it.split(" ") }
            .filter { it.isNotEmpty() }
            .map { it.toInt() }
            .chunked(5) { it.toTypedArray() }
            .chunked(5) { it.toTypedArray() }
            .map { Board(it) }
        return Pair(numbers, boards)
    }

    private fun evaluate(board: Board, numbers: List<Int>): Pair<Int, Int> {
        for (index in numbers.indices) {
            val number = numbers[index]
            board.mark(number)
            if (board.wins()) {
                val score = board.score(number)
                return Pair(score, index)
            }
        }
        throw RuntimeException()
    }
}

fun main() {
    val input = readInput(4)
    println(Day04.part1(input))
    println(Day04.part2(input))
}
