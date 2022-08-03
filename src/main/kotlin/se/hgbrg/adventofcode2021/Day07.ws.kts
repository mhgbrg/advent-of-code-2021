package se.hgbrg.adventofcode2021

import kotlin.math.abs

val input = readInput(7)

val positions = input[0]
    .split(",")
    .map { it.toInt() }

val target = positions
    .sorted()[positions.size / 2]

positions
    .map { abs(it - target) }
    .sum()

val min = positions.minOrNull()!!
val max = positions.maxOrNull()!!

min
max

val fuelCosts = (min..max)
    .map { target ->
        positions.sumOf { pos ->
            val distance = abs(pos - target)
            // https://simple.wikipedia.org/wiki/Triangular_number
            distance * (distance + 1) / 2
        }
    }

fuelCosts.sorted()

fuelCosts.minOrNull()
