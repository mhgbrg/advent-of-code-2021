package se.hgbrg.adventofcode2021

val input = readInput(9)

val matrix = input
    .map {
        it.split("")
            .filter { it.isNotEmpty() }
            .map { it.toInt() }
    }

val lows = matrix.indices
    .flatMap { x -> matrix[x].indices.map { y -> Pos(x, y) } }
    .filter { pos ->
        val height = get(pos)
        height < get(pos.north())
                && height < get(pos.south())
                && height < get(pos.west())
                && height < get(pos.east())
    }

// Part 1
lows.sumOf { pos -> get(pos) + 1 }

// Part 2
lows
    .map { explore(it) }
    .map { it.size }
    .sortedDescending()
    .take(3)
    .reduce { l, r -> l * r }

fun explore(pos: Pos): Set<Pos> {
    val height = get(pos)
    if (height == 9) {
        return setOf()
    }
    val basin = mutableSetOf(pos)
    if (get(pos.north()) > height) {
        basin += explore(pos.north())
    }
    if (get(pos.south()) > height) {
        basin += explore(pos.south())
    }
    if (get(pos.west()) > height) {
        basin += explore(pos.west())
    }
    if (get(pos.east()) > height) {
        basin += explore(pos.east())
    }
    return basin
}

fun get(pos: Pos): Int {
    return matrix.getOrNull(pos.x)?.getOrNull(pos.y) ?: 9
}
