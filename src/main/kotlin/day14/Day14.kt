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
    val rawInput = if(1 == 1) test else input(day = 14, year = 2021)
    val (start, lookup) = rawInput.trim().split("\n\n").let { (a, b) ->
        a to b.lines().map { s ->
            s.split(" -> ")
        }.associate { (a, b) -> a to b }
    }
    var s = mutableMapOf(start to 1)
    var charsToRemove = mutableListOf<Char>()
    for(n in 1..40) {
        s = s.map { ( entry, count) -> entry.zipWithNext().joinToString("", postfix = "${entry.last()}") { (a, b) ->
            "$a${lookup["$a$b"]}"
        } to count }.toMap().toMutableMap()


        val addTo = mutableMapOf<String, Int>()
        val removeThese = mutableSetOf<String>()
        for((entry, count) in s){
            // if any part of 'entry' contains 'BB', we split it, add 'BB' to the charsToRemove, and add one to the 'BB' count
            if(entry.contains("BB")) {
                entry.split("BB").forEach { segment ->
                    addTo[segment] = (addTo[segment] ?: 0) + count
                    addTo["BB"] = (addTo["BB"] ?: 0) + count
                    charsToRemove.addAll(listOf('b', 'b'))
                }
                removeThese.add(entry)
            }
        }
        for(removedEntry in removeThese) {
            s.remove(removedEntry)
        }
        for((entry, count) in addTo) {
            s[entry] = (s[entry] ?: 0) + count
        }
        println("After step $n: ${s}")
    }
    val uniqueChars = s.map(::id).distinct().sortedBy { s.count { a -> a == it } }
    println(uniqueChars)
    println()
    println(s.count { it == uniqueChars.last() } - s.count { it == uniqueChars.first() })

}