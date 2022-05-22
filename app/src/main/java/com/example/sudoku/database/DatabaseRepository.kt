package com.example.sudoku.database

import com.example.sudoku.database.entity.GameState
import com.example.sudoku.model.data.TableType
import com.example.sudoku.model.data.table.Table

interface DatabaseRepository {
    fun getTable(tableType: TableType): Table
    fun insertTable(table: Table)
    fun loadGameState(): GameState?
    fun saveGameState(gameState: GameState)
    fun deleteGameState()
}