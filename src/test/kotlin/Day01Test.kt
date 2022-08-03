import se.hgbrg.adventofcode2021.Day01
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day01Test {
    private val input = testInput(1)

    @Test
    fun testPart1() {
        assertEquals(Day01.part1(input), 7)
    }

    @Test
    fun testPart2() {
        assertEquals(Day01.part2(input), 5)
    }
}
