import se.hgbrg.adventofcode2021.Pos
import se.hgbrg.adventofcode2021.readInput
import se.hgbrg.adventofcode2021.readTestInput

val input = readInput(20)

val algo = input[0]
val img = input.drop(2)

algo
img

// Part 1
(1..2).fold(img) { img, i -> enhance(img, i) }
    .joinToString("\n")
    .count { it == '#' }

// Part 2
(1..50).fold(img) { img, i -> enhance(img, i) }
    .joinToString("\n")
    .count { it == '#' }

fun enhance(img: List<String>, iteration: Int): List<String> {
    val default = if (algo[0] == '.') '.' else if (iteration % 2 == 0) '#' else '.'
    val enhancedImg = mutableListOf<String>()
    for (x in -2..img.size) {
        var row = ""
        for (y in -2..img[0].length) {
            val pixel = outputPixel(img, Pos(x, y), default)
            row += pixel
        }
        enhancedImg.add(row)
    }
    return enhancedImg
}

fun outputPixel(img: List<String>, pos: Pos, default: Char): Char {
    val algoIndex = getAlgoIndex(img, pos, default)
    return algo[algoIndex]
}

fun getAlgoIndex(img: List<String>, pos: Pos, default: Char): Int {
    val str = (
        get(img, pos.northWest(), default).toString()
            + get(img, pos.north(), default)
            + get(img, pos.northEast(), default)
            + get(img, pos.west(), default)
            + get(img, pos, default)
            + get(img, pos.east(), default)
            + get(img, pos.southWest(), default)
            + get(img, pos.south(), default)
            + get(img, pos.southEast(), default)
    )
    return str.replace('.', '0').replace('#', '1').toInt(2)
}

fun get(img: List<String>, pos: Pos, default: Char): Char {
    return img.getOrNull(pos.x)?.getOrNull(pos.y) ?: default
}
