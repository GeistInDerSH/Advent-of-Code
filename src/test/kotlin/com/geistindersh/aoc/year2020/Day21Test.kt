package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day21Test {
    @Test
    fun part1() {
        assertEquals(5, Day21(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals("mxmxvkd,sqjhc,fvjkl", Day21(DataFile.Example).part2())
    }
}