package se.hgbrg.adventofcode2021

val input = readInput(10)

// Part 1
input
    .map { firstIllegal(it) }
    .filterNotNull()
    .map { illegalScore(it) }
    .sum()

// Part 2
val scores = input
    .map { complete(it) }
    .filterNotNull()
    .map { autocompleteScore(it) }

scores.sorted()[scores.size / 2]

fun firstIllegal(line: String): Char? {
    val deque = ArrayDeque<Char>()
    for (char in line) {
        if (isOpen(char)) {
            deque.addLast(char)
        } else if (isClose(char)) {
            val lastOpen = deque.removeLast()
            if (!matches(lastOpen, char)) {
                return char
            }
        } else {
            throw RuntimeException()
        }
    }
    return null
}

fun complete(line: String): String? {
    val deque = ArrayDeque<Char>()
    for (char in line) {
        if (isOpen(char)) {
            deque.addLast(char)
        } else if (isClose(char)) {
            val lastOpen = deque.removeLast()
            if (!matches(lastOpen, char)) {
                return null
            }
        } else {
            throw RuntimeException()
        }
    }
    return deque
        .reversed()
        .map { closing(it) }
        .joinToString("") { it.toString() }
}

fun isOpen(char: Char): Boolean {
    return char in listOf('(', '[', '{', '<')
}

fun isClose(char: Char): Boolean {
    return char in listOf(')', ']', '}', '>')
}

fun closing(open: Char): Char {
    return when (open) {
        '(' -> ')'
        '[' -> ']'
        '{' -> '}'
        '<' -> '>'
        else -> throw RuntimeException()
    }
}

fun matches(open: Char, close: Char): Boolean {
    return closing(open) == close
}

fun illegalScore(char: Char): Long {
    return when (char) {
        ')' -> 3
        ']' -> 57
        '}' -> 1197
        '>' -> 25137
        else -> throw RuntimeException()
    }
}

fun autocompleteScore(string: String): Long {
    return string
        .fold(0L) { total, char ->
            total * 5 + when (char) {
                ')' -> 1
                ']' -> 2
                '}' -> 3
                '>' -> 4
                else -> throw RuntimeException()
            }
        }
}
