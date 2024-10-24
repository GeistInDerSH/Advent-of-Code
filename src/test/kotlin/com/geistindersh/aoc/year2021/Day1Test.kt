package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day1Test {

	@Test
	fun part1() {
		assertEquals(7, Day1(DataFile.Example).part1())
		assertEquals(1791, Day1(DataFile.Part1).part1())
	}

	@Test
	fun part2() {
		assertEquals(5, Day1(DataFile.Example).part2())
		assertEquals(1822, Day1(DataFile.Part1).part2())
	}
}