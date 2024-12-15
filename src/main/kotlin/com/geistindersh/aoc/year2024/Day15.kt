package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.enums.Direction
import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day15(
    rawGrid: String,
    rawMoves: String,
) {
    constructor(sections: List<String>) : this(sections.first(), sections.last())
    constructor(dataFile: DataFile) : this(fileToString(2024, 15, dataFile).split("\n\n"))

    private val grid =
        rawGrid
            .split("\n")
            .flatMapIndexed { row, line -> line.mapIndexed { col, char -> Point2D(row, col) to char } }
            .toMap()
            .let { grid ->
                val start = grid.filterValues { it == '@' }.firstNotNullOf { it.key }
                Grid(grid, start)
            }
    private val moves = rawMoves.mapNotNull { Direction.tryFromArrow(it) }
    private val expandedGrid = ExpandedGrid.from(grid)

    private interface Warehouse {
        /**
         * Generate the next state of the [Warehouse] when applying the given [move]
         *
         * @param move The direction to move the cursor
         *
         * @return The new [Warehouse]
         */
        fun next(move: Direction): Warehouse

        /**
         * @return The score of the [Warehouse]
         */
        fun score(): Long
    }

    data class Grid(
        val grid: Map<Point2D, Char>,
        val position: Point2D,
    ) : Warehouse {
        fun maxRow() = grid.keys.maxOf { it.row }

        fun maxCol() = grid.keys.maxOf { it.col }

        override fun score() = grid.filterValues { it == 'O' }.keys.sumOf { it.row * 100L + it.col }

        override fun next(move: Direction): Grid {
            val newPosition = position + move
            val newGrid = grid.toMutableMap()
            return when (grid[newPosition]!!) {
                '.' -> {
                    newGrid[position] = '.'
                    newGrid[newPosition] = '@'
                    Grid(newGrid, newPosition)
                }
                'O' -> {
                    var nextPos = newPosition + move
                    while (nextPos in grid && grid[nextPos]!! == 'O') nextPos += move
                    when (grid[nextPos]!!) {
                        '.' -> {
                            var moving = nextPos
                            while (moving != position) {
                                val prev = moving - move
                                newGrid[moving] = newGrid[prev]!!
                                newGrid[prev] = '.'
                                moving = prev
                            }

                            Grid(newGrid, newPosition)
                        }
                        else -> this
                    }
                }
                else -> this
            }
        }

        override fun toString(): String {
            val rowMax = maxRow()
            val colMax = maxCol()
            val buff = Array(rowMax + 1) { Array(colMax + 1) { ' ' } }
            for ((k, v) in grid) {
                buff[k.row][k.col] = v
            }
            return buff.joinToString("\n") { it.joinToString("") }
        }

        @Suppress("unused")
        fun print() {
            println(this.toString())
        }
    }

    data class ExpandedGrid(
        val grid: Map<Point2D, Char>,
        val position: Point2D,
    ) : Warehouse {
        override fun score() = grid.filterValues { it == '[' }.keys.sumOf { it.row * 100L + it.col }

        private sealed class Obstacle {
            data class Empty(
                val c: Char = '.',
            ) : Obstacle()

            data class Wall(
                val c: Char = '#',
            ) : Obstacle()

            data class Box(
                val lhs: Point2D,
                val rhs: Point2D,
            ) : Obstacle() {
                fun toPair() = lhs to rhs
            }
        }

        /**
         * Get all [Obstacle] touching the given pair of points in the given [Direction].
         *
         * @param move The direction to check for [Obstacle]s in
         *
         * @return The unique [Obstacle]s a [Obstacle.Box] may encounter when moving in that [Direction]
         */
        private fun Pair<Point2D, Point2D>.getTouchingObstacles(move: Direction): Set<Obstacle> {
            val (left, right) = this
            val newLeft = left + move
            val newRight = right + move
            val touchingLeft =
                when (grid[left + move]!!) {
                    '#' -> setOf(Obstacle.Wall())
                    '[' -> setOf(Obstacle.Box(newLeft, newRight))
                    ']' -> setOf(Obstacle.Box(newLeft + Direction.West, newLeft))
                    else -> setOf(Obstacle.Empty())
                }
            val touchingRight =
                when (grid[right + move]!!) {
                    '#' -> setOf(Obstacle.Wall())
                    '[' -> setOf(Obstacle.Box(newRight, newRight + Direction.East))
                    ']' -> setOf(Obstacle.Box(newLeft, newRight))
                    else -> setOf(Obstacle.Empty())
                }
            return touchingLeft + touchingRight
        }

        /**
         * Determine if pushing the point will cause it to impact any [Obstacle.Wall]s when being pushed
         * in the [move] [Direction]. This will check all boxes touching the starting box, recursively.
         *
         * @param move The [Direction] the box is being pushed
         * @return If the box can be pushed in that direction
         */
        private fun Pair<Point2D, Point2D>.canMove(move: Direction): Boolean {
            val queue = ArrayDeque<Pair<Point2D, Point2D>>().apply { add(this@canMove) }
            while (queue.isNotEmpty()) {
                val point = queue.removeFirst()
                for (touch in point.getTouchingObstacles(move)) {
                    when (touch) {
                        is Obstacle.Wall -> return false
                        is Obstacle.Box -> queue.add(touch.toPair())
                        is Obstacle.Empty -> continue
                    }
                }
            }
            return true
        }

        /**
         * Get all [Obstacle.Box] as [Pair]s of [Point2D] that are touching the current point, in the [Direction].
         * This should only be used after [canMove] has been called for the point.
         *
         * @param move The direction that the boxes are going to be moved
         *
         * @return All boxes touching the initial box in the direction
         */
        private fun Pair<Point2D, Point2D>.getTouchingBoxes(move: Direction): List<Pair<Point2D, Point2D>> {
            val queue = ArrayDeque<Pair<Point2D, Point2D>>().apply { add(this@getTouchingBoxes) }
            val boxes = mutableListOf(this@getTouchingBoxes)
            while (queue.isNotEmpty()) {
                val point = queue.removeFirst()
                for (touch in point.getTouchingObstacles(move)) {
                    when (touch) {
                        is Obstacle.Wall -> continue
                        is Obstacle.Box -> {
                            queue.add(touch.toPair())
                            boxes.add(touch.toPair())
                        }
                        is Obstacle.Empty -> continue
                    }
                }
            }
            return boxes
        }

        override fun next(move: Direction): ExpandedGrid {
            val newPosition = position + move
            val newGrid = grid.toMutableMap()
            return when (grid[newPosition]!!) {
                '.' -> {
                    newGrid[position] = '.'
                    newGrid[newPosition] = '@'
                    ExpandedGrid(newGrid, newPosition)
                }
                '[', ']' -> {
                    if (move == Direction.East || move == Direction.West) {
                        var nextPos = newPosition + move
                        while (nextPos in grid && (grid[nextPos]!! == '[' || grid[nextPos]!! == ']')) nextPos += move
                        when (grid[nextPos]!!) {
                            '.' -> {
                                var moving = nextPos
                                while (moving != position) {
                                    val prev = moving - move
                                    newGrid[moving] = newGrid[prev]!!
                                    newGrid[prev] = '.'
                                    moving = prev
                                }

                                ExpandedGrid(newGrid, newPosition)
                            }
                            else -> this
                        }
                    } else {
                        val box =
                            if (newGrid[newPosition] == '[') {
                                newPosition to newPosition + Direction.East
                            } else {
                                newPosition + Direction.West to newPosition
                            }
                        if (!box.canMove(move)) {
                            this
                        } else {
                            val touching = box.getTouchingBoxes(move).reversed()
                            for ((lhs, rhs) in touching) {
                                newGrid[lhs + move] = '['
                                newGrid[rhs + move] = ']'
                                newGrid[lhs] = '.'
                                newGrid[rhs] = '.'
                            }
                            newGrid[newPosition] = '@'
                            newGrid[position] = '.'

                            ExpandedGrid(newGrid, newPosition)
                        }
                    }
                }
                else -> this
            }
        }

        companion object {
            fun from(grid: Grid): ExpandedGrid {
                val newGrid =
                    grid
                        .toString()
                        .replace("#", "##")
                        .replace("O", "[]")
                        .replace(".", "..")
                        .replace("@", "@.")
                        .split("\n")
                        .flatMapIndexed { row, line -> line.mapIndexed { col, char -> Point2D(row, col) to char } }
                        .toMap()
                val start = newGrid.filterValues { it == '@' }.keys.first()
                return ExpandedGrid(newGrid, start)
            }
        }
    }

    private fun moveBoxes(warehouse: Warehouse) =
        generateSequence(0 to warehouse) { (i, g) -> if (i > moves.lastIndex) null else i + 1 to g.next(moves[i]) }
            .takeWhile { true }
            .last()
            .second
            .score()

    fun part1() = moveBoxes(grid)

    fun part2() = moveBoxes(expandedGrid)
}

fun day15() {
    val day = Day15(DataFile.Part1)
    report(2024, 15, day.part1(), day.part2())
}
