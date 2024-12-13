package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day12Test {
    @Test
    fun part1() {
        assertEquals(226, Day12(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(3509, Day12(DataFile.Example).part2())
    }
}
