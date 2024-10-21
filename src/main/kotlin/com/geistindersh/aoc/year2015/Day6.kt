package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.enums.Point
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day6(dataFile: DataFile) {
    private val instructions = fileToStream(2015, 6, dataFile).map { Action.from(it) }.toList()

    private sealed class Action(val start: Point, val end: Point) {
        class Toggle(start: Point, end: Point) : Action(start, end)
        class Enable(start: Point, end: Point) : Action(start, end)
        class Disable(start: Point, end: Point) : Action(start, end)

        fun generateLights() = (start.row..end.row)
            .flatMap { row ->
                (start.col..end.col).map { col -> Point(row, col) }
            }.toSet()

        companion object {
            fun from(line: String): Action {
                val words = line.split(' ')
                return if (words[0] == "toggle") {
                    val start = words[1].split(',').map(String::toInt).let { Point(it[0], it[1]) }
                    val end = words[3].split(',').map(String::toInt).let { Point(it[0], it[1]) }
                    Toggle(start, end)
                } else if (words[1] == "on") {
                    val start = words[2].split(',').map(String::toInt).let { Point(it[0], it[1]) }
                    val end = words[4].split(',').map(String::toInt).let { Point(it[0], it[1]) }
                    Enable(start, end)
                } else {
                    val start = words[2].split(',').map(String::toInt).let { Point(it[0], it[1]) }
                    val end = words[4].split(',').map(String::toInt).let { Point(it[0], it[1]) }
                    Disable(start, end)
                }
            }
        }
    }

    private fun followInstructions(): Set<Point> {
        val enabledLights = mutableSetOf<Point>()
        for (rule in instructions) {
            val lights = rule.generateLights()
            when (rule) {
                is Action.Toggle -> {
                    for (light in lights) {
                        if (light in enabledLights) {
                            enabledLights.remove(light)
                        } else {
                            enabledLights.add(light)
                        }
                    }
                }

                is Action.Disable -> {
                    for (light in lights) {
                        enabledLights.remove(light)
                    }
                }

                is Action.Enable -> {
                    for (light in lights) {
                        enabledLights.add(light)
                    }
                }
            }
        }
        return enabledLights
    }

    fun part1() = followInstructions().count()
    fun part2() = 0
}

fun day6() {
    val day = Day6(DataFile.Part1)
    report(2015, 6, day.part1(), day.part2())
}
