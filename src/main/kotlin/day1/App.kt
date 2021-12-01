package dev.mee42.day1

import dev.mee42.*

fun main1() {
    val input = inputLines(day = 1, year = 2021).map(::int)
    val sol = input.zipWithNext().count { (a, b) -> a < b }
    println(sol)
}
fun main2() {
    val input = inputLines(day = 1, year = 2021).map(::int)
    val sol = input.zip(input.drop(3)).count { (a, b) -> a < b }
    println(sol)
}

fun main() = main2()
