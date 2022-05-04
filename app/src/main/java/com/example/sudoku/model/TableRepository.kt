package com.example.sudoku.model

import com.example.sudoku.model.data.TableType
import com.example.sudoku.model.data.Table

interface TableRepository {
    fun getTable(mode: TableType): Table
}