package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day17Test {

    @Test
    fun part1() {
        assertEquals(3068, Day17(DataFile.Example).part1())
        assertEquals(3147, Day17(DataFile.Part1).part1())
    }

    @Test
    fun part2() {
        assertEquals(1514285714288, Day17(DataFile.Example).part2())
        assertEquals(1532163742758, Day17(DataFile.Part1).part2())
    }
}