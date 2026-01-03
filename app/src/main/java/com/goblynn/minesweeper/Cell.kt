package com.goblynn.minesweeper


data class Cell(
    val isBomb: Boolean,
    val isFlagged: Boolean,
    val isRevealed: Boolean,
    val bombCount: Int,
    val isBorder : Boolean,
)