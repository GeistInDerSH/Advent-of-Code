package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day24(dataFile: DataFile) {
	private val weights = fileToStream(2015, 24, dataFile).map(String::toInt).toList()

	private data class Group(val weights: Set<Int>) : Comparable<Group> {
		fun quantumEntanglement() = weights.fold(1L, Long::times)
		fun hasOverlap(other: Group) = other.weights.any { it in weights }

		override fun compareTo(other: Group): Int {
			if (other.weights.size != weights.size) return weights.size.compareTo(other.weights.size)
			return quantumEntanglement().compareTo(other.quantumEntanglement())
		}
	}

	private fun List<Int>.subsetSum(target: Int): Sequence<List<Int>> = sequence {
		if (target == 0) {
			yield(emptyList())
			return@sequence
		}
		val options = this@subsetSum
		options.forEachIndexed { index, option ->
			if (option <= target) {
				this@subsetSum.subList(index + 1, options.size)
					.subsetSum(target - option)
					.forEach { yield(listOf(option) + it) }
			}
		}
	}

	private fun findGroupOrNull(size: Int): Group? {
		val target = weights.sum() / size
		val groups = weights
			.subsetSum(target)
			.map { Group(it.toSet()) }
			.sorted()
			.toList()

		groups.forEach { bestGroup ->
			val isEvenlyGrouped = groups
				.filter { group -> !bestGroup.hasOverlap(group) }
				.canPartitionBy(size - 1)
			if (isEvenlyGrouped) return bestGroup
		}
		return null
	}

	private fun Collection<Group>.canPartitionBy(size: Int): Boolean {
		return if (size == 1) {
			this.isNotEmpty()
		} else {
			this.firstOrNull { group ->
				this.filter { nextGroup -> !nextGroup.hasOverlap(group) }
					.canPartitionBy(size - 1)
			} != null
		}
	}

	fun part1() = findGroupOrNull(3)?.quantumEntanglement() ?: throw Exception("No solution")

	fun part2() = findGroupOrNull(4)?.quantumEntanglement() ?: throw Exception("No solution")
}

fun day24() {
	val day = Day24(DataFile.Part1)
	report(2015, 24, day.part1(), day.part2())
}
