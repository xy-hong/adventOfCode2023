package day3

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val lines = File("src/main/kotlin/day3/input2.txt").readLines()
    val data = mutableListOf<CharArray>()
    lines.forEach { line ->
        data.add(line.toCharArray())
    }
    val time = measureTimeMillis {
        val r = part2(data)
        println(r)
    }
    println("time $time ms")
}

/** part 1 **/
private fun part1(data: List<CharArray>): Int {
    val maxRow = data.size
    val maxCol = data.last().size
    val partNumbers = mutableListOf<Int>()
    var isPart = false
    var tmp = StringBuilder()
    data.forEachIndexed { row, line ->
        line.forEachIndexed { col, c ->
            if (c.isDigit()) {
                tmp.append(c)
                // 如果已经是 partNumber，则不需要再判断, 继续下一个字符
                if (isPart) {
                    return@forEachIndexed
                }
                if (checkPart(data, row, col, maxRow, maxCol)) {
                    isPart = true
                }
            } else { // 遇见不是数字的情况
                if (tmp.isEmpty()) {
                    return@forEachIndexed
                }
                if (isPart) {
                    partNumbers.add(tmp.toString().toInt())
                }
                tmp.clear()
                isPart = false
            }
        }
        // 换行时
        if (isPart) {
            partNumbers.add(tmp.toString().toInt())
        }
        tmp.clear()
        isPart = false
    }

    return partNumbers.sum()
}

/** 判断某个字符是否是 part number, 即判断周围是否有字符 */
private fun checkPart(data: List<CharArray>, row: Int, col: Int, maxRow: Int, maxCol: Int): Boolean {
    if (row - 1 >= 0 && col - 1 >= 0 && data[row - 1][col - 1].isSymbol()) return true
    if (row - 1 >= 0 && data[row - 1][col].isSymbol()) return true
    if (row - 1 >= 0 && col + 1 < maxCol && data[row - 1][col + 1].isSymbol()) return true
    if (col - 1 >= 0 && data[row][col - 1].isSymbol()) return true
    if (col + 1 < maxCol && data[row][col + 1].isSymbol()) return true
    if (row + 1 < maxRow && col - 1 >= 0 && data[row + 1][col - 1].isSymbol()) return true
    if (row + 1 < maxRow && data[row + 1][col].isSymbol()) return true
    if (row + 1 < maxRow && col + 1 < maxCol && data[row + 1][col + 1].isSymbol()) return true

    return false
}

/** 判断该字符是否是符号 */
private fun Char.isSymbol() = !this.isDigit() && this != '.'

/** part 2 **/
private fun part2(data: List<CharArray>): Int {
    val maxRow = data.size
    val maxCol = data.last().size
    val partNumbers = mutableMapOf<Pair<Int,Int>, MutableList<Int>>()
    var isPart = false
    var x = -1
    var y = -1
    var tmp = StringBuilder()
    data.forEachIndexed { row, line ->
        line.forEachIndexed { col, c ->
            if (c.isDigit()) {
                tmp.append(c)
                // 如果已经是 partNumber，则不需要再判断, 继续下一个字符
                if (isPart) {
                    return@forEachIndexed
                }
                checkPart2(data, row, col, maxRow, maxCol)?.let {
                    isPart = true
                    x = it.first
                    y = it.second
                    if (partNumbers[x to y] == null) {
                        partNumbers[x to y] = mutableListOf()
                    }
                }
            } else { // 遇见不是数字的情况
                if (tmp.isEmpty()) {
                    return@forEachIndexed
                }
                if (isPart) {
                    partNumbers[x to y]!!.add(tmp.toString().toInt())
                }
                tmp.clear()
                x = -1
                y = -1
                isPart = false
            }
        }
        // 换行时
        if (isPart) {
            partNumbers[x to y]!!.add(tmp.toString().toInt())
        }
        tmp.clear()
        isPart = false
        x = -1
        y = -1
    }

    return partNumbers.values.filter { it.size > 1 }.sumOf { it.reduce { acc, i -> acc * i }}
}

private fun checkPart2(data: List<CharArray>, row: Int, col: Int, maxRow: Int, maxCol: Int): Pair<Int, Int>? {
    if (row - 1 >= 0 && col - 1 >= 0 && data[row - 1][col - 1].isStarSymbol()) return row - 1 to col -1
    if (row - 1 >= 0 && data[row - 1][col].isStarSymbol()) return row - 1 to col
    if (row - 1 >= 0 && col + 1 < maxCol && data[row - 1][col + 1].isStarSymbol()) return row -1 to col + 1
    if (col - 1 >= 0 && data[row][col - 1].isStarSymbol()) return row to col - 1
    if (col + 1 < maxCol && data[row][col + 1].isStarSymbol()) return row to col + 1
    if (row + 1 < maxRow && col - 1 >= 0 && data[row + 1][col - 1].isStarSymbol()) return row + 1 to col - 1
    if (row + 1 < maxRow && data[row + 1][col].isStarSymbol()) return row + 1 to col
    if (row + 1 < maxRow && col + 1 < maxCol && data[row + 1][col + 1].isStarSymbol()) return row + 1 to col + 1

    return null
}

private fun Char.isStarSymbol() = this == '*'