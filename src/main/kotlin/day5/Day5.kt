package day5

import java.io.File
import kotlin.math.max
import kotlin.math.min
import kotlin.system.measureTimeMillis

fun main() {
    val lines = File("src/main/kotlin/day5/input.txt").readLines()
    val time = measureTimeMillis {
        println(part2(lines))
    }
    println("time $time ms")
}

/** part 1 **/
fun part1(lines: List<String>): Long {
    val seeds = lines[0].split(":")[1].trim().split(" ").map { it.toLong() }
    val locations = mutableListOf<Long>()
    val almanac = getAlmanac(lines)
    seeds.forEach {
        locations.add(getLocation(it, almanac))
    }
    return locations.min()
}

fun getAlmanac(lines: List<String>): Array<MutableList<Triple<Long, Long, Long>>> {
    var type = -1
    var i = 1
    val almanac = Array(7) { mutableListOf<Triple<Long, Long, Long>>() }
    while (i < lines.size) {
        val line = lines[i]
        if (line.endsWith(":")) {
            type += 1
        } else if (line.isEmpty()) {

        } else {
            val numbers = line.split(" ")
            almanac[type].add(Triple(numbers[1].toLong(), numbers[0].toLong(), numbers[2].toLong()))
        }
        i++
    }

    return almanac
}

fun getLocation(seed: Long, almanac: Array<MutableList<Triple<Long, Long, Long>>>): Long {
    var r = seed
    almanac.forEach { types ->
        for (it in types) {
            val left = it.first
            val right = it.first + it.third
            if (r in left until right) {
                r += (it.second - it.first)
                break
            }
        }
    }
    return r
}

/** part 2 **/
fun part2(lines: List<String>): Long {
    val locations = mutableListOf<Pair<Long, Long>>()
    val almanac = getAlmanac(lines)
    val seedRanges = getSeedRanges(lines)
    seedRanges.forEach {
        locations.addAll(afterRange(it, almanac))
    }
    return locations.map { it.first }.min()
}

fun getSeedRanges(lines: List<String>): List<Pair<Long, Long>> {
    val r = mutableListOf<Pair<Long, Long>>()
    val seeds = lines[0].split(":")[1].trim().split(" ").map { it.toLong() }
    for (i in seeds.indices step 2) {
        r.add(seeds[i] to seeds[i + 1])
    }
    return r
}

private fun afterRange(
    seedRange: Pair<Long, Long>,
    almanac: Array<MutableList<Triple<Long, Long, Long>>>
): List<Pair<Long, Long>> {
    var result = mutableListOf<Pair<Long, Long>>()
    result.add(seedRange.first to seedRange.first + seedRange.second)
    almanac.forEach {
        result = afterMapping(result, it)
    }

    return result
}

private fun afterMapping(ranges: MutableList<Pair<Long, Long>>, filters: MutableList<Triple<Long, Long, Long>>): MutableList<Pair<Long, Long>> {
    val result = mutableListOf<Pair<Long, Long>>()
    val source = mutableListOf<Pair<Long, Long>>()
    source.addAll(ranges)
    val same = mutableListOf<Pair<Long, Long>>()
    filters.forEach { f ->
        val left = f.first
        val right = f.first + f.third - 1
        val gap = f.second - f.first
        same.clear()
        source.forEach { range ->
            if (range.second < left) {
                same.add(range.first to range.second)
                return@forEach
            }

            if (range.first > right) {
                same.add(range.first to range.second)
                return@forEach
            }

            if (range.first < left) {
                same.add(range.first to left - 1)
            }

            if (range.second > right ) {
                same.add(right + 1 to range.second)
            }

            result.add(max(range.first, left) + gap  to min(range.second, right) + gap)
        }
        source.clear()
        source.addAll(same)
    }
    result.addAll(same)
    return result
}