package com.example.sudoku.model.data

data class GameStateDto(
    val remainingHelpCount: Int,
    val secondsPassed: Int,
    val table: Table
)