package dev.mee42.day15

import dev.mee42.*
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt

val test = """
1163751742
1381373672
2136511328
3694931569
7463417111
1319128137
1359912421
3125421639
1293138521
2311944581
""".trimIndent()

fun Int.wrap() = if(this > 9) this - 9 else this

fun main() {
    val inputRaw = if(1 == 1) test else input(15, 2021)
    val inputSmall: List<List<Int>> = inputRaw.trim().lines().map { it.map(::id).map { x -> "$x".toInt() } }

    val incrAmount = Array2D.from((0..4).map { x -> (0..4).map { y -> x + y } })
    incrAmount.print()
    // extend each row 5 times
    val inputBigRows: List<List<Int>> =
        inputSmall.map { xRow -> (0..4).flatMap { off -> xRow.map { (it + off) } }  } // map each row
    // extend the entire thing 5 times
    val inputBigCols: List<List<Int>> =
        (0..4).flatMap { off -> inputBigRows.map { list -> list.map { (it + off).wrap() } } }
    val input = Array2D.from(inputBigCols.transpose())
    input.print(fixedSpace = 0)
    //return
    val probableDistance = mutableMapOf<Coords2D, Int>(point(0, 0) to 0)
    val unvisited = PriorityQueue<Coords2D>(object : Comparator<Coords2D> {
        override fun compare(o1: Coords2D, o2: Coords2D): Int {
            if(o1 == o2) return 0 // equal
            val f1: Int = (probableDistance[o1] ?: Int.MAX_VALUE)
            val f2: Int = (probableDistance[o2] ?: Int.MAX_VALUE)
            return f1 - f2
        }
    })
    for(elem in input.coordsList) unvisited.offer(elem)
    val visited = mutableSetOf<Coords2D>()
    var current = point(0, 0)
    val goal = point(input.xSize() - 1, input.ySize() - 1)
    val path = mutableListOf<Coords2D>()
    val prev = mutableMapOf<Coords2D, Coords2D>()

    while(true) {
        if(current == goal) {
            println("found goal")
            println("SOLUTION: " + probableDistance[current])
            path.add(goal)
            var node: Coords2D? = goal
            // find the node with the smallest
            while(true) {
                path.add(node!!)
                node = prev[node]
                if(prev[node] == null) break
            }
           break
        }
        println("current: $current visited: ${visited.size} / ${input.xSize() * input.ySize()}")
        val neighbors = CARDINAL_OFFSETS.map { it + current }
        //println("neigbors: $neighbors")
        for(n in neighbors.filter { input.isInBound(it) }) {
            if(n in visited) continue
            //println("looking at neighbor $n")
            val dist = probableDistance[current]?.plus(input[n]) ?: Int.MAX_VALUE
            val currentDist = probableDistance[n] ?: Int.MAX_VALUE
            //println("dist $dist currentDist $currentDist")
            if(dist < currentDist) {
                //println("set")
                probableDistance[n] = dist
                prev[n] = current
            }
            unvisited.remove(n)
            unvisited.add(n)
        }
        unvisited.remove(current)
        visited.add(current)
        val newCurrent = unvisited.remove()
        if(newCurrent == null) {
            println("breaking cause unvisited is $unvisited")
            break
        }
        current = newCurrent
    }
    println()
    input.print { c, coords2D -> if(coords2D in path) "*" else " " }
    println()
    input.print { c, coords2D -> if(coords2D in unvisited) "${probableDistance[coords2D]}" else "V" }
}