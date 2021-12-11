package dev.mee42.day9

import dev.mee42.*
val test = """
2199943210
3987894921
9856789892
8767896789
9899965678
""".trimIndent()

fun <T> T.println(name: String? = null): T {
    if(name == null) println(this)
    else System.out.println("$name: $this")
    return this
}

fun main() {
    val inputRaw = if(1 == 0) test.trim() else input(day = 9, year = 2021)
    val input = Array2D.from(inputRaw.trim().split("\n").map { it.map(::id) })
        .map { c -> "$c".toInt() }

    // the size of a basin is trivial to find
    fun sizeOfBasin(pos: Coords2D, filled: Array2D<Boolean>, captureAbove: Int): Int {
        if(!filled.isInBound(pos)) return 0
        val value = input[pos]
        if(filled[pos] || value < captureAbove || value == 9) return 0
        filled[pos] = true
        return CARDINAL_OFFSETS.sumOf { off ->
            sizeOfBasin(pos + off, filled, value)
        } + 1
    }

    input.map { value, coords ->
        for (off in CARDINAL_OFFSETS) {
            val around = input.getOrNull(coords + off) ?: continue
            if (around <= value) return@map null
        }
        return@map coords
    }
        .toList()
        .filterNotNull()
        .println("Lowest points")
        .also { println("Part 1:" + it.sumOf { pos -> input[pos] }) }
        .map { pos -> sizeOfBasin(pos, input.map { _ -> false }, 0) } // list of basin sizes
        .sortedDescending()
        .take(3)
        .product()
        .println("Solution")
}