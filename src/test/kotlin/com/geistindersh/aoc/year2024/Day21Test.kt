package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day21Test {
    @Test
    fun part1() {
        assertEquals(126384, Day21(DataFile.Example).part1())
//        assertEquals(-1, Day21(DataFile.Part1).part1())
    }

    @Test
    fun part2() {
        assertEquals(-1, Day21(DataFile.Example).part2())
        assertEquals(-1, Day21(DataFile.Part1).part2())
    }
}
