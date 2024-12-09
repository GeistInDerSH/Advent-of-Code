package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day21Test {
    @Test
    fun part1() {
        assertEquals(739785, Day21(DataFile.Example).part1())
    }

    @Test
    fun part2() {
        assertEquals(444356092776315, Day21(DataFile.Example).part2())
    }
}
