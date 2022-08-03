package se.hgbrg.adventofcode2021

data class Pos(val x: Int, val y: Int) {
    fun north() = Pos(x - 1, y)
    fun northEast() = Pos(x - 1, y + 1)
    fun east() = Pos(x, y + 1)
    fun southEast() = Pos(x + 1, y + 1)
    fun south() = Pos(x + 1, y)
    fun southWest() = Pos(x + 1, y - 1)
    fun west() = Pos(x, y - 1)
    fun northWest() = Pos(x - 1, y - 1)
}
