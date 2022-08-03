package se.hgbrg.adventofcode2021

val input = readInput(1)

val measurements = input.map { it.toInt() }

measurements
    .zipWithNext()
    .sumOf { (x, y) -> if (y > x) 1L else 0L }

measurements
    .zipWithNext()
    .zip(measurements.drop(2)) { (x, y), z -> Triple(x, y, z) }
    .map { (x, y, z) -> x + y + z }
    .zipWithNext()
    .sumOf { (x, y) -> if (y > x) 1L else 0L }
