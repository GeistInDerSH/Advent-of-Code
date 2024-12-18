package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day18Test {
    @Test
    fun part1() {
        assertEquals(22, Day18(DataFile.Example).part1())
        assertEquals(-1, Day18(DataFile.Part1).part1())
    }

    @Test
    fun part2() {
        assertEquals(-1, Day18(DataFile.Example).part2())
        assertEquals(-1, Day18(DataFile.Part1).part2())
    }
}