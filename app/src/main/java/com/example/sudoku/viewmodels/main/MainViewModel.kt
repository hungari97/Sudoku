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
import java.util.*

class MainViewModel(private val tableRepository: TableRepository) : ViewModel() {
    private val tableMutable = MutableLiveData<Table>()
    private val countMutable = MutableLiveData(0)
    private var giveAnswer = false
    private var pencil = false
    private var delete = false
    private var remainingHelpCount = 3
    private lateinit var timer: Timer

    val table: LiveData<Table>
        get() = tableMutable
    val count: LiveData<Int>
        get() = countMutable

    fun cancelTimer() {
        timer.cancel()
    }

    fun startTimeCounter() {
        timer = Timer()
        countMutable.value = 0
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                viewModelScope.launch {
                    countMutable.value = countMutable.value!! + 1
                }
            }
        }, 1000, 1000)
    }

    fun saveGameState() {
        CoroutineScope(Dispatchers.IO).launch {
            tableRepository.saveGame(tableMutable.value!!, remainingHelpCount, count.value!!)
        }
    }

    fun loadGameState() {
        CoroutineScope(Dispatchers.IO).launch {
            tableRepository.loadGameOrNull()?.let {
                val (helpCount, secondsPassed, table) = it
                remainingHelpCount = helpCount
                viewModelScope.launch {
                    countMutable.value = secondsPassed
                    tableMutable.value = table
                }
            }
        }
    }

    fun isTableFinished(): Boolean {
        return table.value!!.isFinished()
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
        tableMutable.value?.showAllPossibilities()
        updateTable()
    }

    fun hideNumber(row: Int, column: Int) {
        tableMutable.value!!.cells[row, column].hideNumbers()
        updateTable()
    }

    fun givenNumber(row: Int, column: Int): Boolean {
        return tableMutable.value!!
            .cells[row, column]
            .given
            .also { updateTable() }
    }

    fun writeAnswer(index: Pair<Int, Int>, number: Int) {
        giveAnswer = false

        if (pencil) {
            tableMutable.value!!.writeTip(index, number)
        } else {
            tableMutable.value!!.writeAnswer(index, number)
        }

        updateTable()
    }

    private fun updateTable() {
        tableMutable.value = tableMutable.value
    }

    fun askPossibleTip() {
        giveAnswer = !giveAnswer
    }

    fun givePossibleTip(pair: Pair<Int, Int>) {
        giveAnswer = false
        if (remainingHelpCount > 0) {
            --remainingHelpCount
            tableMutable.value!!.cells[pair].givePossibility()
        }
        updateTable()
    }

    fun getInitialiseTable(mode: TableType) =
        CoroutineScope(Dispatchers.IO).launch { setTable(tableRepository.getTable(mode)) }

    private fun setTable(table: Table) = viewModelScope.launch { tableMutable.value = table }
}