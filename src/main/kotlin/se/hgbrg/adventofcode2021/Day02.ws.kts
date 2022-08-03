package se.hgbrg.adventofcode2021

val input = readInput(2)

val (h1, d1) = input
    .map { parseCommand(it) }
    .fold(Pair(0, 0)) { (h, d), (instruction, arg) ->
        when (instruction) {
            "forward" -> Pair(h + arg, d)
            "down" -> Pair(h, d + arg)
            "up" -> Pair(h, d - arg)
            else -> throw RuntimeException()
        }
    }

h1
d1
h1 * d1

val (h2, d2, a) = input
    .map { parseCommand(it) }
    .fold(Triple(0, 0, 0)) { (h, d, a), (instruction, arg) ->
        when (instruction) {
            "forward" -> Triple(h + arg, d + a * arg, a)
            "down" -> Triple(h, d, a + arg)
            "up" -> Triple(h, d, a - arg)
            else -> throw RuntimeException()
        }
    }

h2
d2
a
h2 * d2

fun parseCommand(command: String): Pair<String, Int> {
    val (instruction, arg) = command.split(" ")
    return Pair(instruction, arg.toInt())
}
