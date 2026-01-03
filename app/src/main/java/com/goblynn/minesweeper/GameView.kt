package com.goblynn.minesweeper

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class GameView : ViewModel() {
    val cells = mutableStateMapOf<Vec2, Cell>()

    var shouldVibrate by mutableStateOf(true)

    var remainingBombCount by mutableIntStateOf(0)
        private set

    var width by mutableIntStateOf(0)
        private set

    var bombCount by mutableIntStateOf(10)
        private set

    var height by mutableIntStateOf(0)
        private set

    var isPlaying by mutableStateOf(false)
        private set

    var won by mutableStateOf(false)
        private set

    var lost by mutableStateOf(false)
        private set

    val canKeepPlaying: Boolean
        get() = !lost && !won && isPlaying

    /**
     * List of all positions to check for bombs when counting
     */
    val positionsToCheck: List<Vec2> = listOf(
        Vec2(-1, -1), Vec2(0, -1), Vec2(1, -1),
        Vec2(-1, 0), Vec2(1, 0),
        Vec2(-1, 1), Vec2(0, 1), Vec2(1, 1)
    )

    /**
     * Start a new game resetting everything
     * @param width width of the playfield
     * @param height height of the playfield
     */
    fun startGame(width: Int, height: Int, bombCount: Int) {
        this.width = width
        this.height = height
        won = false
        lost = false
        this.bombCount = bombCount.coerceAtMost(width * height)
        remainingBombCount = this.bombCount
        cells.clear()
        for (y in 0..<height) {
            for (x in 0..<width) {
                cells[Vec2(x, y)] = Cell(
                    isBomb = false,
                    isFlagged = false,
                    isRevealed = false,
                    bombCount = 0,
                    isBorder = false
                )
            }

        }
        val bombs = ArrayList<Vec2>()
        for (i in 0..<this.bombCount) {
            var x = Random.nextInt(0, width)
            var y = Random.nextInt(0, height)
            Log.i("GENERATOR_TEST", "Trying at pos: $x:$y")
            while (!cells.contains(Vec2(x, y)) || cells[Vec2(x, y)]!!.isBomb) {
                x += 1
                if (x >= width) {
                    x = 0
                    y += 1
                }
                if (y >= height) {
                    x = 0
                    y = 0
                }

            }
            if (!cells.contains(Vec2(x, y))) {
                continue
            }
            cells[Vec2(x, y)] = cells[Vec2(x, y)]!!.copy(isBomb = true)
            bombs.add(Vec2(x, y))
        }
        bombs.forEach { currPos ->
            if (cells[currPos] == null || !cells[currPos]!!.isBomb) {
                return
            }
            positionsToCheck.forEach { pos ->
                if (cells.contains(currPos + pos)) {
                    cells[currPos + pos] = cells[currPos + pos]!!.copy(
                        isBorder = true,
                        bombCount = cells[currPos]!!.bombCount + 1
                    )
                }
            }

        }
        isPlaying = true
    }

    /**
     * Mark given cell as mine
     * @param position cell position
     */
    fun markCell(position: Vec2) {
        if (!canKeepPlaying) {
            return
        }
        if (cells[position] != null && !cells[position]!!.isRevealed) {
            if (!cells[position]!!.isFlagged) {
                remainingBombCount = (remainingBombCount - 1).coerceAtLeast(0)
            } else {
                remainingBombCount = (remainingBombCount + 1).coerceAtMost(bombCount)
            }
            cells[position] = cells[position]!!.copy(isFlagged = !cells[position]!!.isFlagged)
            if (cells.all { cell -> (cell.value.isBomb && cell.value.isFlagged) || (!cell.value.isBomb && !cell.value.isFlagged) }) {
                won = true
            }
        }
    }

    /**
     * Reveal all bombs and mark game as lost
     */
    fun looseGame() {

        for (pos in cells.keys) {
            if (cells[pos]?.isBomb ?: false) {
                cells[pos] = cells[pos]!!.copy(isRevealed = true)
            }
        }
        lost = true
    }

    /**
     * Click cell and run related logic
     * @param position cell position
     * @return true if cell was a bomb
     */
    fun clickCell(position: Vec2): Boolean {
        if (cells[position]!!.isBomb && !cells[position]!!.isFlagged) {
            looseGame()
            return true
        }
        reveal(position)
        return false
    }

    /**
     * Use flood fill to reveal all possible fields from given start point
     * @param position starting position
     */
    fun reveal(position: Vec2) {
        if (!canKeepPlaying) {
            return
        }
        if (!cells.contains(position)) {
            return
        }
        if (cells[position]!!.isBomb || cells[position]!!.isRevealed || cells[position]!!.isFlagged) {
            return
        }

        cells[position] = cells[position]!!.copy(isRevealed = true)
        if (cells[position]!!.isBorder) {
            return
        }
        reveal(Vec2(position.x + 1, position.y))
        reveal(Vec2(position.x - 1, position.y))
        reveal(Vec2(position.x, position.y + 1))
        reveal(Vec2(position.x, position.y - 1))
    }

    /**
     * Quit game without saving
     */
    fun cancelGame() {
        isPlaying = false
    }

}