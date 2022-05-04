package com.example.sudoku.database.tableitem

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tables")
data class TableItem(
    @PrimaryKey(autoGenerate = true) val tableId: Long,
    @ColumnInfo(name = "refNumber") val referenceNumber: String,
    @ColumnInfo(name = "content") val tableArray: String
)
