package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day21Test {

    @Test
    fun part1() {
        assertEquals(152, Day21(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(301, Day21(DataFile.Example).part2())
    }
}