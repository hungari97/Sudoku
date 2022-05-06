package com.example.sudoku.model.data

import com.example.sudoku.utility.forEachCellIndexed

class DiagonalTable : Table {

    constructor(
        reference: String = "D_0_",
        cells: Array<Array<Cell>>
    ) : super(reference, cells)

    constructor(
        reference: String = "D_0_",
        solutionArray: IntArray,
        givenNumbers: BooleanArray
    ) : super(
        reference,
        solutionArray,
        givenNumbers)

    override fun removeTipsPossibilities(place: Pair<Int, Int>, number: Int, correct: Boolean) {
        super.removeTipsPossibilities(place, number, correct)
        //Diagonal
        if (place.first == place.second || place.first + place.second == 9) {
            cells.forEachCellIndexed { rowIndex, columnIndex, cell ->
                if (rowIndex == columnIndex || rowIndex + columnIndex == 9) {
                    if ((place.first != rowIndex || place.second != columnIndex)) {
                        if (correct)
                            cell.allPossibilities[number - 1] = false   //kiveszi ha az jó volt
                        cell.shownPossibilities[number - 1] = false
                    } else {
                        if (correct)
                            cell.allPossibilities[number - 1] = true
                        cell.shownPossibilities[number - 1] = true
                    }
                }

            }
        }
    }

}
