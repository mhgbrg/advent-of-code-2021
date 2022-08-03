package se.hgbrg.adventofcode2021

val input = readInput(11)

var matrix = input
    .map { it
        .split("")
        .filter { it.isNotEmpty() }
        .map { it.toInt() }
        .toMutableList()
    }
    .toMutableList()

val numCells = matrix.size * matrix[0].size

// Part 1
(1..100).map { step() }
    .sum()

// Part 2
matrix = input
    .map { it
        .split("")
        .filter { it.isNotEmpty() }
        .map { it.toInt() }
        .toMutableList()
    }
    .toMutableList()

generateSequence(1) { it + 1 }
    .map { Pair(it, step()) }
    .first { it.second == numCells }

fun step(): Int {
    // Increase all by one
    for (x in matrix.indices) {
        for (y in matrix.indices) {
            matrix[x][y]++
        }
    }

    // Flash
    var flashed: Boolean
    var flashes = 0
    do {
        flashed = false
        for (x in matrix.indices) {
            for (y in matrix.indices) {
                if (matrix[x][y] > 9) {
                    flash(Pos(x, y))
                    flashed = true
                    flashes++
                }
            }
        }
    } while (flashed)

    return flashes
}

fun flash(pos: Pos) {
    set(pos, 0)
    splash(pos.north())
    splash(pos.northEast())
    splash(pos.east())
    splash(pos.southEast())
    splash(pos.south())
    splash(pos.southWest())
    splash(pos.west())
    splash(pos.northWest())
}

fun set(pos: Pos, value: Int) {
    matrix[pos.x][pos.y] = value
}

fun splash(pos: Pos) {
    if (pos.x < 0 || pos.x >= matrix.size || pos.y < 0 || pos.y >= matrix[0].size) {
        return
    }
    val current = matrix[pos.x][pos.y]
    if (current == 0) {
        return
    }
    matrix[pos.x][pos.y] = current + 1
}
