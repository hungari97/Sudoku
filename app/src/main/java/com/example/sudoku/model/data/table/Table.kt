package com.example.sudoku.model.data.table

import com.example.sudoku.utility.forEachCell
import com.example.sudoku.utility.forEachCellIndexed
import com.example.sudoku.utility.get
import java.util.*

sealed class Table(
    val id: String = UUID.randomUUID().toString(),
    var cells: Array<Array<Cell>>
) {
    constructor(
        id: String = UUID.randomUUID().toString(),
        solutionArray: IntArray,
        givenNumbers: BooleanArray,
        vararg groups: BooleanArray
    ) : this(
        id,
        Array(9) { rowIndex ->
            Array(9) { columnIndex ->
                Cell(
                    solutionNumber = solutionArray[to1DCoordinate(rowIndex, columnIndex)],
                    given = givenNumbers[to1DCoordinate(rowIndex, columnIndex)],
                    groupFlags = BooleanArray(groups.size) {
                        groups[it][to1DCoordinate(rowIndex, columnIndex)]
                    }
                )
            }
        }) {
        initializeTable()
    }

    companion object {
        private fun to1DCoordinate(rowIndex: Int, columnIndex: Int) = rowIndex * 9 + columnIndex
    }

    private fun initializeTable() {
        cells.forEachCellIndexed { rowIndex, columnIndex, cell ->
            if (cell.isChosenNumberCorrect()) {
                removeTipsPossibilities(
                    Pair(rowIndex, columnIndex),
                    cell.chosenNumber,
                    cell.isChosenNumberCorrect()
                )
            }
        }
    }

    fun writeTip(spot: Pair<Int, Int>, value: Int): Pair<Boolean, Boolean> {
        return Pair(false, cells[spot].writeTip(value))
    }

    fun writeAnswer(spot: Pair<Int, Int>, value: Int): Pair<Boolean, Boolean> {
        cells[spot].writeAnswer(value)
        removeTipsPossibilities(spot, value, cells[spot].isChosenNumberCorrect())
        return Pair(
            true,
            cells[spot].isChosenNumberCorrect()
        )
    }

    fun isFinished(): Boolean {
        cells.forEachCell { cell ->
            if (!cell.isChosenNumberCorrect()) {
                return false
            }
        }
        return true
    }

    open fun removeTipsPossibilities(
        place: Pair<Int, Int>,
        number: Int,
        correct: Boolean
    ) {
        //Row
        cells[place.first].forEachIndexed { columnIndex, cell ->
            if (columnIndex == place.second)
                return@forEachIndexed

            if (correct) {
                cell.allPossibilities[number - 1] = false   //kiveszi ha az jó volt
            }
            cell.shownPossibilities[number - 1] = false

        }

        //Column
        for (rowIndex in 0..8) {
            if (rowIndex == place.first)
                continue

            if (correct) {
                cells[rowIndex, place.second].allPossibilities[number - 1] = false
            }
            cells[rowIndex, place.second].shownPossibilities[number - 1] = false
        }

        //Box
        val positionInSection = Pair(place.first % 3, place.second % 3)

        for (rowIndexInSection: Int in 0..2) {
            for (columnIndexInSection: Int in 0..2) {
                if ((place.first == rowIndexInSection && place.second == columnIndexInSection))
                    continue

                val rowIndex = (place.first - positionInSection.first) + rowIndexInSection
                val columnIndex = (place.second - positionInSection.second) + columnIndexInSection

                if (correct) {
                    cells[rowIndex][columnIndex].allPossibilities[number - 1] = false
                }
                cells[rowIndex][columnIndex].shownPossibilities[number - 1] = false
            }
        }
    }

    fun showAllPossibilities() {
        cells.forEachCell { cell ->
            cell.allPossibilities.forEachIndexed { index, value ->
                cell.shownPossibilities[index] = value
            }
        }
    }

}