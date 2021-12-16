package dev.mee42.day16

import dev.mee42.*

val test = """
C200B40A82
""".trimIndent()

sealed class Packet(val vNumber: Int) {
    data class LiteralValue(val value: Long, val vNum: Int): Packet(vNum)
    data class Op(val packets: List<Packet>, val vNum: Int, val typeID: Int): Packet(vNum)
}

fun Packet.versionSum(): Int = this.vNumber+ when(this) {
    is Packet.LiteralValue -> 0
    is Packet.Op -> packets.sumOf { it.versionSum() }
}
fun Packet.eval(): Long {
    println("evaling $this")
    val x: Long = when(this) {
        is Packet.LiteralValue -> this.value
        is Packet.Op -> {
            when(this.typeID) {
                0 -> packets.sumOf { it.eval() }
                1 -> packets.fold(1) { a, p -> a * p.eval() }
                2 -> packets.map { it.eval() }.minOrNull()!!
                3 -> packets.map { it.eval() }.maxOrNull()!!
                5 -> if(packets[0].eval() > packets[1].eval()) 1 else 0
                6 -> if(packets[0].eval() < packets[1].eval()) 1 else 0
                7 -> if(packets[0].eval() == packets[1].eval()) 1 else 0
                else -> error("got type id that was wrong $typeID")
            }
        }
    }
    println("<== $x")
    return x
}
fun main() {
    val inputRaw = if(1 == 0) test else input(16, 2021)
    val input = ArrayDeque(inputRaw.trim().flatMap { digit -> digit.digitToInt(radix = 16).toString(radix = 2).padStart(4, '0').map(::id) })
    println(input.joinToString(""))
    val parsed = parsePacket(input)
    println(parsed)
    println(parsed.versionSum())
    println(parsed.eval())
}
fun parsePacket(bits: ArrayDeque<Char>): Packet {
    val packetVersion = bits.removeN(3).joinToString("").toInt(2)
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
            val bitsOfNextPackets = bits.removeN(15).joinToString("").toInt(2)
            val inner = ArrayDeque(bits.removeN(bitsOfNextPackets))
            while(inner.any { it == '1' }) {
                packets.add(parsePacket(inner))
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
fun Iterable<Char>.fromBin() = this.joinToString("").toLong(2)

fun <T> ArrayDeque<T>.removeN(n: Int): List<T> {
    val x = take(n)
    for(i in 0 until n) removeFirst()
    return x
}