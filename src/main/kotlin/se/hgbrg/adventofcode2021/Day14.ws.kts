package se.hgbrg.adventofcode2021

val input = readInput(14)

val template = input[0]

val rules = input.drop(2)
    .map { it.split(" -> ") }
    .associate { (from, to) -> Pair(from[0], from[1]) to to[0] }

template
rules

// Part 1
part1(10)

fun part1(steps: Int): Int {
    val finalPolymer = (1..steps).fold(template) { polymer, _ -> expand(polymer) }
    val counts = finalPolymer
        .groupingBy { it }
        .eachCount()
    println(counts.toSortedMap())
    return counts.maxOf { it.value } - counts.minOf { it.value }
}

fun expand(polymer: String): String {
    val builder = StringBuilder()
    for (i in 0 until polymer.length - 1) {
        val first = polymer[i]
        val second = polymer[i+1]
        builder.append(first)
        builder.append(rules[Pair(first, second)])
    }
    builder.append(polymer.last())
    return builder.toString()
}

// Part 2
part2(40)

fun part2(steps: Int): Long {
    val expander = Expander(rules)
    val counts = template.zipWithNext()
        .map { expander.expand(it, steps) }
        .fold(mapOf(), ::mergeCounts)
        .toMutableMap()
    for (char in template) {
        counts[char] = counts.getOrDefault(char, 0) + 1
    }
    println(counts.toSortedMap())
    return counts.maxOf { it.value } - counts.minOf { it.value }
}

class Expander(private val rules: Map<Pair<Char, Char>, Char>) {
    private val cache: HashMap<Pair<Pair<Char, Char>, Int>, Map<Char, Long>> = hashMapOf()

    fun expand(pair: Pair<Char, Char>, steps: Int): Map<Char, Long> {
        if (cache.containsKey(Pair(pair, steps))) {
            return cache[Pair(pair, steps)]!!
        }
        if (steps == 0) {
            return mapOf()
        }
        val middle = rules[pair]!!
        val left = Pair(pair.first, middle)
        val right = Pair(middle, pair.second)
        val leftCounts = expand(left, steps - 1)
        val rightCounts = expand(right, steps - 1)
        val allCounts = mergeCounts(leftCounts, rightCounts).toMutableMap()
        allCounts[middle] = allCounts.getOrDefault(middle, 0) + 1
        cache[Pair(pair, steps)] = allCounts
        return allCounts
    }

    private fun mergeCounts(first: Map<Char, Long>, second: Map<Char, Long>): Map<Char, Long> {
        return (first.asIterable() + second.asIterable())
            .groupBy({ it.key }, { it.value })
            .mapValues { (_, value) -> value.sum() }
    }
}

fun mergeCounts(first: Map<Char, Long>, second: Map<Char, Long>): Map<Char, Long> {
    return (first.asIterable() + second.asIterable())
        .groupBy({ it.key }, { it.value })
        .mapValues { (_, value) -> value.sum() }
}
