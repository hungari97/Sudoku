package com.example.sudoku.database

import com.example.sudoku.database.entity.GameState
import com.example.sudoku.model.data.TableType
import com.example.sudoku.model.data.Table

object DBConnector : DatabaseRepository {
    private val dbimpl by lazy { DatabaseRepositoryImpl() }
    private var localDB = mutableListOf<Table>()

    init {
    }

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
                TableType.NORMAL -> {
                    it.reference.startsWith("N_")
                }
                TableType.DIAGONAL -> {
                    it.reference.startsWith("D_")
                }
            }
        }.random()


    }

    override fun insertTable(item: Table) {
        dbimpl.insertTable(item)
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