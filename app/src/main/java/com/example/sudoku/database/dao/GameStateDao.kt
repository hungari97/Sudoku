package com.example.sudoku.database.dao

import androidx.room.*
import com.example.sudoku.database.entity.GameState

@Dao
interface GameStateDao {
    @Query("SELECT * FROM gameState")
    fun getAll(): List<GameState>

    @Query("SELECT * FROM gameState WHERE id = :id")
    fun getById(id: String): GameState

    @Query("SELECT EXISTS(SELECT * FROM gameState WHERE id = :id)")
    fun isPresentById(id: String): Boolean

    @Insert
    fun insert(gameState: GameState)

    @Update
    fun update(gameState: GameState)

    @Delete
    fun delete(gameState: GameState)
}