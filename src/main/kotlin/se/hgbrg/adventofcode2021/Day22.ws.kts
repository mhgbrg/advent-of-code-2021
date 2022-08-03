import se.hgbrg.adventofcode2021.readInput
import se.hgbrg.adventofcode2021.readTestInput

val input = readInput(22)

val instructions = input
    .map { parseInstruction(it) }

// Part 1
rebootPointGrid(instructions, partial=true)

// Part 2
rebootRangeGrid(instructions, partial=false)

fun rebootPointGrid(instructions: List<Instruction>, partial: Boolean): Long {
    val grid = PointGrid()
    for (instruction in instructions) {
        if (partial) {
            if (instruction.x.first < -50 || instruction.x.last > 50
                || instruction.y.first < -50 || instruction.y.last > 50
                || instruction.z.first < -50 || instruction.z.last > 50) {
                continue
            }
        }
        val cuboid = Cuboid(instruction.x, instruction.y, instruction.z)
        if (instruction.state) {
            grid.turnOn(cuboid)
        } else {
            grid.turnOff(cuboid)
        }
    }
    return grid.getNumberOn()
}

fun rebootRangeGrid(instructions: List<Instruction>, partial: Boolean): Long {
    val grid = RangeGrid()
    for (instruction in instructions) {
        if (partial) {
            if (instruction.x.first < -50 || instruction.x.last > 50
                || instruction.y.first < -50 || instruction.y.last > 50
                || instruction.z.first < -50 || instruction.z.last > 50) {
                continue
            }
        }
        val cuboid = Cuboid(instruction.x, instruction.y, instruction.z)
        if (instruction.state) {
            grid.turnOn(cuboid)
        } else {
            grid.turnOff(cuboid)
        }
    }
    return grid.getNumberOn()
}

interface Grid {
    fun turnOn(cuboid: Cuboid)
    fun turnOff(cuboid: Cuboid)
    fun getNumberOn(): Long
}

data class Pos3d(val x: Int, val y: Int, val z: Int)

class PointGrid : Grid {
    val cubeStates = mutableMapOf<Pos3d, Boolean>()

    override fun turnOn(cuboid: Cuboid) {
        for (x in cuboid.x) {
            for (y in cuboid.y) {
                for (z in cuboid.z) {
                    cubeStates[Pos3d(x, y, z)] = true
                }
            }
        }
    }

    override fun turnOff(cuboid: Cuboid) {
        for (x in cuboid.x) {
            for (y in cuboid.y) {
                for (z in cuboid.z) {
                    cubeStates[Pos3d(x, y, z)] = false
                }
            }
        }
    }

    override fun getNumberOn(): Long {
        return cubeStates
            .count { it.value }
            .toLong()
    }
}

class RangeGrid : Grid {
    var onCuboids = mutableSetOf<Cuboid>()

    override fun turnOn(cuboid: Cuboid) {
        var remaining = setOf(cuboid)
        for (on in onCuboids) {
            remaining = remaining
                .flatMap { c -> Cuboid.difference(c, on) }
                .toSet()
        }
        onCuboids.addAll(remaining)
    }

    override fun turnOff(cuboid: Cuboid) {
        onCuboids = onCuboids
            .flatMap { on -> Cuboid.difference(on, cuboid) }
            .toMutableSet()
    }

    override fun getNumberOn(): Long {
        return onCuboids.sumOf { on -> on.volume() }
    }
}

data class Cuboid(val x: IntRange, val y: IntRange, val z: IntRange) {
    fun volume(): Long {
        return x.count().toLong() * y.count().toLong() * z.count().toLong()
    }

    companion object Math {
        fun difference(c1: Cuboid, c2: Cuboid): List<Cuboid> {
            val intersection = intersection(c1, c2) ?: return listOf(c1)

            val xRanges = listOf(
                c1.x.first until intersection.x.first,
                intersection.x.first..intersection.x.last,
                (intersection.x.last+1)..c1.x.last,
            )

            val yRanges = listOf(
                c1.y.first until intersection.y.first,
                intersection.y.first..intersection.y.last,
                (intersection.y.last+1)..c1.y.last,
            )

            val zRanges = listOf(
                c1.z.first until intersection.z.first,
                intersection.z.first..intersection.z.last,
                (intersection.z.last+1)..c1.z.last,
            )

            val all = xRanges.flatMap { xRange ->
                yRanges.flatMap { yRange ->
                    zRanges.map { zRange ->
                        Cuboid(xRange, yRange, zRange)
                    }
                }
            }

            return all
                .filter { c -> !c.x.isEmpty() }
                .filter { c -> !c.y.isEmpty() }
                .filter { c -> !c.z.isEmpty() }
                .filter { c -> c != intersection }
        }

        fun intersection(c1: Cuboid, c2: Cuboid): Cuboid? {
            val xRange = if (c2.x.first < c1.x.first && c2.x.last > c1.x.last) {
                c1.x
            } else if (c1.x.contains(c2.x.first) && c1.x.contains(c2.x.last)) {
                c2.x
            } else if (c1.x.contains(c2.x.first)) {
                c2.x.first..c1.x.last
            } else if (c1.x.contains(c2.x.last)) {
                c1.x.first..c2.x.last
            } else {
                return null
            }

            val yRange = if (c2.y.first < c1.y.first && c2.y.last > c1.y.last) {
                c1.y
            } else if (c1.y.contains(c2.y.first) && c1.y.contains(c2.y.last)) {
                c2.y
            } else if (c1.y.contains(c2.y.first)) {
                c2.y.first..c1.y.last
            } else if (c1.y.contains(c2.y.last)) {
                c1.y.first..c2.y.last
            } else {
                return null
            }

            val zRange = if (c2.z.first < c1.z.first && c2.z.last > c1.z.last) {
                c1.z
            } else if (c1.z.contains(c2.z.first) && c1.z.contains(c2.z.last)) {
                c2.z
            } else if (c1.z.contains(c2.z.first)) {
                c2.z.first..c1.z.last
            } else if (c1.z.contains(c2.z.last)) {
                c1.z.first..c2.z.last
            } else {
                return null
            }

            return Cuboid(
                xRange,
                yRange,
                zRange,
            )
        }
    }
}

fun parseInstruction(str: String): Instruction {
    val (onOff, range) = str.split(" ")
    val ranges = parseRange3d(range)
    return Instruction(onOff == "on", ranges.first, ranges.second, ranges.third)
}

fun parseRange3d(str: String): Triple<IntRange, IntRange, IntRange> {
    val (x, y, z) = str.split(",").map { it.drop(2) }
    return Triple(
        parseRange(x),
        parseRange(y),
        parseRange(z),
    )
}

fun parseRange(str: String): IntRange {
    val (start, end) = str.split("..").map { it.toInt() }
    return IntRange(start, end)
}

data class Instruction(val state: Boolean, val x: IntRange, val y: IntRange, val z: IntRange)
