package com.example.sudoku.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "tableState")
data class TableState(
    @PrimaryKey() val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "tableType") val tableType: String,
) {
    @Ignore
    var cells: Array<CellState>? = null
        set(value) {
            value?.forEachIndexed { index, cell ->
                cell.tableId = id
                cell.index = index
            }
            field = value
        }

    var gameStateId: String = ""
}
