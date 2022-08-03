package se.hgbrg.adventofcode2021

val input = readInput(8)

val parsedLines = input
    .map { line ->
        val (first, second) = line.split(" | ")
        val patterns = first.split(" ")
        val output = second.split(" ")
        Pair(patterns, output)
    }

// Part 1
parsedLines
    .flatMap { it.second }
    .count { it.length in arrayOf(2, 4, 3, 7) }

// Part 2
parsedLines
    .map { (patterns, output) ->
        val mapping = deduceSegmentMapping(patterns)
        output
            .map { decode(it, mapping) }
            .map { it.toString() }
            .reduce { left, right -> left + right }
            .toInt()
    }
    .sum()

fun deduceSegmentMapping(patterns: List<String>): Map<Char, Int> {
    val candidates = mutableMapOf(
        0 to mutableSetOf('a', 'b', 'c', 'd', 'e', 'f', 'g'),
        1 to mutableSetOf('a', 'b', 'c', 'd', 'e', 'f', 'g'),
        2 to mutableSetOf('a', 'b', 'c', 'd', 'e', 'f', 'g'),
        3 to mutableSetOf('a', 'b', 'c', 'd', 'e', 'f', 'g'),
        4 to mutableSetOf('a', 'b', 'c', 'd', 'e', 'f', 'g'),
        5 to mutableSetOf('a', 'b', 'c', 'd', 'e', 'f', 'g'),
        6 to mutableSetOf('a', 'b', 'c', 'd', 'e', 'f', 'g'),
    )

    for (pattern in patterns) {
        val chars = pattern.toSet().toMutableSet()
        when (pattern.length) {
            2 -> {
                // '1'
                candidates[2] = candidates[2]!!.intersect(chars).toMutableSet()
                candidates[5] = candidates[5]!!.intersect(chars).toMutableSet()
            }
            3 -> {
                // '7'
                candidates[0] = candidates[0]!!.intersect(chars).toMutableSet()
                candidates[2] = candidates[2]!!.intersect(chars).toMutableSet()
                candidates[5] = candidates[5]!!.intersect(chars).toMutableSet()
            }
            4 -> {
                // '4'
                candidates[1] = candidates[1]!!.intersect(chars).toMutableSet()
                candidates[2] = candidates[2]!!.intersect(chars).toMutableSet()
                candidates[3] = candidates[3]!!.intersect(chars).toMutableSet()
                candidates[5] = candidates[5]!!.intersect(chars).toMutableSet()
            }
            5 -> {
                // '2', '3', '5'
                candidates[0] = candidates[0]!!.intersect(chars).toMutableSet()
                candidates[3] = candidates[3]!!.intersect(chars).toMutableSet()
                candidates[6] = candidates[6]!!.intersect(chars).toMutableSet()
            }
            6 -> {
                // '0', '6', '9'
                candidates[0] = candidates[0]!!.intersect(chars).toMutableSet()
                candidates[1] = candidates[1]!!.intersect(chars).toMutableSet()
                candidates[5] = candidates[5]!!.intersect(chars).toMutableSet()
                candidates[6] = candidates[6]!!.intersect(chars).toMutableSet()
            }
            7 -> {
                // '8'
                candidates[0] = candidates[0]!!.intersect(chars).toMutableSet()
                candidates[1] = candidates[1]!!.intersect(chars).toMutableSet()
                candidates[2] = candidates[2]!!.intersect(chars).toMutableSet()
                candidates[3] = candidates[3]!!.intersect(chars).toMutableSet()
                candidates[4] = candidates[4]!!.intersect(chars).toMutableSet()
                candidates[5] = candidates[5]!!.intersect(chars).toMutableSet()
                candidates[6] = candidates[6]!!.intersect(chars).toMutableSet()
            }
        }
    }

    return reduce(candidates)
        .mapValues { (_, segmentCandidates) ->
            if (segmentCandidates.size != 1) {
                throw RuntimeException()
            }
            segmentCandidates.first()
        }
        .entries.associate { (k, v) -> v to k }
}

fun reduce(candidates: MutableMap<Int, MutableSet<Char>>): MutableMap<Int, MutableSet<Char>> {
    var mutated = false
    for ((segment, segmentCandidates) in candidates) {
        if (segmentCandidates.size == 1) {
            val onlyCandidate = segmentCandidates.first()
            for ((innerSegment, innerCandidates) in candidates) {
                // Remove from all other segments
                if (innerSegment != segment && innerCandidates.contains(onlyCandidate)) {
                    innerCandidates.remove(onlyCandidate)
                    mutated = true
                }
            }
        }
    }
    return if (mutated) reduce(candidates) else candidates
}

fun decode(encoded: String, mapping: Map<Char, Int>): Int {
    val segments = encoded.map { mapping[it]!! }
    return toDigit(segments)
}

fun toDigit(segments: List<Int>): Int {
    return when (segments.sorted()) {
        listOf(0, 1, 2, 4, 5, 6) -> 0
        listOf(2, 5) -> 1
        listOf(0, 2, 3, 4, 6) -> 2
        listOf(0, 2, 3, 5, 6) -> 3
        listOf(1, 2, 3, 5) -> 4
        listOf(0, 1, 3, 5, 6) -> 5
        listOf(0, 1, 3, 4, 5, 6) -> 6
        listOf(0, 2, 5) -> 7
        listOf(0, 1, 2, 3, 4, 5, 6) -> 8
        listOf(0, 1, 2, 3, 5, 6) -> 9
        else -> throw RuntimeException()
    }
}
