package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day3(
    dataFile: DataFile,
) {
    private val input = fileToStream(2024, 3, dataFile).joinToString("")

    private fun MatchGroupCollection.reduceGroup() =
        this
            .mapNotNull { it?.value?.toLongOrNull() }
            .reduce(Long::times)

    fun part1() =
        mulRegex
            .findAll(input)
            .fold(0L) { acc, match -> acc + match.groups.reduceGroup() }

    fun part2(): Long {
        var isEnabled = true
        var total = 0L
        for (match in doDontMulRegex.findAll(input)) {
            when (match.value) {
                "don't()" -> isEnabled = false
                "do()" -> isEnabled = true
                else -> {
                    if (!isEnabled) continue
                    total += match.groups.reduceGroup()
                }
            }
        }
        return total
    }

    companion object {
        private val mulRegex = """mul\((\d+),(\d+)\)""".toRegex()
        private val doDontMulRegex = """(do\(\)|don't\(\)|mul\((\d+),(\d+)\))""".toRegex()
    }
}

fun day3() {
    val day = Day3(DataFile.Part1)
    report(2024, 3, day.part1(), day.part2())
}
