package se.hgbrg.adventofcode2021

import se.hgbrg.adventofcode2021.*

val input = readInput(16)

// TODO: Use a custom data structure with a list and a pointer instead of an iterator
// TODO: Only use longs for values that need longs

val packets = input
    .map { parsePacket(toBinary(it).iterator()) }

// Part 1
packets.map { versionSum(it.first) }

// Part 2
packets.map { evaluate(it.first) }

fun versionSum(packet: Packet): Long {
    return packet.version + packet.subPackets.sumOf { versionSum(it) }
}

fun evaluate(packet: Packet): Long {
    return when (packet.typeId) {
        0L -> packet.subPackets.sumOf { evaluate(it) }
        1L -> packet.subPackets.fold(1L) { acc, subPacket -> acc * evaluate(subPacket) }
        2L -> packet.subPackets.minOf { evaluate(it) }
        3L -> packet.subPackets.maxOf { evaluate(it) }
        4L -> packet.literal!!
        5L -> {
            val (first, second) = packet.subPackets
            if (evaluate(first) > evaluate(second)) 1 else 0
        }
        6L -> {
            val (first, second) = packet.subPackets
            if (evaluate(first) < evaluate(second)) 1 else 0
        }
        7L -> {
            val (first, second) = packet.subPackets
            if (evaluate(first) == evaluate(second)) 1 else 0
        }
        else -> throw RuntimeException()
    }
}

fun toBinary(hex: String): String {
    return hex
        .map { it.digitToInt(16).toString(2).padStart(4, '0') }
        .joinToString("")
}

fun parsePacket(binary: Iterator<Char>): Pair<Packet, Long> {
    var numBitsRead = 0L
    val version = takeLong(binary, 3)
    numBitsRead += 3
    val typeId = takeLong(binary, 3)
    numBitsRead += 3
    val (literal, literalNumBits) = if (typeId == 4L) parseLiteral(binary) else Pair(null, 0L)
    numBitsRead += literalNumBits
    val (subPackets, subPacketsNumBits) = if (typeId != 4L) parseSubPackets(binary) else Pair(listOf(), 0L)
    numBitsRead += subPacketsNumBits
    val packet = Packet(
        version = version,
        typeId = typeId,
        literal = literal,
        subPackets = subPackets,
    )
    return Pair(packet, numBitsRead)
}

fun parseLiteral(binary: Iterator<Char>): Pair<Long, Long> {
    var numBitsRead = 0L
    val literal = mutableListOf<Char>()
    while (true) {
        val leading = binary.next()
        literal += take(binary, 4)
        numBitsRead += 5
        if (leading == '0') {
            return Pair(toLong(literal), numBitsRead)
        }
    }
}

fun parseSubPackets(binary: Iterator<Char>): Pair<List<Packet>, Long> {
    val lengthTypeId = binary.next()
    return if (lengthTypeId == '0') {
        // next 15 bits = total length
        val numBits = takeLong(binary, 15)
        val (subPackets, subPacketsNumBits) = parseSubPackets0(binary, numBits)
        Pair(subPackets, subPacketsNumBits + 15 + 1)
    } else {
        // next 11 bits = number of packets
        val numSubPackets = takeLong(binary, 11)
        val (subPackets, subPacketsNumBits) = parseSubPackets1(binary, numSubPackets)
        return Pair(subPackets, subPacketsNumBits + 11 + 1)
    }
}

fun parseSubPackets0(binary: Iterator<Char>, expectedNumBits: Long): Pair<List<Packet>, Long> {
    val packets = mutableListOf<Packet>()
    var numBitsRead = 0L
    while (numBitsRead != expectedNumBits) {
        val (packet, numBits) = parsePacket(binary)
        packets.add(packet)
        numBitsRead += numBits
    }
    return Pair(packets, numBitsRead)
}

fun parseSubPackets1(binary: Iterator<Char>, expectedNumSubPackets: Long): Pair<List<Packet>, Long> {
    val packets = mutableListOf<Packet>()
    var numBitsRead = 0L
    while (packets.size.toLong() != expectedNumSubPackets) {
        val (packet, numBits) = parsePacket(binary)
        packets.add(packet)
        numBitsRead += numBits
    }
    return Pair(packets, numBitsRead)
}

fun toLong(binary: List<Char>): Long {
    return binary.joinToString("").toLong(2)
}

fun takeLong(binary: Iterator<Char>, length: Long): Long {
    return toLong(take(binary, length))
}

fun <T> take(iterator: Iterator<T>, n: Long): List<T> {
    return (1..n).map { iterator.next() }
}

data class Packet(
    val version: Long,
    val typeId: Long,
    val literal: Long?,
    val subPackets: List<Packet>,
)
