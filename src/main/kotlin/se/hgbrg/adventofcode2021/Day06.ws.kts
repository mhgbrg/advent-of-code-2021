package se.hgbrg.adventofcode2021

val input = readInput(6)

val init = input[0]
    .split(",")
    .map { it.toInt() }

(0 until 80)
    .fold(init) { acc, _ ->
        acc.flatMap { counter ->
            if (counter == 0) {
                listOf(6, 8)
            } else {
                listOf(counter - 1)
            }
        }
    }
    .count()

val initMap = init
    .groupingBy { it }
    .eachCount()
    .mapValues { it.value.toLong() }

(0 until 256)
    .fold(initMap) { acc, _ ->
        val newAcc = acc.toMutableMap()
        acc.forEach { (counter, number) ->
            newAcc[counter] = newAcc[counter]!! - number
            if (counter == 0) {
                newAcc[6] = newAcc.getOrDefault(6, 0) + number
                newAcc[8] = newAcc.getOrDefault(8, 0) + number
            } else {
                newAcc[counter - 1] = newAcc.getOrDefault(counter - 1, 0) + number
            }
        }
        newAcc
    }
    .map { it.value }
    .sum()

