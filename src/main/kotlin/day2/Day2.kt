package day2

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val lines = File("src/main/kotlin/day2/input.txt").readLines()
    val time = measureTimeMillis {
        var sum = 0
        lines.forEach {
            sum += powerOf(it)
        }
        println(sum)
    }
    println("time: $time ms")
}

/** part 1 **/
fun part1(line: String): Int {
    val a = line.split(":")
    val gameNo = a[0].split(" ")[1].trim().toInt()
    val times = a[1].split(";")
    times.forEach { turn ->
        if (!isPossible(turn)) {
            return 0
        }
    }

    return gameNo
}

val limit = mapOf("red" to 12, "green" to 13, "blue" to 14)
fun isPossible(turn: String): Boolean {
    val s = turn.trim().split(",")
    s.forEach {
        val a = it.trim().split(" ")
        val no = a[0].toInt()
        val color = a[1]
        if (no > limit[color]!!) {
            return false
        }
    }

    return true
}

/** part 2 **/
fun powerOf(line: String): Int {
    val colors = mutableMapOf<String, Int>()
    val s = line.split(":")[1].replace(";", ",").trim().split(",")
    s.forEach {
        val a = it.trim().split(" ")
        val color = a[1]
        val no = a[0].toInt()
        if (colors[color] == null || colors[color]!! < no) {
            colors[color] = no
        }
    }

    return colors.values.reduce { acc, i -> acc * i }
}