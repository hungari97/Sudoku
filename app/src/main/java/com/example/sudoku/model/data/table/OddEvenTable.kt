package com.example.sudoku.model.data.table

import com.example.sudoku.utility.get
import com.example.sudoku.utility.isEven
import java.util.*

class OddEvenTable : Table {
    constructor(
        id: String = UUID.randomUUID().toString(),
        cells: Array<Array<Cell>>
    ) : super(id, cells)

    constructor(
        id: String = UUID.randomUUID().toString(),
        solutionArray: IntArray,
        givenNumbers: BooleanArray,
        oddEvenGroup: BooleanArray
    ) : super(
        id,
        solutionArray,
        givenNumbers,
        oddEvenGroup
    )


    override fun removeTipsPossibilities(place: Pair<Int, Int>, number: Int, correct: Boolean) {
        super.removeTipsPossibilities(place, number, correct)
        val cell = cells[place]
        if (!cell.groupFlags[0])
            return

        val otherCellsInGroup = cells
            .flatten()
            .filter { it.groupFlags[0] && it != cell }

        otherCellsInGroup.forEach {
            if (correct)
                (1..9).forEach { numberInner ->
                    if (numberInner.isEven() != number.isEven())
                        it.allPossibilities[numberInner - 1] = false
                }

            (0 until 9).forEach { numberInner ->
                if (numberInner.isEven() != number.isEven())
                    it.shownPossibilities[numberInner - 1] = false
            }
        }
    }
}