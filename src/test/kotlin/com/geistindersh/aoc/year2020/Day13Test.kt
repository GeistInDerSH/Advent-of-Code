package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.files.DataFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Day13Test {
    @Test
    fun part1() {
        assertEquals(295, Day13(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(1068781, Day13(DataFile.Example).part2())
    }
}
