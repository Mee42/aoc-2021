package dev.mee42.day6

import dev.mee42.input
import dev.mee42.ints

fun main() {
    val input = input(day = 6, year = 2021).ints()
    var fish = (0..8).associateWith { input.count { a -> it == a }.toLong() }
    for(n in 1..256) {
        fish = fish
            .map { (age, count) -> if(age == 0) (8 to 0L) else (age - 1) to count }
            .toMap()
            .mapValues { (age, count) -> if(age == 6 || age == 8) count + fish[0]!! else count }
        if(n == 80)  println("Part 1: " + fish.values.sum())
        if(n == 256) println("Part 2: " + fish.values.sum())
    }
}