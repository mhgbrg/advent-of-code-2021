import se.hgbrg.adventofcode2021.readInput
import se.hgbrg.adventofcode2021.readTestInput
import kotlin.math.ceil
import kotlin.math.floor

val input = readInput(18)

// Part 1
input
    .map { parse(it) }
    .reduce { n1, n2 -> add(n1, n2) }
    .let { magnitude(it) }

// Part 2
input
    .flatMap { line1 ->
        input.flatMap { line2 ->
            if (line1 == line2) {
                listOf()
            } else {
                listOf(Pair(line1, line2), Pair(line2, line1))
            }
        }
    }
    .map { (n1, n2) -> add(parse(n1), parse(n2)) }
    .map { magnitude(it) }
    .maxOrNull()

fun parse(number: String): Number {
    if (number.length == 1) {
        return Literal(number[0].digitToInt())
    }
    var bracketCounter = 0
    for (i in number.indices) {
        val char = number[i]
        if (char == '[') {
            bracketCounter++
        } else if (char == ']') {
            bracketCounter--
        } else if (char == ',' && bracketCounter == 1) {
            val left = number.substring(1 until i)
            val right = number.substring(i+1 until number.length-1)
            val pair = Pair(parse(left), parse(right))
            pair.left.parent = pair
            pair.right.parent = pair
            return pair
        }
    }
    throw RuntimeException()
}

fun pretty(number: Number): String {
    return when (number) {
        is Literal -> number.value.toString()
        is Pair -> "[" + pretty(number.left) + "," + pretty(number.right) + "]"
        else -> throw RuntimeException()
    }
}

fun add(number1: Number, number2: Number): Number {
    return reduce(Pair(number1, number2))
}

fun reduce(number: Number): Number {
    val pairToExplode = shouldExplode(number, 0)
    if (pairToExplode != null) {
        explode(number, pairToExplode)
        return reduce(number)
    }

    val literalToSplit = shouldSplit(number)
    if (literalToSplit != null) {
        split(literalToSplit)
        return reduce(number)
    }

    return number
}

fun shouldExplode(number: Number, level: Int): Pair? {
    return when (number) {
        is Literal -> null
        is Pair -> {
            if (level == 4) {
                return number
            }
            return shouldExplode(number.left, level + 1) ?: shouldExplode(number.right, level + 1)
        }
        else -> throw RuntimeException()
    }
}

fun explode(number: Number, pair: Pair) {
    val literals = traverse(number)
    val leftIndex = literals.indexOf(pair.left)
    if (leftIndex > 0) {
        literals[leftIndex-1].value += (pair.left as Literal).value
    }
    val rightIndex = literals.indexOf(pair.right)
    if (rightIndex < literals.size-1) {
        literals[rightIndex+1].value += (pair.right as Literal).value
    }
    pair.replaceWith(Literal(0))
}

fun shouldSplit(number: Number): Literal? {
    return when (number) {
        is Literal -> if (number.value >= 10) number else null
        is Pair -> shouldSplit(number.left) ?: shouldSplit(number.right)
        else -> throw RuntimeException()
    }
}

fun split(literal: Literal) {
    literal.replaceWith(Pair(
        Literal(floor(literal.value / 2.0).toInt()),
        Literal(ceil(literal.value / 2.0).toInt()),
    ))
}

fun traverse(number: Number): List<Literal> {
    return when (number) {
        is Literal -> listOf(number)
        is Pair -> traverse(number.left) + traverse(number.right)
        else -> throw RuntimeException()
    }
}

fun magnitude(number: Number): Int {
    return when (number) {
        is Literal -> number.value
        is Pair -> 3 * magnitude(number.left) + 2 * magnitude(number.right)
        else -> throw RuntimeException()
    }
}

sealed class Number {
    var parent: Pair? = null

    fun isLeftChild(): Boolean {
        if (parent == null) {
            return false
        }
        return parent!!.left == this
    }

    fun replaceWith(number: Number) {
        if (isLeftChild()) {
            parent!!.left = number
        } else {
            parent!!.right = number
        }
        number.parent = parent
    }
}

class Literal(var value: Int): Number() {
    override fun toString(): String {
        return "Literal(value=$value)"
    }
}

class Pair(var left: Number, var right: Number): Number() {
    init {
        left.parent = this
        right.parent = this
    }

    override fun toString(): String {
        return "Pair(left=$left, right=$right)"
    }
}
