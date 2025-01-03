package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.binary.digitCount
import com.geistindersh.aoc.helper.caching.memoize
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report
import kotlin.math.pow

class Day11(
    dataFile: DataFile,
) : AoC<Long, Long> {
    // Pre-store 0L -> [1L] in the memory to avoid needing to add the check in the memoized function for speed reasons
    private val memoizedCore = memoize(::core, mapOf(0L to listOf(1L)))
    private val stones = fileToString(2024, 11, dataFile).split(" ").associate { it.toLong() to 1L }

    private fun core(value: Long): List<Long> {
        val digits = value.digitCount()
        return if (digits % 2 == 0) {
            val div = 10.0.pow(digits / 2.0).toLong()
            listOf(value.floorDiv(div), value % div)
        } else {
            listOf(value * 2024)
        }
    }

    private fun Map<Long, Long>.update() =
        this
            .flatMap { (k, v) -> memoizedCore(k).map { it to v } }
            .groupingBy { it.first }
            .fold(0L) { acc, element -> acc + element.second }

    private fun Map<Long, Long>.blink(times: Int) =
        generateSequence(this) { it.update() }
            .drop(times)
            .first()
            .values
            .sum()

    override fun part1() = stones.blink(25)

    override fun part2() = stones.blink(75)
}

fun day11() {
    val day = Day11(DataFile.Part1)
    report(2024, 11, day.part1(), day.part2())
}
