package se.hgbrg.adventofcode2021

object Day03 {
    fun part1(input: List<String>): Int {
        val numBits = input[0].length
        var gamma = ""
        var epsilon = ""
        for (i in 0 until numBits) {
            var zeroes = 0
            var ones = 0
            for (value in input) {
                if (value[i] == '0') {
                    zeroes++
                } else {
                    ones++
                }
            }
            gamma += if (zeroes > ones) "0" else "1"
            epsilon += if (zeroes < ones) "0" else "1"
        }
        return gamma.toInt(2) * epsilon.toInt(2)
    }

    fun part2(input: List<String>): Int {
        val oxygen = part2Aux(input) { zeroes, ones -> if (zeroes.size > ones.size) zeroes else ones }
        val co2 = part2Aux(input) { zeroes, ones -> if (zeroes.size <= ones.size) zeroes else ones }
        return oxygen * co2
    }

    private fun part2Aux(input: List<String>, choose: (zeroes: List<String>, ones: List<String>) -> List<String>): Int {
        val numBits = input[0].length
        var candidates = input
        for (i in 0 until numBits) {
            if (candidates.size == 1) {
                break
            }
            val zeroes = mutableListOf<String>()
            val ones = mutableListOf<String>()
            for (value in candidates) {
                if (value[i] == '0') {
                    zeroes.add(value)
                } else {
                    ones.add(value)
                }
            }
            candidates = choose(zeroes, ones)
        }
        return candidates[0].toInt(2)
    }
}

fun main() {
    val input = readInput(2)
    println(Day03.part1(input))
    println(Day03.part2(input))
}
