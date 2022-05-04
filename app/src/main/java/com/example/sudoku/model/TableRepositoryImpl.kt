package com.example.sudoku.model

import com.example.sudoku.model.data.TableType
import com.example.sudoku.model.data.ModelData
import com.example.sudoku.model.data.Table

class TableRepositoryImpl(private val data: ModelData) : TableRepository {
    override fun getTable(mode: TableType): Table {
        return data.getTable(mode)
    }

}