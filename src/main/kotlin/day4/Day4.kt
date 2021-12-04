package dev.mee42.day4

import dev.mee42.*

fun main() {
    val inputRaw = input(day = 4, year = 2021)

    val (bingoNumbers, boardsSplit) = inputRaw
        .split("\n", limit = 2)
        .twoElements()
        .manipulateTuple { bingoNumbers, boards ->
            bingoNumbers.ints() to boards.split("\n\n").map { it.trim() }
        }


    data class Board(val squares: List<List<Int>>) {
        val transposed = squares.transpose()
    }

    val boards = boardsSplit.map {
        Board(it.trim().split("\n").map { line ->
            line.split(Regex("""\s+""")).filter { x -> x.isNotEmpty() }.map { number ->
                number.toInt()
            }
        })
    }.toMutableList()

    val called = mutableListOf<Int>()
    var sol = 0
    outer@ for(bingoNumber in bingoNumbers) {
        called.add(bingoNumber)
        val iterator = boards.listIterator()
        for(board in iterator) {
            val win = (0 until 5).any { index ->
                board.squares[index].all { it in called } ||
                board.transposed[index].all { it in called}
            }
            if(win) {
                iterator.remove()
                val sumOfUnmarked = board.squares.flatten().filter { it !in called }.sum()
                sol = sumOfUnmarked * bingoNumber
                // for part one, exit early here. For part two, continue til the last board is removed
                break@outer
            }
        }
    }


    println(sol)
}
