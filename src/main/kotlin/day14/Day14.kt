package dev.mee42.day14

import dev.mee42.*

fun main(part: Part): Long {
    val rawInput = input(day = 14, year = 2021)
    val (start, lookup) = rawInput.trim().split("\n\n").let { (a, b) ->
        a.map(::id) to b.lines().map { s ->
            s.split(" -> ")
        }.associate { (a, b) -> (a[0] to a[1]) to (b[0]) }
    }
    // NNCB
    // NN NC CB             split into pairs
    // NCN NBC CHB          next step
    // NC CN NB BC CH HB    back to pairs
    // N  C  N  B  C  H +B  take the first value, add B to the end
    // NCNBCHB

    val initial = start.zipWithNext().associateWith { 1L }
    val final = (1..if(part == Part.TWO) 40 else 10).fold(initial) { pairs, _ ->
        val newPairs = mutableMapOf<Pair<Char, Char>, Long>()
        pairs.map { (key, count) ->
            val (a, b) = key
            val middleChar = lookup[key]!!
            newPairs[a to middleChar] = (newPairs[a to middleChar] ?: 0) + count
            newPairs[middleChar to b] = (newPairs[middleChar to b] ?: 0) + count
        }
        newPairs
    }.map { (a, b) -> a.first to b}

    val distinctChars = final.map { it.first }.distinct()

    val result = distinctChars
        .map {
            it to final.filter { (c, _) -> c == it }.sumOf { (_, count) -> count }
        }.map { (char, count) ->
            char to count.apIf(char == start.last(), c_plus(1L))
        }
        .sortedBy { (_, b) -> b }

    return result.last().second - result.first().second
}

fun main() {
    println("Part 1: " + main(Part.ONE))
    println("Part 2: " + main(Part.TWO))
}