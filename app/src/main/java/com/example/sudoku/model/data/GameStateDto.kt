package com.example.sudoku.model.data

import com.example.sudoku.model.data.table.Table

data class GameStateDto(
    val remainingHelpCount: Int,
    val secondsPassed: Int,
    val table: Table
)