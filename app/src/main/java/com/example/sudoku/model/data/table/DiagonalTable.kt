package com.example.sudoku.model.data.table

import com.example.sudoku.utility.forEachCellIndexed
import java.util.*
import kotlin.random.Random

class DiagonalTable : Table {

    constructor(
        id: String = UUID.randomUUID().toString(),
        cells: Array<Array<Cell>>
    ) : super(id, cells)

    constructor(
        id: String = UUID.randomUUID().toString(),
        solutionArray: IntArray,
        givenNumbers: BooleanArray
    ) : super(
        id,
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
