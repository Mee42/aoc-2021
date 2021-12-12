package dev.mee42.day12

import dev.mee42.*
import dev.mee42.day9.println

typealias Path = List<String>

fun main() {
    val input = input(day = 12, year = 2021).trim().lines()
    val caves0 = mutableMapOf<String, MutableList<String>>()
    input.map {
        val (name, con) = it.split("-")
       if(caves0[name] == null) {
           caves0[name] = mutableListOf(con)
       } else {
           caves0[name]!!.add(con)
       }
        if(caves0[con] == null) {
            caves0[con] = mutableListOf(name)
        } else {
            caves0[con]!!.add(name)
        }
    }
    val caves: Map<String, List<String>> = caves0

    fun paths(doneTwice: Boolean,
              startAt: String = "start",
              cavesCovered: Set<String> = emptySet(),
              currentPath: List<String> = listOf("start")): Int =
        caves[startAt]!!.sumOf { nextCave -> when {
            nextCave == "end" -> 1
            nextCave == "start" -> 0
            nextCave[0].isLowerCase() && nextCave in cavesCovered && doneTwice -> 0
            else -> paths(
                doneTwice || (nextCave[0].isLowerCase() && nextCave in cavesCovered),
                nextCave,
                cavesCovered + setOf(nextCave),
                currentPath + listOf(nextCave),
            )
        }
    }
    paths(true).println("Part 1")
    paths(false).println("Part 2")
}
