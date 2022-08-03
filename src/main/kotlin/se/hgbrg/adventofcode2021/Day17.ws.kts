import se.hgbrg.adventofcode2021.Pos
import se.hgbrg.adventofcode2021.readInput
import se.hgbrg.adventofcode2021.readTestInput
import kotlin.math.max

val input = readInput(17)

val (targetXRange, targetYRange) = input[0].replace("target area: ", "")
    .split(", ")
    .map {
        val (start, end) = it.drop(2).split("..").map { it.toInt() }
        start..end
    }

targetXRange
targetYRange

// Part 1
(1..targetXRange.last)
    .map { xVelocity ->
        val velocity = Velocity(xVelocity, -(targetYRange.first + 1))
        simulate(velocity)
    }
    .filter { hitsTargetArea(it) }
    .maxOfOrNull { maxHeight(it) }

// Part 2
(1..targetXRange.last)
    .flatMap { x -> (targetYRange.first..(-targetYRange.first+1)).map { y -> Velocity(x, y) } }
    .map { velocity -> simulate(velocity) }
    .filter { hitsTargetArea(it) }
    .size

fun simulate(velocity: Velocity): Sequence<Pos> {
    return generateSequence(0) { it.inc() }
        .runningFold(Pair(Pos(0, 0), velocity)) { (pos, velocity), _ ->
            Pair(
                Pos(pos.x + velocity.x, pos.y + velocity.y),
                Velocity(max(velocity.x - 1, 0), velocity.y - 1),
            )
        }
        .map { it.first }
}

fun hitsTargetArea(sequence: Sequence<Pos>): Boolean {
    val lastPos = sequence
        .takeWhile { pos -> pos.x <= targetXRange.last() && pos.y >= targetYRange.first() }
        .last()
    return targetXRange.contains(lastPos.x) && targetYRange.contains(lastPos.y)
}

fun maxHeight(sequence: Sequence<Pos>): Int {
    return sequence
        .takeWhile { it.y >=0  }
        .map { it.y }
        .maxOrNull()!!
}

data class Velocity(val x: Int, val y: Int)

