package com.example.sudoku.viewmodels.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sudoku.model.data.table.Table
import com.example.sudoku.model.TableRepository
import com.example.sudoku.model.data.TableType
import com.example.sudoku.utility.forEachCellIndexed
import com.example.sudoku.utility.get
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateViewModel(private val tableRepository: TableRepository) : ViewModel() {
    private var remainingNumbersCounter = intArrayOf(9, 9, 9, 9, 9, 9, 9, 9)
    private val tables = MutableLiveData<Table>()

    val currentTable: LiveData<Table>
        get() = tables

    private var delete = false

    fun changeDelete() {
        delete = !delete
        updateTable()
    }

    fun getRemainingNumbers(): IntArray {
        return remainingNumbersCounter
    }

    fun decreseRemainingNumber(number: Int) {
        remainingNumbersCounter[number]--
    }

    fun numberDeletedRemainingUpdate() {
        remainingNumbersCounter = intArrayOf(9, 9, 9, 9, 9, 9, 9, 9)
        tables.value!!.cells.forEachCellIndexed { rowIndex, cellIndex, cell ->
            remainingNumbersCounter[cell.chosenNumber - 1] =
                remainingNumbersCounter[cell.chosenNumber - 1]--
        }
    }

    fun NumberRemaining(number: Int): Int {
        return remainingNumbersCounter[number]
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

    fun saveTable() {
        if (checkTable()) {
            CoroutineScope(Dispatchers.IO).launch {
                tableRepository.createTable(tables.value!!)
            }
        }
    }

    private fun checkTable(): Boolean {
        //Cheking cells and rows
        var checkingArray = arrayListOf<Int>()
        tables.value!!.cells.forEachIndexed { index, row ->
            row.forEach {
                if (9 < it.chosenNumber || it.chosenNumber < 1 || checkingArray.contains(it.chosenNumber))
                    return false

                checkingArray.add(it.chosenNumber)
            }
            checkingArray = arrayListOf()
        }

        //Checking columns
        for (rowIndex in 0..8) {
            for (columnIndex in 0..8) {
                if (checkingArray.contains(tables.value!!.cells[rowIndex, columnIndex].chosenNumber)) {
                    return false
                }
            }
            checkingArray = arrayListOf()
        }

        //Checking boxes
        var positionInSection: Pair<Int, Int>

        for (section in 0..8) {
            positionInSection = Pair(
                ((section * 3) / 9) * 3,
                (section % 3) * 3
            )
            for (rowIndex: Int in 0..2) {
                for (columnIndex: Int in 0..2) {
                    if (checkingArray.contains(tables.value!!.cells[positionInSection.first + rowIndex, positionInSection.second + columnIndex].chosenNumber)) {
                        return false
                    }
                    checkingArray.add(tables.value!!.cells[positionInSection.first + rowIndex, positionInSection.second + columnIndex].chosenNumber)
                }
            }
            checkingArray = arrayListOf()
        }
        return true
    }

    fun getInitialiseTable(mode: TableType) =
        CoroutineScope(Dispatchers.IO).launch { setTable(tableRepository.getBlankTable(mode)) }

    private fun setTable(table: Table) = viewModelScope.launch { tables.value = table }
}

