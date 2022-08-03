import se.hgbrg.adventofcode2021.memoize
import se.hgbrg.adventofcode2021.readInput
import se.hgbrg.adventofcode2021.readTestInput
import kotlin.math.min

val input = readInput(21)

val (p1Start, p2Start) = input
    .map { line ->
        line.split(": ")[1].toInt()
    }

// Part 1
playDeterministic(p1Start, p2Start)

// Part 2
val playQuantumMemoized = memoize { args: Pair<Pair<Int, Int>, Pair<Int, Int>> ->
    playQuantum(args.first, args.second)
}

playQuantumMemoized(Pair(
    Pair(p1Start, 0),
    Pair(p2Start, 0),
))

// Deterministic
fun playDeterministic(p1Start: Int, p2Start: Int): Int {
    var p1 = p1Start
    var p2 = p2Start

    var p1Score = 0
    var p2Score = 0

    val die = DeterministicDie()
    var turn = 0

    while (p1Score < 1000 && p2Score < 1000) {
        if (turn % 2 == 0) {
            p1 = move(p1, die)
            p1Score += p1
        } else {
            p2 = move(p2, die)
            p2Score += p2
        }
        turn++
    }

    return turn * 3 * min(p1Score, p2Score)
}

fun move(p: Int, die: Die): Int {
    val roll = roll3(die)
    return (p + roll - 1) % 10 + 1
}

fun roll3(die: Die): Int {
    return die.roll() + die.roll() + die.roll()
}

interface Die {
    fun roll(): Int
}

class DeterministicDie: Die {
    private var rolls = 0

    override fun roll(): Int {
        return (rolls++ % 100) + 1
    }
}

// Quantum
fun playQuantum(p1: Pair<Int, Int>, p2: Pair<Int, Int>): Pair<Long, Long> {
    val winningScore = 21

    val (p1Pos, p1Score) = p1
    val (p2Pos, p2Score) = p2

    var p1Wins = 0L
    var p2Wins = 0L

    for (p1Roll in rollQuantum3()) {
        val newP1Pos = (p1Pos + p1Roll - 1) % 10 + 1
        val newP1Score = p1Score + newP1Pos
        if (newP1Score >= winningScore) {
            p1Wins++
            continue
        }
        for (p2Roll in rollQuantum3()) {
            val newP2Pos = (p2Pos + p2Roll - 1) % 10 + 1
            val newP2Score = p2Score + newP2Pos
            if (newP2Score >= winningScore) {
                p2Wins++
                continue
            }
            val result = playQuantumMemoized(Pair(
                Pair(newP1Pos, newP1Score),
                Pair(newP2Pos, newP2Score),
            ))
            p1Wins += result.first
            p2Wins += result.second
        }
    }

    return Pair(p1Wins, p2Wins)
}

fun rollQuantum3(): List<Int> {
    val rolls = mutableListOf<Int>()
    for (a in 1..3) {
        for (b in 1..3) {
            for (c in 1..3) {
                rolls.add(a + b + c)
            }
        }
    }
    return rolls
}

