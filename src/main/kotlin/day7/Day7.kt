package dev.mee42.day7

import dev.mee42.*
import kotlin.math.absoluteValue


fun main(part: Part) {
    val input = input(day = 7, year = 2021).ints()
    val sol = (0..input.max()).map { n ->
        // find sum needed to
        // move each crab to position n
        input.sumOf { crab ->
            (crab - n)
                .absoluteValue
                .apIf(part == Part.TWO) { it.inc() * it / 2 }
        }
    }.min()
    println(sol)
}
fun main() {
    main(Part.ONE)
    main(Part.TWO)
}