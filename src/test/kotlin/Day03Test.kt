import se.hgbrg.adventofcode2021.Day03
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day03Test {
    private val input = testInput(3)

    @Test
    fun testPart1() {
        assertEquals(198, Day03.part1(input))
    }

    @Test
    fun testPart2() {
        assertEquals(230, Day03.part2(input))
    }
}
