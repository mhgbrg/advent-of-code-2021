import se.hgbrg.adventofcode2021.Day02
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day02Test {
    private val input = testInput(2)

    @Test
    fun testPart1() {
        assertEquals(Day02.part1(input), 150)
    }

    @Test
    fun testPart2() {
        assertEquals(Day02.part2(input), 900)
    }
}
