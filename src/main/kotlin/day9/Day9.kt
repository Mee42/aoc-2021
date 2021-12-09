package dev.mee42.day9

import dev.mee42.*
val test = """
2199943210
3987894921
9856789892
8767896789
9899965678
""".trimIndent()

fun main() {
    val input = if(1 == 0) test.trim() else input(day = 9, year = 2021)
    val input2 = input.trim().split("\n").map { it.map { x -> "$x".toInt() } }
    input2.map { println(">$it") }
    val smallestPoints = input2.flatMapIndexed { row, list ->
        list.mapIndexed foo@{ col, value ->
            val surrounding = listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)
            for ((rOff, cOff) in surrounding) {
                val around = input2.getOrNull(row + rOff)?.getOrNull(col + cOff) ?: continue
                if (around <= value) return@foo null
            }
            row to col
        }
    }.filterNotNull()
    // the size of a basin is trivial to find
    fun sizeOfBasin(row: Int, col: Int, filled: List<List<MutBox<Boolean>>>, captureAbove: Int): Int {
        if(row !in filled.indices || col !in filled[0].indices) return 0
        if(filled[row][col].get()) return 0 // already counted square
        if(input2[row][col] < captureAbove) {
            return 0
        }
        if(input2[row][col] == 9) return 0
        filled[row][col].set(true) // filled already
        val surrounding = listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)
        val value = input2[row][col]
        return surrounding.sumOf { (r, c) ->
            sizeOfBasin(row + r, col + c, filled, value)
        } + 1
    }

    val (a, b, c) = smallestPoints.map { (r, c) ->
        val filled = input2.map { it.map { MutBox(false) } }
        val x = (r to c) to sizeOfBasin(r, c, filled, 0)
        println("$r, $c ->")
        println()
        x.second
    }.sortedDescending().take(3)

    println(a * b * c)

}