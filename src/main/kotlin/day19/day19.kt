package day19

import helper.DataFile
import helper.fileToString
import helper.report

typealias Workflow = Map<String, List<Condition>>

data class Condition(val key: String, val number: Int, val dest: String, val delim: Char) {
    /**
     * Lazy evaluate the condition into a function that takes [Data] and returns the next location
     * to go to.
     *
     * Special cases:
     * * A: Accept; Finished
     * * R: Reject; Finished
     * * "": Go to the next [Condition] in the list
     */
    val eval: (Data) -> String by lazy {
        when (delim) {
            '<' -> { d: Data -> if (key in d.map && d.map[key]!! < number) dest else "" }
            '>' -> { d: Data -> if (key in d.map && d.map[key]!! > number) dest else "" }
            else -> { _: Data -> dest }
        }
    }
}

data class Data(val map: Map<String, Long>) {
    fun sum() = map.values.sum()
}

fun parseInput(fileType: DataFile): Pair<Map<String, List<Condition>>, List<Data>> {
    val (code, data) = fileToString(19, fileType).split("\n\n")

    val workflow = code.split('\n').associate { line ->
        val stage = line.substringBefore('{')
        val conditions = line.substringAfter('{').substringBefore('}').split(',').map { cond ->
            if (!cond.contains("[<>]".toRegex())) {
                Condition("", 0, cond, ' ')
            } else {
                val delim = if ('<' in cond) '<' else '>'
                val key = cond.substringBefore(delim)
                val number = cond.substringAfter(delim).substringBefore(':').toInt()
                val dest = cond.substringAfter(':')

                Condition(key, number, dest, delim)
            }
        }
        stage to conditions
    }

    val parsedData = data.split('\n').map { line ->
        val parts = line.substring(1, line.length - 1).split(',').associate {
            val (k, v) = it.split('=')
            k to v.toLong()
        }
        Data(parts)
    }

    return workflow to parsedData
}

fun part1(workflow: Workflow, data: List<Data>): Long {
    val termination = setOf("A", "R")
    return data.sumOf { value ->
        var workflowName = "in"
        while (workflowName !in termination) {
            workflowName = workflow[workflowName]!!.map { it.eval(value) }.first { it.isNotEmpty() }
        }
        if (workflowName == "A") value.sum()
        else 0
    }
}

/**
 * @param r1 The first range
 * @param r2 The second range
 * @return The intersection of the two ranges, or an empty range if there is no overlap
 */
fun intersect(r1: IntRange, r2: IntRange): IntRange {
    val intersect = r1.intersect(r2)
    return if (intersect.isEmpty()) {
        IntRange.EMPTY
    } else {
        intersect.min()..intersect.max()
    }
}


fun part2(workflow: Workflow): Long {
    fun part2(ranges: Map<String, IntRange>, ruleName: String): Long {
        if (ruleName == "A") {
            return ranges.values.fold(1) { prod, range -> prod * range.count() }
        } else if (ruleName == "R") {
            return 0
        }

        val conditions = workflow[ruleName] ?: throw Exception("Failed to find workflow for rule $ruleName")
        val mutableRange = ranges.toMutableMap()
        var combinations = 0L

        for (cond in conditions) {
            // If any of the ranges are empty, then we know there are no valid values
            if (ranges.any { it.value.isEmpty() }) {
                return 0
            }

            combinations += when (cond.delim) {
                // '<' means we want to try the values between 0 and the number - 1 and that the updated
                // range should be AFTER the number up to the upper bound
                '<' -> {
                    val rangeWithBelow = mutableRange.toMutableMap()
                    rangeWithBelow[cond.key] = intersect(rangeWithBelow[cond.key]!!, 0..<cond.number)
                    mutableRange[cond.key] = intersect(mutableRange[cond.key]!!, cond.number..4000)
                    part2(rangeWithBelow, cond.dest)
                }

                // '<' means we want to try the values between the number + 1 and the upperbound and that the updated
                // range should be BEFORE the number up to 4000
                '>' -> {
                    val rangeWithAbove = mutableRange.toMutableMap()
                    rangeWithAbove[cond.key] = intersect(rangeWithAbove[cond.key]!!, cond.number + 1..4000)
                    mutableRange[cond.key] = intersect(mutableRange[cond.key]!!, 0..cond.number)
                    part2(rangeWithAbove, cond.dest)
                }

                // We know that we will always jump to the destination without changing the range
                ' ' -> part2(mutableRange, cond.dest)
                else -> throw Exception("Unknown delimiter ${cond.delim}")
            }
        }

        return combinations
    }

    val map = mapOf(
        "x" to 1..4000,
        "m" to 1..4000,
        "a" to 1..4000,
        "s" to 1..4000,
    )
    return part2(map, "in")
}

fun day19() {
    val (workflow, data) = parseInput(DataFile.Part1)
    report(
        dayNumber = 19,
        part1 = part1(workflow, data),
        part2 = part2(workflow),
    )
}