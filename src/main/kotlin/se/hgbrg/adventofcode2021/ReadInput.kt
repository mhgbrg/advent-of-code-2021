package se.hgbrg.adventofcode2021

import java.io.File

fun readInput(day: Int): List<String> {
    val dayStr = day.toString().padStart(2, '0')
    return File("/Users/mats/code/advent-of-code-2021/src/main/resources/day_$dayStr.txt").readLines()
}

fun readTestInput(day: Int, number: Int = 1): List<String> {
    val dayStr = day.toString().padStart(2, '0')
    return File("/Users/mats/code/advent-of-code-2021/src/test/resources/day_${dayStr}_example_$number.txt").readLines()
}
