import se.hgbrg.adventofcode2021.readInput
import se.hgbrg.adventofcode2021.readTestInput
import kotlin.math.abs

val input = readInput(19)
val parsed = parseInput(input)
val result = buildTranslatedMap(parsed)

// Part 1
result.first
    .flatMap { (_, v) -> v }
    .toSet()
    .size

// Part 2
result.second.values.flatMap { s1 ->
    result.second.values.map { s2 ->
        distance(s1, s2)
    }
}.maxOrNull()

fun distance(coord1: Coord, coord2: Coord): Int {
    return abs(coord1.x - coord2.x) + abs(coord1.y - coord2.y) + abs(coord1.z - coord2.z)
}

fun buildTranslatedMap(scanners: Map<Int, List<Coord>>): Pair<Map<Int, List<Coord>>, Map<Int, Coord>> {
    val orientations: List<(coord: Coord) -> Coord> = allOrientations()
    val translatedMap = mutableMapOf(0 to scanners[0]!!)
    val scannerPositions = mutableMapOf(0 to Coord(0, 0, 0))
    outer@ while (translatedMap.size < scanners.size) {
        for ((scanner2, beacons2) in scanners) {
            if (translatedMap.containsKey(scanner2)) {
                continue
            }
            for ((_, beacons1) in translatedMap) {
                for (origo1 in beacons1) {
                    val relativeBeacons1 = beacons1.map { relativeTo(origo1, it) }
                    for (orientation in orientations) {
                        val orientedBeacons2 = beacons2.map { orientation(it) }
                        for (origo2 in orientedBeacons2) {
                            val relativeBeacons2 = orientedBeacons2.map { relativeTo(origo2, it) }
                            val matches = relativeBeacons1.filter { relativeBeacons2.contains(it) }
                            if (matches.size >= 12) {
                                translatedMap[scanner2] = relativeBeacons2.map {
                                        (x, y, z) -> Coord(x + origo1.x, y + origo1.y, z + origo1.z)
                                }
                                val scanner2Pos = Coord(
                                    origo1.x - origo2.x,
                                    origo1.y - origo2.y,
                                    origo1.z - origo2.z,
                                )
                                scannerPositions[scanner2] = scanner2Pos
                                continue@outer
                            }
                        }
                    }
                }
            }
        }
    }
    return Pair(translatedMap, scannerPositions)
}

fun relativeTo(origo: Coord, coord: Coord): Coord {
    return Coord(
        coord.x - origo.x,
        coord.y - origo.y,
        coord.z - origo.z,
    )
}

fun allOrientations(): List<(coord: Coord) -> Coord> {
    return listOf(
        // facing x
        { (x, y, z) ->  Coord(x, y, z) },
        // 90 deg
        { (x, y, z) ->  Coord(x, z, -y) },
        // 180 deg
        { (x, y, z) ->  Coord(x, -y, -z) },
        // 270 deg
        { (x, y, z) ->  Coord(x, -z, y) },
        // facing -x
        { (x, y, z) ->  Coord(-x, y, -z) },
        // 90 deg
        { (x, y, z) ->  Coord(-x, -z, -y) },
        // 180 deg
        { (x, y, z) ->  Coord(-x, -y, z) },
        // 270 deg
        { (x, y, z) ->  Coord(-x, z, y) },
        // facing y
        { (x, y, z) ->  Coord(y, -x, z) },
        // 90 deg
        { (x, y, z) ->  Coord(y, z, x) },
        // 180 deg
        { (x, y, z) ->  Coord(y, x, -z) },
        // 270 deg
        { (x, y, z) ->  Coord(y, -z, -x) },
        // facing -y
        { (x, y, z) ->  Coord(-y, -x, -z) },
        // 90 deg
        { (x, y, z) ->  Coord(-y, -z, x) },
        // 180 deg
        { (x, y, z) ->  Coord(-y, x, z) },
        // 270 deg
        { (x, y, z) ->  Coord(-y, z, -x) },
        // facing z
        { (x, y, z) ->  Coord(z, y, -x) },
        // 90 deg
        { (x, y, z) ->  Coord(z, -x, -y) },
        // 180 deg
        { (x, y, z) ->  Coord(z, -y, x) },
        // 270 deg
        { (x, y, z) ->  Coord(z, x, y) },
        // facing -z
        { (x, y, z) ->  Coord(-z, y, x) },
        // 90 deg
        { (x, y, z) ->  Coord(-z, x, -y) },
        // 180 deg
        { (x, y, z) ->  Coord(-z, -y, -x) },
        // 270 deg
        { (x, y, z) ->  Coord(-z, -x, y) },
    )
}

fun parseInput(input: List<String>): Map<Int, List<Coord>> {
    val scanners = mutableMapOf<Int, List<Coord>>()
    var currentScanner = -1
    for (line in input) {
        if (line.startsWith("---")) {
            currentScanner = line.replace("---", "").replace(" ", "").replace("scanner", "").toInt()
        } else if (line.isNotEmpty()) {
            val (x, y, z) = line.split(",").map { it.toInt() }
            scanners.merge(currentScanner, listOf(Coord(x, y, z))) { l1, l2 -> l1 + l2 }
        }
    }
    return scanners
}

data class Coord(val x: Int, val y: Int, val z: Int)
