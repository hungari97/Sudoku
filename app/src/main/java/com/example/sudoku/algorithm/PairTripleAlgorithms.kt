package com.example.sudoku.algorithm

import com.example.sudoku.model.data.Table

class PairTripleAlgorithms(private var table: Table) {


    fun nakedRowPair(checkedRow: Int) {

        var tempPossibilities = Array(9) { 0 }
        table.cells[checkedRow].forEach { cell ->
            cell.allPossibilities.forEachIndexed { index, possibility ->
                if (possibility)
                    tempPossibilities[index]++
            }
        }

        if (tempPossibilities.count { it == 2 } >= 2) {
            var twoPossibilities =
                arrayOf(tempPossibilities.forEachIndexed { index, i ->
                    if (i == 2) {
                        index
                    }
                })


        }
    }
}