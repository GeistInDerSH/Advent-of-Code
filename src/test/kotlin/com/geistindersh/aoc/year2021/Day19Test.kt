package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day19Test {
    @Test
    fun part1() {
        assertEquals(79, Day19(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(3621, Day19(DataFile.Example).part2())
    }
}
