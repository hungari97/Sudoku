package com.example.sudoku.database.dao

import androidx.room.*
import com.example.sudoku.database.entity.TableState

@Dao
interface TableStateDao {
    @Query("SELECT * FROM tableState")
    fun getAll(): List<TableState>

    @Query("SELECT * FROM tableState WHERE id = :id")
    fun getById(id: String): TableState

    @Query("SELECT EXISTS(SELECT * FROM tableState WHERE id = :id)")
    fun isPresentById(id: String): Boolean

    @Insert
    fun insert(tableState: TableState)

    @Update
    fun update(tableState: TableState)

    @Delete
    fun delete(tableState: TableState)

    @Query("SELECT * FROM tableState WHERE gameStateId = :gameStateId ")
    fun getByGameStateId(gameStateId: String): TableState?
}