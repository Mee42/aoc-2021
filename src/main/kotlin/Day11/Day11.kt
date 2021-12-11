package dev.mee42.Day11

import dev.mee42.*
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

val test = """
11111
19991
19191
19991
11111
""".trimIndent()

val test2 = """
5483143223
2745854711
5264556173
6141336146
6357385478
4167524645
2176841721
6882881134
4846848554
5283751526
""".trimIndent()


@OptIn(ExperimentalTime::class)
fun main() {
    val inputRaw = if(true) input(day = 11, year = 2021) else test2
    val input = Array2D.from(inputRaw.trim().split("\n").map { it.map(::id) }.transpose()).map { c -> "$c".toInt() }

    var flashes = 0

    val alreadyFlashed = mutableSetOf<Coords2D>()

    fun iteration(): Boolean {
        alreadyFlashed.clear()
        input.forEachUpdate { i -> i + 1 }
        loopWhile { input.coordsList.map { coords ->
                val surroundingOctopi = CARDINAL_OFFSETS_INCL_DIAGONALS_AND_SELF.map { it + coords }.filter { input.isInBound(it) }
                if (coords !in alreadyFlashed && input[coords] > 9) {
                    for (surrounding in surroundingOctopi) {
                        input[surrounding]++
                    }
                    alreadyFlashed.add(coords)
                    true
                } else false
        }.any { it } }
        flashes += input.count { it > 9 }
        input.forEachUpdate { it -> if(it > 9) 0 else it }
        return input.all { it == input[point(0, 0)] }
    }
    val d = measureTime {
        for (n in 1..400) {
            if(n == 100) println("Part 1: $flashes")
            if (iteration()) {
                println("Part 2: $n")
                break
            }
        }
    }
    println("completed in " + d.inWholeMilliseconds + "ms")


}