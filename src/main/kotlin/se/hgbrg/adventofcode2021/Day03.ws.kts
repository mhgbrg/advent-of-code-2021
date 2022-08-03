package se.hgbrg.adventofcode2021

val input = readInput(3)

val allZeroes = IntArray(input[0].length).toList()

val gamma = input
    .fold(allZeroes) { acc, binary ->
        acc.mapIndexed { index, i -> i + binary[index].digitToInt() }
    }
    .map { if (it > input.size / 2) 1 else 0 }
    .joinToString(separator = "")
    .toInt(2)

val epsilon = input
    .fold(allZeroes) { acc, binary ->
        acc.mapIndexed { index, i -> i + binary[index].digitToInt() }
    }
    .map { if (it > input.size / 2) 0 else 1 }
    .joinToString(separator = "")
    .toInt(2)

gamma
epsilon
gamma * epsilon

val oxygen = (0 until input[0].length).fold(input) { acc, i ->
    if (acc.size == 1) {
        return@fold acc
    }
    val zeroes = acc.filter { it[i] == '0' }
    val ones = acc.filter { it[i] == '1' }
    if (zeroes.size > ones.size) zeroes else ones
}[0].toInt(2)

val co2 = (0 until input[0].length).fold(input) { acc, i ->
    if (acc.size == 1) {
        return@fold acc
    }
    val zeroes = acc.filter { it[i] == '0' }
    val ones = acc.filter { it[i] == '1' }
    if (zeroes.size <= ones.size) zeroes else ones
}[0].toInt(2)

oxygen
co2
oxygen * co2
