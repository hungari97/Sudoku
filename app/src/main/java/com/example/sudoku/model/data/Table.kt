package com.example.sudoku.model.data

import com.example.sudoku.utility.*

open class Table(
    private val solutionArray: IntArray, open var givenNumbers: BooleanArray,
    private val referenceID: String = "N_0_"
) {
    var cells: Array<Array<Cell>> = Array(9) { rowIndex ->
        Array(9) { columnIndex ->
            Cell(
                solutionNumber = solutionArray[rowIndex * 9 + columnIndex],
                given = givenNumbers[rowIndex * 9 + columnIndex]
            )
        }
    }


    init {
        initializeTable()
    }

    internal open fun initializeTable() {
        cells.forEachCellIndexed { rowIndex, columnIndex, cell ->
            if (cell.checkChosenNumber()) {
                removeTipsPossibilities(
                    Pair(rowIndex, columnIndex),
                    cell.chosenNumber,
                    cell.checkChosenNumber()
                )
            }
        }
    }

    fun writeTip(spot: Pair<Int, Int>, value: Int): Pair<Boolean, Boolean> {
        return Pair(false, cells[spot].writeTip(value))
    }

    fun writeAnswer(spot: Pair<Int, Int>, value: Int): Pair<Boolean, Boolean> {
        cells[spot].writeAnswer(value)
        removeTipsPossibilities(spot, value, cells[spot].checkChosenNumber())
        return Pair(
            true,
            cells[spot].checkChosenNumber()
        )
    }

    fun isFinished(): Boolean {
        cells.forEachCell { cell ->
            if (!cell.checkChosenNumber()) {
                return false
            }
        }
        return true
    }

    internal open fun removeTipsPossibilities(
        place: Pair<Int, Int>,
        number: Int,
        correct: Boolean
    ) {
        //Row
        cells[place.first].forEachIndexed { columnIndex, cell ->
            if (columnIndex != place.second) {
                if (correct) {
                    cell.allPossibilities[number - 1] = false   //kiveszi ha az jó volt
                }
                cell.shownPossibilities[number - 1] = false
            } else {
                if (correct) {
                    cell.allPossibilities[number - 1] = true
                }
                cell.shownPossibilities[number - 1] = true
            }
        }

        //Column
        for (rowIndex in 0..8) {
            if (rowIndex != place.first) {
                if (correct) {
                    cells[rowIndex, place.second].allPossibilities[number - 1] = false
                }
                cells[rowIndex, place.second].shownPossibilities[number - 1] = false
            } else {
                if (correct) {
                    cells[rowIndex, place.second].allPossibilities[number - 1] = true
                }
                cells[rowIndex, place.second].shownPossibilities[number - 1] = true
            }
        }

        //Box
        val positionInSection = Pair(place.first % 3, place.second % 3)

        for (rowIndex: Int in 0..2) {
            for (columnIndex: Int in 0..2) {
                if ((place.first != rowIndex || place.second != columnIndex)) {
                    if (correct) {
                        cells[(place.first - positionInSection.first) + rowIndex][(place.second - positionInSection.second) + columnIndex].allPossibilities[number - 1] =
                            false
                    }
                    cells[(place.first - positionInSection.first) + rowIndex][(place.second - positionInSection.second) + columnIndex].shownPossibilities[number - 1] =
                        false
                } else {
                    if (correct) {
                        cells[(place.first - positionInSection.first) + rowIndex][(place.second - positionInSection.second) + columnIndex].allPossibilities[number - 1] =
                            true
                    }
                    cells[(place.first - positionInSection.first) + rowIndex][(place.second - positionInSection.second) + columnIndex].shownPossibilities[number - 1] =
                        true
                }
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

    fun getRefence(): String {
        return referenceID
    }

    internal fun getSolutionArray(): IntArray {
        return solutionArray
    }

}