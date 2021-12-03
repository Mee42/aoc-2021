package dev.mee42.day3

import dev.mee42.*;

fun main() {

    val input = inputLines(day = 3, year = 2021,)

    fun findLevel(inverse: Boolean) = (0 until input[0].length).toList().foldr(input) { validNumbers, index ->
        val selection = (validNumbers.map { it[index] }.count { it == '1' } > (validNumbers.size - 1) / 2)
            .xor(inverse)
        validNumbers
            .apIf(validNumbers.size > 1) {
                validNumbers.filter { it[index] == '1' == selection }
            }
    }[0].toInt(radix = 2)

    val o2 = findLevel(false)
    val co2 = findLevel(true)
    println(o2 * co2)
}

fun main1() {
    val input = inputLines(day = 3, year = 2021)
    val eps =  input.transpose().map { list -> if(list.count { it == '1' } > input.size / 2) '1' else '0' }
    val gam = eps.map { if(it == '1') '0' else '1' }.charListToString()
    println(eps.charListToString().toInt(2) * gam.toInt(2))
}
