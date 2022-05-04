package com.example.sudoku.viewmodels.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sudoku.model.data.TableType
import com.example.sudoku.model.data.Table
import com.example.sudoku.model.TableRepository
import com.example.sudoku.utility.get
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val tableRepository: TableRepository) : ViewModel() {
    private val tables = MutableLiveData<Table>()
    private var giveAnswer = false
    private var pencil = false
    private var delete = false
    private var giveAnswerCount = 3

    val currentTable: LiveData<Table>
        get() = tables

    fun isTableFinished(): Boolean {
        return currentTable.value!!.isFinished()
    }

    fun isDelete(): Boolean {
        return delete
    }

    fun changeDelete() {
        delete = !delete
    }

    fun isPencil(): Boolean {
        return pencil
    }

    fun changePencil() {
        pencil = !pencil
    }

    fun isGiveAnswer(): Boolean {
        return giveAnswer
    }

    fun showAllPossibilities() {
        tables.value?.showAllPossibilities()
        updateTable()
    }

    fun hideNumber(row: Int, column: Int) {
        tables.value!!.cells[row, column].hideNumbers()
        updateTable()
    }

    fun givenNumber(index: Int): Boolean {
        return tables.value!!.givenNumbers[index].also { updateTable() }
    }

    fun writeAnswer(index: Pair<Int, Int>, number: Int) {
        giveAnswer = false

        if (pencil) {
            tables.value!!.writeTip(index, number)
        } else {
            tables.value!!.writeAnswer(index, number)
        }

        updateTable()
    }

    private fun updateTable() {
        tables.value = tables.value
    }

    fun askPossibleTip() {
        giveAnswer = !giveAnswer
    }

    fun givePossibleTip(pair: Pair<Int, Int>) {
        giveAnswer = false
        if (giveAnswerCount > 0) {
            --giveAnswerCount
            tables.value!!.cells[pair].givePossibility()
        }
        updateTable()
    }

    fun getInitialiseTable(mode: TableType) =
        CoroutineScope(Dispatchers.IO).launch { setTable(tableRepository.getTable(mode)) }

    private fun setTable(table: Table) = viewModelScope.launch { tables.value = table }
}