package com.example.sudoku.model.data.table

import java.util.*
import kotlin.random.Random

class NormalTable : Table {
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
        givenNumbers
    )
}