package dev.mee42.day16

import dev.mee42.*

sealed class Packet(val vNumber: Int) {
    data class LiteralValue(val value: Long, val vNum: Int): Packet(vNum) {
        override fun toString() = "$value"
    }
    data class Op(val packets: List<Packet>, val vNum: Int, val typeID: Int): Packet(vNum) {
        override fun toString() = when(this.typeID) {
            0, 1, 2, 3 -> "" + mapOf(0 to "+", 1 to "*", 2 to "min", 3 to "max")[typeID] + "(" + packets.joinToString(", ") + ")"
            5, 6, 7 -> "(" + packets[0] + " " + mapOf(5 to ">", 6 to "<", 7 to "=")[typeID] + " " + packets[1] + ")"
            else -> e()
        }
    }
}

fun Packet.versionSum(): Int = this.vNumber + when(this) {
    is Packet.LiteralValue -> 0
    is Packet.Op -> packets.sumOf { it.versionSum() }
}

fun Packet.evalT(): Long = when (this) {
    is Packet.LiteralValue -> value
    is Packet.Op -> when (typeID) {
        0 -> packets.sumOf { it.eval() }
        1 -> packets.fold(1) { a, p -> a * p.eval() }
        2 -> packets.minOf { it.eval() }
        3 -> packets.maxOf { it.eval() }
        5 -> if (packets[0].eval() > packets[1].eval()) 1 else 0
        6 -> if (packets[0].eval() < packets[1].eval()) 1 else 0
        7 -> if (packets[0].eval() == packets[1].eval()) 1 else 0
        else -> error("got type id that was wrong $typeID")
    }
}

private var depth = 0
fun Packet.eval(): Long {
    println(" ".repeat(depth) + "==> $this")
    depth++
    val res = evalT()
    depth--
    println(" ".repeat(depth) + "<== $res")
    return res
}

fun main() {
    val input = input(16, 2021).trim().flatMap { digit ->
        digit.digitToInt(16).toString(2).padStart(4, '0').map(::id)
    }
    val parsed = parsePacket(ArrayDeque(input))
    val part2 = parsed.eval()
    println("Part 1: " + parsed.versionSum())
    println("Part 2: $part2")
}

fun parsePacket(bits: ArrayDeque<Char>): Packet {
    val packetVersion = bits.removeN(3).fromBin().toInt()
    val packetTypeID = bits.removeN(3).fromBin().toInt()
    if(packetTypeID == 4) {
        val literalValue = mutableListOf<Char>()
        while(true) {
            val nextFive = bits.removeN(5)
            literalValue.addAll(nextFive.drop(1))
            if(nextFive.first() == '0') break
        }
        return Packet.LiteralValue(literalValue.fromBin(), packetVersion)
    } else {
        // it is an operator packet
        val lengthTypeID = bits.removeFirst() == '0'
        val packets = mutableListOf<Packet>()
        if(lengthTypeID) {
            val bitsOfNextPackets = bits.removeN(15).fromBin()
            val startingSize = bits.size
            while(bits.size > startingSize - bitsOfNextPackets) {
                packets.add(parsePacket(bits))
            }
        } else {
            val numberOfNextPackets = bits.removeN(11).fromBin()
            for(n in 0 until numberOfNextPackets) {
                packets.add(parsePacket(bits))
            }
        }
        return Packet.Op(packets, packetVersion, packetTypeID)
    }
}
