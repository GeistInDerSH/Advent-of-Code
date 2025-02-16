package com.geistindersh.aoc.year2019

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report
import kotlin.math.ceil

class Day14(
    dataFile: DataFile,
) : AoC<Long, Long> {
    private val data: Map<String, Reaction> =
        fileToStream(2019, 14, dataFile)
            .map { line ->
                val (count, key) = line.substringAfter("=> ").split(" ")
                val values = line.substringBefore("=> ").split(",").map { it.trim().split(" ").let { OreCost(it[1], it[0].toLong()) } }
                key to Reaction(OreCost(key, count.toLong()), values)
            }.toMap()

    private data class OreCost(
        val name: String,
        val amount: Long,
    )

    private data class Reaction(
        val produces: OreCost,
        val input: List<OreCost>,
    )

    init {
        data.forEach(::println)
    }

    private fun MutableMap<String, Long>.leftovers(target: OreCost): Long =
        if (target.name in this) {
            val amount = this[target.name]!!
            if (target.amount <= amount) {
                this[target.name] = amount - target.amount
                0
            } else {
                this.remove(target.name)
                target.amount - amount
            }
        } else {
            target.amount
        }

    private fun Map<String, Reaction>.expand(): Long {
        val queue = ArrayDeque<OreCost>().apply { add(OreCost("FUEL", 1)) }
        val inventory = mutableMapOf<String, Long>()
        var oreRequired = 0L
        while (queue.isNotEmpty()) {
            val head = queue.removeFirst()
            if (head.name == "ORE") {
                oreRequired += head.amount
                continue
            }
            val required = inventory.leftovers(head)
            if (required == 0L) continue
            val reactions = this[head.name]!!
            val mul = ceil(required.toDouble() / reactions.produces.amount.toDouble()).toLong()
            val result = (reactions.produces.amount * mul) - required
            if (result > 0) {
                inventory.putIfAbsent(head.name, 0)
                inventory[head.name] = inventory[head.name]!! + result
            }

            for (req in reactions.input) {
                queue.add(req.copy(amount = req.amount * mul))
            }
        }

        return oreRequired
    }

    override fun part1() = data.expand()

    override fun part2() = 0L
}

fun day14() {
    val day = Day14(DataFile.Part1)
    report(2019, 14, day.part1(), day.part2())
}
