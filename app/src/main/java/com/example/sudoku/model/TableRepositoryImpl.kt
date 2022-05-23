package com.example.sudoku.model

import com.example.sudoku.model.data.GameStateDto
import com.example.sudoku.model.data.TableType
import com.example.sudoku.model.data.ModelData
import com.example.sudoku.model.data.table.Table

class TableRepositoryImpl(private val data: ModelData) : TableRepository {
    override fun getTable(mode: TableType): Table {
        return data.getTable(mode)
    }

    override fun loadGameOrNull(): GameStateDto? {
        return data.loadGameStateOrNull()
    }

    override fun saveGame(table: Table, remainingHelp: Int, secondsPassed: Int){
        data.saveGameState(table,remainingHelp,secondsPassed)
    }

    override fun createTable(table: Table) {
        TODO("Not yet implemented")
    }

    override fun getBlankTable(mode: TableType): Table {
        return data.getClearTable(mode)
    }

    override fun clearGameState() {
        data.clearGameState()
    }

}