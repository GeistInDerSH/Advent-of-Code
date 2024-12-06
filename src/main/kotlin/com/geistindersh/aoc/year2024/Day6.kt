package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.enums.Direction
import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day6(
    dataFile: DataFile,
) {
    private val grid =
        fileToStream(2024, 6, dataFile)
            .flatMapIndexed { row, line ->
                line.mapIndexed { col, value -> Point2D(row, col) to value }
            }.toMap()
    private val start = grid.filterValues { it == '^' }.keys.first()

    private fun Map<Point2D, Char>.hasLoop(): Boolean {
        val memory = mutableSetOf<Pair<Point2D, Direction>>()
        var pos = start
        var dir = Direction.North
        while (true) {
            if (!memory.add(pos to dir)) return true
            val next = pos + dir
            if (next !in this) return false
            if (this[next] == '#') {
                dir = dir.turnRight()
            } else {
                pos = next
            }
        }
    }

    private fun traverse() =
        generateSequence(start to Direction.North) { (pos, dir) ->
            val newPos = pos + dir
            if (grid.getOrDefault(newPos, '.') == '#') {
                pos to dir.turnRight()
            } else {
                newPos to dir
            }
        }.map { it.first }
            .takeWhile { it in grid }
            .toSet()

    fun part1() = traverse().count()

    fun part2(): Int =
        traverse()
            .parallelStream()
            .map { point -> if (grid.toMutableMap().also { it[point] = '#' }.hasLoop()) 1 else 0 }
            .reduce(Int::plus)
            .orElse(-1)
}

fun day6() {
    val day = Day6(DataFile.Part1)
    report(2024, 6, day.part1(), day.part2())
}
