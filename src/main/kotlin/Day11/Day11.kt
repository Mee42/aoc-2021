package dev.mee42.Day11

import dev.mee42.*
import dev.mee42.day9.CARDINAL_OFFSETS_INC_DIAGONALS
import dev.mee42.day9.id

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


fun main() {
    val inputRaw = if(true) input(day = 11, year = 2021) else test2
    val input = Array2D.from(inputRaw.trim().split("\n").map { it.map(::id) }.transpose()).map { c -> "$c".toInt() }

    input.print(padding = 0)
    var flashes = 0
    // TODO we need to start with all 9s, then mark them as processed, then repeat until no change is made in an iteration


    fun iteration(): Boolean {
        input.forEachUpdate { i -> i + 1 }
        // one iteration
        val alreadyFlashed = mutableSetOf<Coords2D>()
        while (input.coordsList.any { coords ->
                val surroundingOctopi = CARDINAL_OFFSETS_INC_DIAGONALS.map { it + coords }.filter { input.isInBound(it) }
                if (coords !in alreadyFlashed && input[coords] > 9) {
                    for (surrounding in surroundingOctopi) {
                        input[surrounding]++
                    }
                    input[coords]++ // so we don't mark again? idk dude
                    alreadyFlashed.add(coords)
                    true
                } else false
            })
        input.forEach { value, coords2D ->
            if(value > 9) {
                flashes++
                input[coords2D] = 0
            }
        }
        input.print(0, fixedSpace = 0)
//        input.print(0) { i, c -> if(c in alreadyFlashed) "." else "$i" }
        return input.all { it == input[point(0, 0)] }
    }
    println("\n")
    var n = 0
    while(n++ > -1){
        println("Iteration $n:")
        if(iteration()) break
        println("flashes $flashes")
        println()
    }


}