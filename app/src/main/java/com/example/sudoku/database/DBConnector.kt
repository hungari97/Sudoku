package com.example.sudoku.database

import com.example.sudoku.model.data.TableType
import com.example.sudoku.model.data.Table

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
                TableType.NORMAL -> {
                    it.getRefence().startsWith("N_")
                }
                TableType.DIAGONAL -> {
                    it.getRefence().startsWith("D_")
                }
            }
        }.random()

    }

    fun hasSaved(): Boolean {
        localDB.forEach {
            if (it.getRefence() == "saved") {
                return true
            }
        }
        return false
    }

    private val saved: Table
        get() {
            return localDB.filter {
                it.getRefence().startsWith("saved")
            }.random()
        }

    override fun insertTable(item: Table) {
        dbimpl.insertTable(item)
    }

    /*
    fun getNextIdNumber(): Int {
        return 0
    }

     */

    //TODO Firebase
}