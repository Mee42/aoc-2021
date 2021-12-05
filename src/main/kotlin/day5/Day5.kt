package dev.mee42.day5

import kotlin.math.absoluteValue
import dev.mee42.*


fun main() {
    val partTwo = true

    val input = input(day = 5, year = 2021).trim()

    val max = input.ints().maxOrNull()!!
    val board = (0..max).map { (0..max).map { MutBox(0) } }

    for (line in input.split("\n")) {
        val (one, two) = line.split(" -> ")
        val (x1, y1) = one.split(",").map { it.trim().toInt() }
        val (x2, y2) = two.split(",").map { it.trim().toInt() }
        if (x1 == x2) {
            for (y in y1 rangeTo y2) {
                board[y][x1].incr()
            }
        }
        if (y1 == y2) {
            for (x in x1 rangeTo x2) {
                board[y1][x].incr()
            }
        }
        if (partTwo && ((y1 - y2).absoluteValue == (x1 - x2).absoluteValue)) {
            (x1 rangeTo x2).zip(y1 rangeTo y2).map { (x, y) ->
                board[y][x].incr()
            }
        }
    }
    var sol = 0

    for (a in board) {
        for (b in a) {
            if (b.get() > 1) sol++
        }
    }

    println(sol)
}

infix fun Int.rangeTo(to: Int) = if(this < to) this..to else this downTo to