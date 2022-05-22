package com.example.sudoku.model.data

import com.example.sudoku.model.data.table.DiagonalTable
import com.example.sudoku.model.data.table.NormalTable
import com.example.sudoku.model.data.table.OddEvenTable
import com.example.sudoku.model.data.table.Table

enum class TableType {
    NORMAL,
    DIAGONAL,
    ODD_EVEN;

    companion object {
        fun fromTableType(table: Table) =
            when (table) {
                is NormalTable -> NORMAL
                is DiagonalTable -> DIAGONAL
                is OddEvenTable -> ODD_EVEN
            }
    }
}