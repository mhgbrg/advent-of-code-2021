package se.hgbrg.adventofcode2021

object Day02 {
    fun part1(input: List<String>): Int {
        var horizonalPosition = 0
        var depth = 0
        for (command in input) {
            val (instruction, arg) = parseCommand(command)
            when (instruction) {
                "forward" -> horizonalPosition += arg
                "down" -> depth += arg
                "up" -> depth -= arg
                else -> throw RuntimeException()
            }
        }
        return horizonalPosition * depth
    }

    fun part2(input: List<String>): Int {
        var horizonalPosition = 0
        var depth = 0
        var aim = 0
        for (command in input) {
            val (instruction, arg) = parseCommand(command)
            when (instruction) {
                "forward" -> {
                    horizonalPosition += arg
                    depth += aim * arg
                }
                "down" -> aim += arg
                "up" -> aim -= arg
                else -> throw RuntimeException()
            }
        }
        return horizonalPosition * depth
    }

    fun parseCommand(command: String): Pair<String, Int> {
        val (instruction, arg) = command.split(" ")
        return Pair(instruction, arg.toInt())
    }
}

fun main() {
    val input = readInput(2)
    println(Day02.part1(input))
    println(Day02.part2(input))
}
