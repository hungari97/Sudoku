package com.example.sudoku.model.data

import com.example.sudoku.utility.forEachCellIndexed

class DiagonalTable constructor(
    private val solutionArray: IntArray, givenNumbers: BooleanArray,
    private val referenceID: String = "D_0_"
) : Table(solutionArray, givenNumbers) {

    /*constructor(table: Table) :this(table){
        this.solutionArray=table.getSolutionArray()
        givenNumbers =table.givenNumbers
        referenceID = table.getRefence()
    }*/

    override fun initializeTable() {

    }

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
