package se.hgbrg.adventofcode2021

import java.util.*

val input = readInput(15)

val map = input.map { it.toList().map { it.digitToInt() } }

// Part 1
shortestPath(Pos(0, 0), Pos(map.size - 1, map[0].size - 1))

// Part 2
shortestPath(Pos(0, 0), Pos(map.size * 5 - 1, map[0].size * 5 - 1))

fun shortestPath(start: Pos, end: Pos): Int {
    val minRisks = mutableMapOf<Pos, Int>()

    val queue = PriorityQueue<Pair<Pos, Int>> { (_, risk1), (_, risk2) ->
        risk1.compareTo(risk2)
    }

    queue.add(Pair(start, 0))

    while (queue.isNotEmpty()) {
        val (last, risk) = queue.remove()
        if (last == end) {
            return risk
        }
        if (risk < minRisks.getOrDefault(last, Int.MAX_VALUE)) {
            minRisks[last] = risk
            neighbours(last).forEach { neighbour ->
                queue.add(Pair(neighbour, risk + get(neighbour)))
            }
        }
    }

    // Ran out of nodes before reaching end, which means that we didn't find any path
    throw RuntimeException()
}

fun neighbours(pos: Pos): List<Pos> {
    return listOf(pos.north(), pos.south(), pos.west(), pos.east())
        .filter { inBounds(it) }
}

fun inBounds(pos: Pos): Boolean {
    return pos.x >= 0
            && pos.y >= 0
            && pos.x < map.size * 5
            && pos.y < map[0].size * 5
}

fun get(pos: Pos): Int {
    if (!inBounds(pos)) {
        throw RuntimeException()
    }
    if (pos.x < map.size && pos.y < map[0].size) {
        return map[pos.x][pos.y]
    }
    val corresponding = when {
        pos.x < map.size -> get(Pos(pos.x, pos.y - map[0].size))
        else -> get(Pos(pos.x - map.size, pos.y))
    }
    return corresponding % 9 + 1
}
