package com.example.sudoku.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.sudoku.database.entity.TableItem

@Dao
interface TableItemDao {
    @Insert
    fun insertItem(vararg item: TableItem)

    @Query("SELECT * FROM tables WHERE id LIKE :id")
    fun getTableByNum(id: Long): TableItem

    @Query("SELECT * FROM tables WHERE type LIKE :type")
    fun getTableByType(type: String): List<TableItem>

    @Query("SELECT * FROM tables")
    fun getAll(): List<TableItem>

    @Query("DELETE FROM tables WHERE id = :id")
    fun deleteTable(id: String)

}