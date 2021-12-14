package dev.mee42.day14

import dev.mee42.*

val test = """
NNCB

CH -> B
HH -> N
CB -> H
NH -> C
HB -> C
HC -> B
HN -> C
NN -> C
BH -> H
NC -> B
NB -> B
BN -> B
BB -> N
BC -> B
CC -> N
CN -> C
""".trimIndent()

fun main() {
    val rawInput = if (1 == 1) test else input(day = 14, year = 2021)
    val (start, lookup) = rawInput.trim().split("\n\n").let { (a, b) ->
        a to b.lines().map { s ->
            s.split(" -> ")
        }.associate { (a, b) -> a to b }
    }
    var s = mutableMapOf(start to 1L)
    val charsToRemove = mutableListOf<Char>()
    for (n in 1..40) {
        s = s.map { (entry, count) ->
            entry.zipWithNext().joinToString("", postfix = "${entry.last()}") { (a, b) ->
                "$a${lookup["$a$b"]}"
            } to count
        }.toMap().toMutableMap()
        println("temp: $s")
        val addTo = mutableMapOf<String, Long>()
        val removeThese = mutableSetOf<String>()
        for ((entry, count) in s) {
            // if any part of 'entry' contains 'BB', we split it, add 'BB' to the charsToRemove, and add one to the 'BB' count
            if (entry.contains("BB")) {
                val split = entry.split("BB")
                var zeroSplits = 0
                split.forEachIndexed { i, seg ->
                    val segment = if(i == 0) "${seg}B" else if(i == split.lastIndex) "B$seg" else "B${seg}B"
                    if (segment.length > 1) {
                        addTo[segment] = (addTo[segment] ?: 0) + count
                    } else if(segment.length == 1){
                        zeroSplits++
                    }
                    charsToRemove.addAll(listOf('B', 'B'))
                }
                addTo["BB"] = (addTo["BB"] ?: 0) + (split.size - 1 - zeroSplits)
                removeThese.add(entry)
            }
        }
        for (removedEntry in removeThese) {
            s.remove(removedEntry)
        }
        for ((entry, count) in addTo) {
            s[entry] = (s[entry] ?: 0) + count
        }
        println("After step $n: ${s}")
    }

    val uniqueChars = s.map { it.key }.flatMap { it.map(::id) }.distinct()

    val sorted = uniqueChars.map { char ->
        char to s.map { (key, value) -> if (char in key) value else 0 }.sum()
    }
        .map { (char, count) -> char to (count - charsToRemove.count { it == char }) }
        .toMap()

    println(sorted)
    println("B      got: " + sorted['B'])
    println("B expected: " + 2192039569602L)
}