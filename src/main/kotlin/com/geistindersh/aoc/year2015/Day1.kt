package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day1(dataFile: DataFile) {
    private val floors = fileToString(2015, 1, dataFile)
        .mapNotNull {
            when (it) {
                '(' -> 1
                ')' -> -1
                else -> null
            }
        }
        .toList()

    fun part1() = floors.sum()
    fun part2() = 0
}

fun day1() {
    val day = Day1(DataFile.Part1)
    report(2015, 1, day.part1(), day.part2())
}