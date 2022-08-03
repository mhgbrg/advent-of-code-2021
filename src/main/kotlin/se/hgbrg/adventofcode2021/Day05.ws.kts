package se.hgbrg.adventofcode2021

val input = readInput(5)

data class Range(val start: Pos, val end: Pos) {
    fun isStraight(): Boolean {
        return start.x == end.x || start.y == end.y
    }

    fun expand(): List<Pos> {
        if (start.x == end.x) {
            return progression(start.y, end.y)
                .map { Pos(start.x, it) }
        } else if (start.y == end.y) {
            return progression(start.x, end.x)
                .map { Pos(it, start.y) }
        }
        return progression(start.x, end.x)
            .zip(progression(start.y, end.y))
            .map { (x, y) -> Pos(x, y) }
    }

    private fun progression(start: Int, end: Int): IntProgression {
        return if (start <= end) {
            start..end
        } else  {
            start downTo end
        }
    }
}

val ranges = input
    .map { line ->
        val (start, end) = line.split(" -> ")
        val (startX, startY) = start.split(",").map { it.toInt() }
        val (endX, endY) = end.split(",").map { it.toInt() }
        Range(Pos(startX, startY), Pos(endX, endY))
    }

ranges
    .filter { it.isStraight() }
    .flatMap { it.expand() }
    .groupingBy { it }
    .eachCount()
    .filterValues { it >= 2 }
    .count()

ranges
    .flatMap { it.expand() }
    .groupingBy { it }
    .eachCount()
    .filterValues { it >= 2 }
    .count()
