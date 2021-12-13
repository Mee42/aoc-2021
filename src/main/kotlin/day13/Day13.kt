package dev.mee42.day13

import dev.mee42.*
val test = """
6,10
0,14
9,10
0,3
10,4
4,11
6,0
6,12
4,1
0,13
10,12
3,4
3,0
8,4
1,10
2,14
8,10
9,0

fold along y=7
fold along x=5
""".trimIndent()

fun main() {
    val inputRaw = if(1 == 0) test else input(day = 13, year = 2021)
    val (inputDots, inputInstructions) = inputRaw.trim().split("\n\n").let { (a, b) ->
        a.lines().map {
            val (x, y) = it.split(",")
            point(x.toInt(), y.toInt())
        }.toSet() to b.lines()
    }



    fun doXFold(dots: Set<Coords2D>, along: Int): Set<Coords2D> {
        return dots.map { (x, y) ->
            if(x > along) point(along - (x - along), y) else point(x, y)
        }.toSet()
    }

    fun doYFold(dots: Set<Coords2D>, along: Int): Set<Coords2D> {
        return dots.map { (x, y) ->
            if(y > along) point(x, along - (y - along)) else point(x, y)
        }.toSet()
    }
    var dots = inputDots
    var part1done = false
    for(line in inputInstructions) {
        val along = line.ints()[0]
        dots = if(line.contains("y")) doYFold(dots, along) else doXFold(dots, along)
        if(!part1done) {
            println("Part 1: " + dots.size)
            part1done = true
        }
    }
    println("Part 2:")
    Array2D.init(dots.maxOf { it.x + 1 }, dots.maxOf { it.y + 1 }) {}.print { _, coords2D -> if(coords2D in dots) "#" else " " }
}
