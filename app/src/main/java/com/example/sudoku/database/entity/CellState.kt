package com.example.sudoku.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "cellState",
    foreignKeys = [ForeignKey(
        entity = TableState::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("tableId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class CellState(
    @PrimaryKey() val id: String = UUID.randomUUID().toString(),
    val solutionNumber: Int,
    val given: Boolean,
    val groupFlagsEncoded:String,
    var allPossibilitiesEncoded: String,
    var shownPossibilitiesEncoded: String,
    var chosenNumber: Int
) {
    var tableId: String = ""
    var index: Int = 0
}
