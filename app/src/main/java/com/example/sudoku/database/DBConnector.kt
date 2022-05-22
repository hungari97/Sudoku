package com.example.sudoku.database

import com.example.sudoku.database.entity.GameState
import com.example.sudoku.model.data.TableType
import com.example.sudoku.model.data.table.DiagonalTable
import com.example.sudoku.model.data.table.OddEvenTable
import com.example.sudoku.model.data.table.Table

object DBConnector : DatabaseRepository {
    private val dbimpl by lazy { DatabaseRepositoryImpl() }
    private var localDB = mutableListOf<Table>()

    override fun getTable(tableType: TableType): Table {
        /*
        if (mode.isNullOrEmpty()) {
            return Table0Initialize.Normal.create0Table()
        }
         */

        if (localDB.isEmpty()) {
            localDB = dbimpl.getAllTable().toMutableList()
        }

        /*
        if (mode == Mode.SAVED){
            return saved
        }
         */

        return localDB.filter {
            when (tableType) {
                TableType.DIAGONAL -> it is DiagonalTable
                TableType.ODD_EVEN -> it is OddEvenTable
                TableType.NORMAL -> it !is DiagonalTable && it !is OddEvenTable
            }
        }.random()


    }

    override fun insertTable(table: Table) {
        dbimpl.insertTable(table)
    }

    override fun loadGameState(): GameState? {
        return dbimpl.loadGameState()
    }

    override fun saveGameState(gameState: GameState) {
        dbimpl.saveGameState(gameState)
    }

    override fun deleteGameState() {
        dbimpl.deleteGameState()
    }

    fun saveFirebase() {
        TODO("Not yet implemented")
    }

    //TODO Firebase
}