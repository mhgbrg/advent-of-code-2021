import se.hgbrg.adventofcode2021.Day04
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day04Test {
    private val input = testInput(4)

    @Test
    fun testPart1() {
        assertEquals(4512, Day04.part1(input))
    }

    @Test
    fun testPart2() {
        assertEquals(1924, Day04.part2(input))
    }
}
