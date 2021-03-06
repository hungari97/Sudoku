package com.example.sudoku.model

import com.example.sudoku.model.data.GameStateDto
import com.example.sudoku.model.data.TableType
import com.example.sudoku.model.data.table.Table

interface TableRepository {
    fun getTable(mode: TableType): Table
    fun loadGameOrNull(): GameStateDto?
    fun saveGame(table: Table, remainingHelp: Int, secondsPassed: Int)
    fun createTable(table: Table)
    fun getBlankTable(mode: TableType): Table
    fun clearGameState()
}