package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day20Test {
    @Test
    fun part1() {
        assertEquals(35, Day20(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(3351, Day20(DataFile.Example).part2())
    }
}
