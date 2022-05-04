package com.example.sudoku.utility

operator fun <T> Array<Array<T>>.get(row: Int, column: Int): T = this[row][column]

operator fun <T> Array<Array<T>>.get(place: Pair<Int, Int>): T = this[place.first][place.second]

inline fun <T> Array<Array<T>>.forEachCellIndexed(functionality: (rowIndex: Int, cellIndex: Int, value: T) -> Unit) =
    forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, value ->
            functionality(rowIndex, columnIndex, value)
        }
    }

inline fun <T> Array<Array<T>>.forEachCell(functionality: (value: T) -> Unit):Unit =
    forEach { row ->
        row.forEach { value ->
            functionality(value)
        }
    }

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> =
    Pair(this.first + other.first, this.second + other.second)

inline fun <reified T> Array<T>.toMatrix(rowLength: Int): Array<Array<T>> =
    Array(this.size / rowLength) { rowIndex ->
        Array(rowLength) { columnIndex ->
            this[rowLength * rowIndex + columnIndex]
        }

    }

fun BooleanArray.toMatrix(rowLength: Int): Array<Array<Boolean>> =
    Array(this.size / rowLength) { rowIndex ->
        Array(rowLength) { columnIndex ->
            this[rowLength * rowIndex + columnIndex]
        }

    }

inline fun BooleanArray.mapInPlace(mutator: (Boolean) -> Boolean) {
    this.forEachIndexed { idx, value ->
        mutator(value).let { newValue ->
            if (newValue != value) this[idx] = newValue
        }
    }
}

inline fun BooleanArray.setEach(mutator: (Int, Boolean) -> Boolean?) {
    this.forEachIndexed { idx, value ->
        mutator(idx, value)?.let { newValue ->
            this[idx] = newValue
        }
    }
}
