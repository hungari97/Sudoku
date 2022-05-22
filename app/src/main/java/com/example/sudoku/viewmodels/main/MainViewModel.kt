package com.example.sudoku.viewmodels.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sudoku.model.TableRepository
import com.example.sudoku.model.data.SelectedGameFunction
import com.example.sudoku.model.data.SelectedGameFunction.*
import com.example.sudoku.model.data.TableType
import com.example.sudoku.model.data.table.Table
import com.example.sudoku.utility.get
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

class MainViewModel(private val tableRepository: TableRepository) : ViewModel() {
    private val tableMutable = MutableLiveData<Table>()
    private val countMutable = MutableLiveData(0)
    var selectedFunction: SelectedGameFunction = NONE
    var selectedNumberIndex: Int? = null
    private var remainingHelpCount = 3
    private lateinit var timer: Timer

    val table: LiveData<Table>
        get() = tableMutable
    val count: LiveData<Int>
        get() = countMutable

    fun switchFunction(function: SelectedGameFunction) {
        if (selectedFunction == function) {
            selectedFunction = NONE
            return
        }
        selectedFunction = function
    }

    fun cancelTimer() {
        timer.cancel()
    }

    fun startTimeCounter() {
        timer = Timer()
        timer.scheduleAtFixedRate(1000, 1000) {
            viewModelScope.launch {
                countMutable.value = countMutable.value!! + 1
            }
        }
    }

    fun saveGameState() {
        CoroutineScope(Dispatchers.IO).launch {
            tableRepository.saveGame(tableMutable.value!!, remainingHelpCount, count.value!!)
        }
    }

    fun loadPreviousGameState() = CoroutineScope(Dispatchers.IO).launch {
        tableRepository.loadGameOrNull()?.let {
            val (helpCount, secondsPassed, table) = it
            remainingHelpCount = helpCount
            viewModelScope.launch {
                countMutable.value = secondsPassed
                tableMutable.value = table
            }
        }
    }

    fun clearGameState() = CoroutineScope(Dispatchers.IO).launch {
        tableRepository.clearGameState()
    }

    fun isTableFinished(): Boolean {
        return table.value!!.isFinished()
    }

    fun showAllPossibilities() {
        tableMutable.value?.showAllPossibilities()
        updateTable()
    }

    fun isGivenNumber(row: Int, column: Int): Boolean {
        return tableMutable.value!!
            .cells[row, column]
            .given
    }

    private fun updateTable() {
        tableMutable.value = tableMutable.value
    }

    fun loadNewTable(mode: TableType) = CoroutineScope(Dispatchers.IO).launch {
        setTable(tableRepository.getTable(mode))
    }

    private fun setTable(table: Table) = viewModelScope.launch { tableMutable.value = table }

    fun selectPosition(rowIndex: Int, columnIndex: Int) {
        if (isGivenNumber(rowIndex, columnIndex))
            return

        when (selectedFunction) {
            GIVE_ANSWER -> {
                givePossibleTip(Pair(rowIndex, columnIndex))
            }
            DELETION -> {
                hideNumber(rowIndex, columnIndex)
            }
            PENCIL -> {
                writeTip(
                    Pair(rowIndex, columnIndex),
                    (selectedNumberIndex ?: -1) + 1
                )
            }
            NONE -> {
                writeAnswer(
                    Pair(rowIndex, columnIndex),
                    (selectedNumberIndex ?: -1) + 1
                )
            }
        }

    }

    private fun givePossibleTip(pair: Pair<Int, Int>) {
        selectedFunction = NONE
        if (remainingHelpCount > 0) {
            --remainingHelpCount
            tableMutable.value!!.cells[pair].givePossibility()
        }
        updateTable()
    }

    private fun hideNumber(row: Int, column: Int) {
        selectedNumberIndex = null
        tableMutable.value!!.cells[row, column].hideNumbers()
        updateTable()
    }

    private fun writeAnswer(index: Pair<Int, Int>, number: Int) {
        if (selectedNumberIndex == null)
            return

        tableMutable.value?.writeAnswer(index, number)
        updateTable()
    }

    private fun writeTip(index: Pair<Int, Int>, number: Int) {
        if (selectedNumberIndex == null)
            return

        tableMutable.value?.writeTip(index, number)
        updateTable()
    }

    fun isInGroup(row: Int, column: Int, groupIndex: Int): Boolean {
        return tableMutable.value!!.cells[row, column].groupFlags[groupIndex]
    }
}