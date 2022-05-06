package com.example.sudoku.database.dao

import androidx.room.*
import com.example.sudoku.database.entity.CellState

@Dao
interface CellStateDao {
    @Query("SELECT * FROM cellState")
    fun getAll(): List<CellState>

    @Query("SELECT * FROM cellState WHERE id = :id")
    fun getById(id: String): CellState

    @Query("SELECT EXISTS(SELECT * FROM cellState WHERE id = :id)")
    fun isPresentById(id: String): Boolean

    @Insert
    fun insert(cellState: CellState)

    @Update
    fun update(cellState: CellState)

    @Delete
    fun delete(cellState: CellState)

    @Query("SELECT * FROM cellState WHERE tableId = :tableStateId ")
    fun getByTableStateId(tableStateId: String): List<CellState>
}