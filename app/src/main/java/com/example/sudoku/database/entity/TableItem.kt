package com.example.sudoku.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.sudoku.model.data.TableType

@Entity(tableName = "tables")
data class TableItem(
    @PrimaryKey() val id: String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "content") val content: String
)
