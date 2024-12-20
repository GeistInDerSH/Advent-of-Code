package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day16Test {
    @Test
    fun part1() {
        assertEquals(7036, Day16(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(45, Day16(DataFile.Example).part2())
    }
}
