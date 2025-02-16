package com.geistindersh.aoc.year2019

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day14Test {
    @Test
    fun part1() {
        assertEquals(13312, Day14(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(82892753, Day14(DataFile.Example).part2())
        assertEquals(460664, Day14(DataFile.Example2).part2())
    }
}
