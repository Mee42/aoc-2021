package dev.mee42.day10

import dev.mee42.*
val test = """
[({(<(())[]>[[{[]{<()<>>
[(()[<>])]({[<{<<[]>>(
{([(<{}[<>[]}>{[]{[(<()>
(((({<>}<{<{<>}{[]{[]{}
[[<[([]))<([[{}[[()]]]
[{[{({}]{}}([{[{{{}}([]
{<[[]]>}<{[{[{[]{()[[[]
[<(<(<(<{}))><([]([]()
<{([([[(<>()){}]>(<<{{
<{([{{}}[<[[[<>{}]]]>[]]
""".trimIndent()

fun main() {
    val lines = input(day = 10, year = 2021).trim().split("\n").map { it.trim() }
    val done = lines.map { line ->
        val stack = ArrayDeque<Char>(line.length) // expected chars
        for(elem in line) {
            if(elem in "(<[{") {
                stack.addLast(")>]}"["(<[{".indexOf(elem)])
            } else { // end char
                val got = stack.removeLastOrNull()
                if(got == null) println("reached end of stack while parsing")
                else if(got != elem) return@map null // corrupted line we will ignore
            }
        }
        return@map stack.reversed().joinToString("")
    }.filterNotNull()
        .map { rest ->
            rest.fold(0L) { acc, c -> acc * 5 + when(c) {
                ')' -> 1
                ']' -> 2
                '}' -> 3
                '>' -> 4
                else -> error("illegal char $c")
            } }
        }.sorted()
    println(done[done.size / 2])
}