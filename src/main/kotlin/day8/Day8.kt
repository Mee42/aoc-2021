package dev.mee42.day8

import dev.mee42.*

val testShort = """
acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf
""".trimIndent()
val test = """
    
    
be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce
""".trimIndent().trim()

// 0000
//1    2
//1    2
// 3333
//4    5
//4    5
// 6666


fun main() {
    val inputStr = if (0 == 0) input(day = 8, year = 2021).trim() else test.trim()
    val input = inputStr.split("\n").map { it.trim() }

    val numbers = mutableMapOf<Int, Int>()
    val sol = input.map { line ->
        val (firstPart, secondPart) = line.split("|").map { it.trim() }

        // remove elements as we strike them out
        val couldBe = mutableMapOf<Int, MutableList<Char>>()
        for(i in 0..6) {
            couldBe[i] = ('a'..'g').toMutableList()
        }
        firstPart.split(" ").forEach { n ->
            val (weKnow, numberFound) = when (n.length) {
                3 -> listOf(0, 2, 5) to 7
                4 -> listOf(1, 2, 3, 5) to 4
                2 -> listOf(2, 5) to 1
                7 -> listOf(0, 1, 2, 3, 4, 5, 6) to 8
                else -> null to -1
            }
            if(weKnow != null) {// the list of chars we know it is
                for(i in 0..6) {
                    if(i !in weKnow) {
                        couldBe[i]!!.removeAll(n.toList())
                    }
                }
            }
        }
        // simplify down to pairs
        for(x in 0..10) {
            for (i1 in 0..6) {
                for (i2 in 0..6) {
                    if (i1 == i2) continue
                    if (couldBe[i1]!! == couldBe[i2]!! && couldBe[i1]!!.size == 2) {
                        for (i3 in 0..6) {
                            if (i3 != i1 && i3 != i2) couldBe[i3]!!.removeAll(couldBe[i1]!!)
                        }
                    }
                }
            }
            for(i1 in 0..6) {
                if(couldBe[i1]!!.size == 1) {
                    for (i2 in 0..6) {
                        if(i1 != i2) couldBe[i2]!!.remove(couldBe[i1]!!.first())
                    }
                }
            }
        }

        // disambiguate 6 and 2
        val sixCode = firstPart.split(" ").first { n ->
            n.length == 6 && (couldBe[0]!! + couldBe[1]!! + couldBe[4]!!).all { it in n }
        }
        val fiveLetter = sixCode.toMutableList().run {
            removeAll(couldBe[0]!!)
            removeAll(couldBe[1]!!)
            removeAll(couldBe[4]!!)
            this
        }.first()
        couldBe[5]!!.removeIf { it != fiveLetter }
        couldBe[2]!!.removeIf { it == fiveLetter }

        // disambiguate 1 and 3
        val twoCode = firstPart.split(" ").first { n ->
            n.length == 5 && (couldBe[0]!! + couldBe[2]!! + couldBe[6]!!).all { it in n}
        }

        val threeLetter = twoCode.toMutableList().run {
            removeAll(couldBe[0]!!)
            removeAll(couldBe[2]!!)
            removeAll(couldBe[4]!!)
            this
        }.first()

        couldBe[3]!!.removeIf { it != threeLetter }
        couldBe[1]!!.removeIf { it == threeLetter }

        val fiveCodeAlready = couldBe[0]!! + couldBe[1]!! + couldBe[3]!! + couldBe[5]!!
        val fiveCode = firstPart.split(" ").first { n ->
            n.length == 5 && fiveCodeAlready.all { it in n }
        }

        val sixLetter = fiveCode.toMutableList().run {
            removeAll(fiveCodeAlready)
            this
        }.first()

        couldBe[6]!!.removeIf { it != sixLetter }
        couldBe[4]!!.removeIf { it == sixLetter }

        val solution = couldBe.map { it.value.first() to it.key }.toMap()
        println(solution)
        // maps the char to the physical position
        val number = secondPart.split(" ").map { n ->
            "" + when(val x = n.map { solution[it]!! }.sorted()) {
                listOf(0, 1, 2, 4, 5, 6) -> 0
                listOf(2, 5) -> 1
                listOf(0, 2, 3, 4, 6) -> 2
                listOf(0, 2, 3, 5, 6) -> 3
                listOf(1, 2, 3, 5) -> 4
                listOf(0, 1, 3, 5, 6) -> 5
                listOf(0, 1, 3, 4, 5, 6) -> 6
                listOf(0, 2, 5) -> 7
                listOf(0, 1, 2, 3, 4, 5, 6) -> 8
                listOf(0, 1, 2, 3, 5, 6) -> 9
                else -> error("FUCK YOU $x $n")
            }
        }.joinToString("").toInt()
        // find one that's six
        println(number)
        number
    }.sum()
    println(sol)
}
