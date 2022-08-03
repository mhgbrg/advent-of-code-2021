package se.hgbrg.adventofcode2021

val input = readInput(13)

val coords = input
    .filter { it != "" && !it.startsWith("fold along") }
    .map { it.split(",").map { it.toInt() } }
    .map { (x, y) -> Pos(x, y) }
    .toSet()

val folds = input
    .filter { it.startsWith("fold along") }
    .map { it.replace("fold along ", "") }
    .map { it.split("=") }
    .map { (axis, num) -> Fold(axis[0], num.toInt()) }

fold(coords, folds[0]).count()

val final = folds.fold(coords) { acc, fold -> fold(acc, fold) }
visualize(final)

data class Fold(val axis: Char, val num: Int)

fun fold(coords: Set<Pos>, fold: Fold): Set<Pos> {
    return coords
        .map { translate(it, fold) }
        .toSet()
}

fun translate(pos: Pos, fold: Fold): Pos {
    when (fold.axis) {
        'x' -> {
            if (pos.x < fold.num) {
                return pos
            }
            val dist = pos.x - fold.num
            return Pos(pos.x - dist * 2, pos.y)
        }
        'y' -> {
            if (pos.y < fold.num) {
                return pos
            }
            val dist = pos.y - fold.num
            return Pos(pos.x, pos.y - dist * 2)
        }
        else -> throw RuntimeException()
    }
}

fun visualize(coords: Set<Pos>) {
    val minX = coords.minOf { it.x }
    val maxX = coords.maxOf { it.x }
    val minY = coords.minOf { it.y }
    val maxY = coords.maxOf { it.y }
    for (y in minY..maxY) {
        for (x in minX..maxX) {
            if (coords.contains(Pos(x, y))) {
                print("#")
            } else {
                print(".")
            }
        }
        println()
    }
}
