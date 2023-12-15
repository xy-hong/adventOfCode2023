package day4

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val lines = File("src/main/kotlin/day4/input.txt").readLines()
    val time = measureTimeMillis {
//        val sumPoint = lines.sumOf { cardPoint(it) }
//        println(sumPoint)
        val sumCard = part2(lines)
        println(sumCard)
    }
    println("time $time ms")
}

/** part 1 **/
fun cardPoint(card: String): Int {
    val numbers = card.split(":")[1]
    val winNums = numbers.split("|")[0].trim().split(" ").filter { it.isNotBlank() }.map { it.toInt() }
    val haveNums = numbers.split("|")[1].trim().split(" ").filter { it.isNotBlank() }.map { it.toInt() }
    var point = 0
    haveNums.forEach {
        if (it in winNums) {
            if (point == 0) point = 1 else point *= 2
        }
    }
    return point
}
/** 扩展，左右两边都没有重复的元素，可以看成是集合求交集,列举几种
 * 1）暴力枚举
 * 2）先排序,再遍历
 * 3) 使用 hashMap, 统计每个元素的数量
 */

/** part 2 **/
fun part2(cards: List<String>): Int {
    val counts = IntArray(cards.size) { 1 }
    counts.forEachIndexed { i, count ->
        val point = winCardNo(cards[i])
        for (x in 1..point) {
            if (i+x < counts.size) counts[i+x] += count
        }
    }

    return counts.sum()
}

fun winCardNo(card: String): Int {
    val numbers = card.split(":")[1]
    val winNums = numbers.split("|")[0].trim().split(" ").filter { it.isNotBlank() }.map { it.toInt() }
    val haveNums = numbers.split("|")[1].trim().split(" ").filter { it.isNotBlank() }.map { it.toInt() }
    var point = 0
    haveNums.forEach {
        if (it in winNums) {
            point += 1
        }
    }
    return point
}
