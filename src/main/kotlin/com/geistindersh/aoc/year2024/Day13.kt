package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.math.determinant
import com.geistindersh.aoc.helper.report
import com.geistindersh.aoc.helper.strings.extractPositiveLongs

class Day13(
    dataFile: DataFile,
) : AoC<Long, Long> {
    private val games =
        fileToString(2024, 13, dataFile)
            .split("\n\n")
            .map(ClawMachine::from)
            .toList()

    private data class ClawMachine(
        val ax: Long,
        val ay: Long,
        val bx: Long,
        val by: Long,
        val px: Long,
        val py: Long,
    ) {
        fun getScore(): Long {
            val div = determinant(ax, ay, bx, by)
            val x = determinant(px, bx, py, by)
            val y = determinant(ax, px, ay, py)
            if (x % div != 0L || y % div != 0L) return 0L
            return (x / div) * 3 + (y / div)
        }

        companion object {
            fun from(str: String) = str.extractPositiveLongs().let { ClawMachine(it[0], it[1], it[2], it[3], it[4], it[5]) }
        }
    }

    override fun part1() = games.sumOf { it.getScore() }

    override fun part2() =
        games
            .map { it.copy(px = it.px + 10000000000000L, py = it.py + 10000000000000L) }
            .sumOf { it.getScore() }
}

fun day13() {
    val day = Day13(DataFile.Part1)
    report(2024, 13, day.part1(), day.part2())
}
