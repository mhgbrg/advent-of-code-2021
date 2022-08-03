package se.hgbrg.adventofcode2021

object Day01 {
    fun part1(input: List<String>): Int {
        val measurements = input.map { it.toInt() }
        var increaseCount = 0
        var prev = measurements[0]
        for (i in 1 until measurements.size) {
            val current = measurements[i]
            if (current > prev) {
                increaseCount++
            }
            prev = current
        }
        return increaseCount
    }

    fun part2(input: List<String>): Int {
        val measurements = input.map { it.toInt() }
        var increaseCount = 0
        var prev = measurements[0] + measurements[1] + measurements[2]
        for (i in 1 until measurements.size - 2) {
            val current = measurements[i] + measurements[i+1] + measurements[i+2]
            if (current > prev) {
                increaseCount++
            }
            prev = current
        }
        return increaseCount
    }
}

fun main() {
    val input = readTestInput(1)
    println(Day01.part1(input))
    println(Day01.part2(input))
}
