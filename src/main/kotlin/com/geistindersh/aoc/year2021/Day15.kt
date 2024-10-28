package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.enums.Point
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report
import java.util.*

class Day15(dataFile: DataFile) {
	private val graph = fileToStream(2021, 15, dataFile)
		.flatMapIndexed { row, line ->
			line.mapIndexed { col, value -> Point(row, col) to value.digitToInt() }
		}
		.toMap()

	private data class Path(val current: Point, val risk: Int, val steps: Set<Point>)

	private fun Map<Point, Int>.getPathCost(): Int {
		val start = Point(0, 0)
		val end = this.let { g ->
			val rowMax = g.maxOf { it.key.row }
			val colMax = g.maxOf { it.key.col }
			Point(rowMax, colMax)
		}
		val queue: PriorityQueue<Path> = PriorityQueue<Path>(compareBy { it.risk })
			.apply { add(Path(start, 0, setOf(start))) }
		val seen = mutableSetOf<Point>()

		while (queue.isNotEmpty()) {
			val head = queue.poll()
			if (head.current in seen) continue
			if (head.current == end) return head.risk
			seen.add(head.current)

			val next = head
				.current
				.neighbors()
				.filter { it in this && it !in head.steps }
				.map { Path(it, head.risk + this[it]!!, head.steps + listOf(it)) }
			queue.addAll(next)
		}
		return -1
	}

	private fun Map<Point, Int>.scale(): Map<Point, Int> {
		val maxRow = this.keys.maxOf { it.row } + 1
		val maxCol = this.keys.maxOf { it.col } + 1

		val mut = this.toMutableMap()
		for (row in 0..<maxRow) {
			for (col in maxCol..<5 * maxCol) {
				val risk = mut[Point(row, col - maxCol)]!!
				val newRisk = ((risk + 1) % 10).coerceAtLeast(1)
				val newPoint = Point(row, col)
				mut[newPoint] = newRisk
			}
		}
		for (row in maxRow..<5 * maxRow) {
			for (col in 0..<5 * maxCol) {
				val risk = mut[Point(row - maxRow, col)]!!
				val newRisk = ((risk + 1) % 10).coerceAtLeast(1)
				val newPoint = Point(row, col)
				mut[newPoint] = newRisk
			}
		}
		return mut
	}

	fun part1() = graph.getPathCost()
	fun part2() = graph.scale().getPathCost()
}

fun day15() {
	val day = Day15(DataFile.Part1)
	report(2021, 15, day.part1(), day.part2())
}