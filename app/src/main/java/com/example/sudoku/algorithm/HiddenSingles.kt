package com.example.sudoku.algorithm

import com.example.sudoku.model.data.Table
import com.example.sudoku.utility.forEachCellIndexed
import com.example.sudoku.utility.get
import com.example.sudoku.utility.plus

class HiddenSingles(private var table: Table) {
    fun hiddenSingleInRow(checkedRow: Int) {
        val tempPossibilities = Array(9) { 0 }
        table.cells[checkedRow].forEach { cell ->
            cell.allPossibilities.forEachIndexed { index, possibility ->
                if (possibility)
                    tempPossibilities[index]++
            }
        }

        if (tempPossibilities.contains(1)) {
            tempPossibilities.forEachIndexed { index, sum ->
                if (sum == 1 && table.cells[checkedRow, index].checkChosenNumber()) {
                    table.cells[checkedRow, index].reduceAllPossibilities(index + 1)
                }
            }
        }
    }

    fun hiddenSingleInColumn(checkedColumn: Int) {
        var tempPossibilities = Array(9) { 0 }
        table.cells.forEach { row ->
            row[checkedColumn].allPossibilities.forEachIndexed { index, possibility ->
                if (possibility)
                    tempPossibilities[index]++
            }
        }

        if (tempPossibilities.contains(1)) {
            tempPossibilities.forEachIndexed { index, sum ->
                if (sum == 1 && table.cells[index, checkedColumn].checkChosenNumber()) {
                    table.cells[index, checkedColumn].reduceAllPossibilities(index + 1)
                }
            }
        }
    }

    fun hiddenSingleInBox(checkedBox: Int) {
        var tempPossibilities = Array(9) { 0 }
        val start = when (checkedBox) {
            1 -> Pair(0, 0)
            2 -> Pair(0, 3)
            3 -> Pair(0, 6)
            4 -> Pair(3, 0)
            5 -> Pair(3, 3)
            6 -> Pair(3, 6)
            7 -> Pair(6, 0)
            8 -> Pair(6, 3)
            9 -> Pair(6, 6)
            else -> throw Exception()
        }

        for (rowindex: Int in 0..2) {
            for (columnindex: Int in 0..2) {
                table.cells[start + Pair(
                    rowindex,
                    columnindex
                )].allPossibilities.forEachIndexed { index, possibility ->
                    if (possibility)
                        tempPossibilities[index]++
                }
            }
        }

        if (tempPossibilities.contains(1)) {
            tempPossibilities.forEachIndexed { index, sum ->

                if (sum == 1) {
                    for (rowindex: Int in 0..2) {
                        for (columnindex: Int in 0..2) {
                            if (table.cells[start + Pair(
                                    rowindex, columnindex
                                )].allPossibilities[index] && table.cells[start + Pair(
                                    rowindex, columnindex
                                )].checkChosenNumber()
                            ) {
                                table.cells[start + Pair(
                                    rowindex, columnindex
                                )].reduceAllPossibilities(index + 1)

                            }
                        }
                    }
                }

            }
        }
    }

    fun writeAllSingles() {
        table.cells.forEachCellIndexed { rowIndex, columnIndex, column ->
            if (column.allPossibilities.count { it } == 1 && !column.checkChosenNumber()) {
                table.writeAnswer(
                    Pair(rowIndex, columnIndex),
                    column.allPossibilities.indexOf(true) + 1
                )
            }
        }
    }
}