package day1

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val lines = File("src/main/kotlin/day1/input.txt").readLines()
    val time = measureTimeMillis {
        var sum = 0
        lines.forEach {
            sum += getNumber2(it)
        }
        println(sum)
    }
    println("---- time taken $time ms ----")
}

/** part 1 **/
private fun getNumber(line: String): Int {
    val n = line.find { it.isDigit() }
    val n2 = line.findLast { it.isDigit() }
    return n!!.digitToInt() * 10 + n2!!.digitToInt()
}

/** part 2 **/
val letterNumbers = listOf<String>("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
val numbers = listOf<String>("1", "2", "3", "4", "5", "6", "7", "8", "9")
private fun getNumber2(line: String): Int {
    val n = findNumber(line)
    val n2 = findNumber(line, true)
    return n * 10 + n2
}

private fun findNumber(line: String, last: Boolean = false): Int {
    // number
    val n = if (last) line.findLastAnyOf(numbers) else line.findAnyOf(numbers)
    // letter number
    val n2  = if (last) line.findLastAnyOf(letterNumbers) else line.findAnyOf(letterNumbers)
    if (n == null) {
        return mapToInt(n2!!.second)
    }
    if (n2 == null) {
        return mapToInt(n!!.second)
    }

    return if (last) {
        if (n.first > n2.first) mapToInt(n.second) else mapToInt(n2.second)
    } else {
        if (n.first < n2.first) mapToInt(n.second) else mapToInt(n2.second)
    }
}

private fun mapToInt(number: String): Int {
    if (number.length == 1) return number.toInt()

    letterNumbers.indexOf(number).let {
        if (it < 0) {
            throw RuntimeException("cant find letter number $number")
        }

        return it + 1
    }
}