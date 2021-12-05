package dev.mee42.day5

import kotlin.math.absoluteValue
import dev.mee42.*
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

val test = """
0,9 -> 5,9
8,0 -> 0,8
9,4 -> 3,4
2,2 -> 2,1
7,0 -> 7,4
6,4 -> 2,0
0,9 -> 2,9
3,4 -> 1,4
0,0 -> 8,8
5,5 -> 8,2
""".trimIndent()

fun main() {
    val partTwo = true

    val input =
        input(day = 5, year = 2021)
//        test
            .trim()

    val max = input.ints().maxOrNull()!! + 1
    val board = Array2D.initSquare(max) { 0 }

    for (line in input.split("\n")) {
        val (one, two) = line.split(" -> ")
        val (x1, y1) = one.split(",").map { it.trim().toInt() }
        val (x2, y2) = two.split(",").map { it.trim().toInt() }
        if (x1 == x2) {
            for (y in y1 rangeTo y2) {
                board[point(x1, y)]++
            }
        }
        if (y1 == y2) {
            for (x in x1 rangeTo x2) {
                board[point(x, y1)]++
            }
        }
        if (partTwo && ((y1 - y2).absoluteValue == (x1 - x2).absoluteValue)) {
            (x1 rangeTo x2).zip(y1 rangeTo y2).map { (x, y) ->
                board[point(x, y)]++
            }
        }
    }
//    board.print(fixedSpace = 0) { if(it == 0) "." else it.toString(radix = 16) }
    val image = BufferedImage(max, max, 1)
    board.forEach { value, coords ->
        image.setRGB(coords.x, coords.y, when(value) {
            0 -> Color.BLACK
            1 -> Color.RED
            else -> Color.GREEN
        }.rgb)
    }
    ImageIO.write(image, "png", File("build/out.png"))

    val sol = board.toList().count { it > 1 }
    println(sol)
}
