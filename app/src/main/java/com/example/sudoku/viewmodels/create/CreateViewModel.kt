package com.example.sudoku.viewmodels.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sudoku.model.data.Table
import com.example.sudoku.model.TableRepository
import com.example.sudoku.utility.get

class CreateViewModel(tableRepository: TableRepository) : ViewModel() {

    private val tables = MutableLiveData<Table>()

    private val currentTable: LiveData<Table>
        get() = tables

    private var delete = false

    fun changeDelete() {
        delete = !delete
        updateTable()
    }

    fun hideNumber(row: Int, coloumn: Int) {
        println("HIDE!! $row $coloumn")
        tables.value!!.cells[row, coloumn].hideNumbers()
        updateTable()
    }

    fun isDelete(): Boolean {
        return delete
    }

    fun writeAnswer(index: Pair<Int, Int>, number: Int): Pair<Boolean, Boolean> {
        return tables.value!!.writeAnswer(index, number).also { updateTable() }
    }

    private fun updateTable() {
        tables.value = tables.value
    }

    fun saveTable(){

    }

}
