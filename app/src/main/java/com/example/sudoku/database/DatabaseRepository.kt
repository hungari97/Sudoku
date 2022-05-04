package com.example.sudoku.database

import com.example.sudoku.model.data.TableType
import com.example.sudoku.model.data.Table

interface DatabaseRepository {
    fun getTable(tableType: TableType): Table
    fun insertTable(item: Table)
}