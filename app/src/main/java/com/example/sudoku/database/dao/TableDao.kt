package com.example.sudoku.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.sudoku.database.entity.TableItem

@Dao
interface TableDao {
    @Query("SELECT * FROM tables WHERE refNumber LIKE :number")
    fun getTableByNum(number:String): TableItem

    @Insert
    fun insertItem(vararg item: TableItem)

    @Query("SELECT * FROM tables")
    fun getAll():List<TableItem>

    @Delete
    fun deleteTable(table: TableItem)

}