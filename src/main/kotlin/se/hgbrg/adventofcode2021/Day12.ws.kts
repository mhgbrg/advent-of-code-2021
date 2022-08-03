package se.hgbrg.adventofcode2021

val input = readInput(12)

val edges = input
    .map { line ->
        val (start, end) = line.split("-")
        start to end
    }

val graph = buildGraph()

fun buildGraph(): Map<String, Set<String>> {
    val mutableGraph: MutableMap<String, Set<String>> = mutableMapOf()
    for ((node1, node2) in edges) {
        mutableGraph.merge(node1, setOf(node2)) { a, b -> a + b }
        mutableGraph.merge(node2, setOf(node1)) { a, b -> a + b }
    }
    return mutableGraph
}

// Part 1
getPathsToEnd("start", setOf("start")).count()

fun getPathsToEnd(node: String, visited: Set<String>): Set<List<String>> {
    if (node == "end") {
        return setOf(listOf(node))
    }
    val neighbours = graph[node]!!
    return neighbours
        .flatMap { neighbour ->
            if (isLarge(neighbour) || !visited.contains(neighbour)) {
                getPathsToEnd(neighbour, visited + neighbour)
            } else {
                setOf()
            }
        }
        .map { listOf(node) + it }
        .toSet()
}

// Part 2
val smallNodes = graph.values
    .flatten()
    .toSet()
    .filter(::isSmall)
smallNodes
    .flatMap { node ->
        // Allow small caves to be visited twice, but pretend that we've already visited all of
        // them, except `node`, once.
        val visited = smallNodes
            .filter { it != node }
            .associateWith { 1 }
        getPathsToEndWithRepeatVisit("start", visited)
    }
    .toSet()
    .count()

fun getPathsToEndWithRepeatVisit(node: String, visited: Map<String, Int>): Set<List<String>> {
    val neighbours = graph[node]!!
    return neighbours
        .flatMap { neighbour ->
            if (isLarge(neighbour)) {
                getPathsToEndWithRepeatVisit(neighbour, visited)
            } else if (isSmall(neighbour)) {
                val visitedNum = visited.getOrDefault(neighbour, 0)
                if (visitedNum < 2) {
                    getPathsToEndWithRepeatVisit(neighbour, visited + mapOf(neighbour to visitedNum + 1))
                } else {
                    setOf()
                }
            } else if (neighbour == "end") {
                setOf(listOf("end"))
            } else {
                setOf()
            }
        }
        .map { listOf(node) + it }
        .toSet()
}

fun isLarge(node: String): Boolean {
    return node.all { it.isUpperCase() }
}

fun isSmall(node: String): Boolean {
    return node != "start" && node != "end" && node.all { it.isLowerCase() }
}
