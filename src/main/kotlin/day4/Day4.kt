package dev.mee42.day4

import dev.mee42.*

fun main() {
    val inputRaw = input(day = 4, year = 2021)

    val (inputNumbers, boardsSplit) = inputRaw.split("\n", limit = 2)
        .run { this[0].ints() to this[1].split("\n\n").map { it.trim() } }

    println(inputNumbers)

    var sol = 0
    data class Square(val number: Int, var marked: Boolean)
    data class Board(val squares: List<List<Square>>)

    val boards = boardsSplit.map {
        Board(it.trim().split("\n").map { line ->
            println("\"$line\"")
            line.split(Regex("""\s+""")).filter { it.isNotEmpty() }.map { number ->
                Square(number.toInt(), false)
            }
        })
    }.toMutableList()

    val called = mutableListOf<Int>()
    for(bingoNumber in inputNumbers) {
        println("calling $bingoNumber")
        called.add(bingoNumber)
        val removedBoards= mutableListOf<Board>()
        for((i, board ) in boards.withIndex()) {
            for(row in 0 until 5) {
                for (col in 0 until 5) {
                    if(board.squares[row][col].number == bingoNumber) {
                        board.squares[row][col].marked = true
                    }
                }
            }
            // check if it won
            var win = false
            var winBySum: List<Square>? = null
            for(row in 0 until 5) {
                // check row win
                if(board.squares[row].all { it.number in called }) {
                    println("Row")
                    win = true
                    winBySum = board.squares[row]
                }
            }
            val transposed = board.squares.transpose()
            for(col in 0 until 5) {
                if(transposed[col].all { it.number in called }) {
                    println("col")
                    win = true
                    winBySum = transposed[col]
                }
            }
            if(win) {
                removeBoard.add(board)
                val winBySumNum = board.squares.flatten().filter { it.number !in called }.map { it.number }
                println("board $i won")
                println("sum: " + winBySum)
                println("sumnum: " + winBySumNum + " : " + winBySumNum.sum())
                println("just called: "  + bingoNumber)
                println("mult:" + winBySumNum.sum() * bingoNumber)
                sol = winBySumNum.sum() * bingoNumber
            }
        }
        boards.removeAll(removeBoard)
    }


    println(sol)
}
