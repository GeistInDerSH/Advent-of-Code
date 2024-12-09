package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day10Test {
    @Test
    fun part1() {
        assertEquals(36, Day10(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(81, Day10(DataFile.Example).part2())
    }
}
