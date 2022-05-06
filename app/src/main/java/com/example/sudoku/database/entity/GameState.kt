package com.example.sudoku.database.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "gameState")
data class GameState(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val remainingHelpCount: Int,
    val secondsPassed: Int
) {
    @Ignore
    var table: TableState? = null
        set(value) {
            value?.gameStateId = id
            field = value
        }
}