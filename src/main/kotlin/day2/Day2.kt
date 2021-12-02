package dev.mee42.day2
import dev.mee42.*;

val testCase = """
    forward 5
down 5
forward 8
up 3
down 8
forward 2
""".trimIndent()


fun main2() {
    val inputRaw =
//        input(day = 2, year = 2021)
        inputLines(day = 2, year = 2021)
//        testCase
//        testCase.trim().split("\n").map { it.trim() }

    var hor = 0
    var aim = 0
    var depth = 0
    for(line in inputRaw) {
        val (dir, mag) = line.split(" ")
        when(dir) {
            "up" -> aim -= mag.toInt()
            "down" -> aim += mag.toInt()
            "forward" -> {
                hor += mag.toInt()
                depth -= aim * mag.toInt()
            }
        }
    }

    var sol = hor * depth
    println(sol)
}

