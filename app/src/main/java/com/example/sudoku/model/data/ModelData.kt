package com.example.sudoku.model.data

import com.example.sudoku.database.DBConnector
import kotlin.random.Random

class ModelData {
    private lateinit var table: Table

    fun getTable(tableType: TableType): Table {
       table=DBConnector.getTable(tableType)
        return table
    }

    fun generateNewTable(): Table {
        repeat(6) { switchRandomBoxColumns() }
        repeat(6) { switchRandomBoxLines() }
        repeat(18) { switchRandomColumns() }
        repeat(18) { switchRandomLines() }
        return table
    }

    private fun switchRandomLines() {
        val boxLineIndex = Random.nextInt(3)
        val lineIndexes = arrayOf(Random.nextInt(3), Random.nextInt(2))
        if (lineIndexes[0] == lineIndexes[1])
            lineIndexes[1] = (lineIndexes[1] + 1) % 3
        (0..8).forEach { columnIndex ->
            val tmp = table.cells[boxLineIndex * 3 + lineIndexes[0]][columnIndex]
            table.cells[boxLineIndex * 3 + lineIndexes[0]][columnIndex] =
                table.cells[boxLineIndex * 3 + lineIndexes[1]][columnIndex]
            table.cells[boxLineIndex * 3 + lineIndexes[1]][columnIndex] = tmp
            val givenTmp = table.givenNumbers[(boxLineIndex * 3 + lineIndexes[0]) * 9 + columnIndex]
            table.givenNumbers[(boxLineIndex * 3 + lineIndexes[0]) * 9 + columnIndex] =
                table.givenNumbers[(boxLineIndex * 3 + lineIndexes[1]) * 9 + columnIndex]
            table.givenNumbers[(boxLineIndex * 3 + lineIndexes[1]) * 9 + columnIndex] = givenTmp
        }
    }

    private fun switchRandomColumns() {
        val boxColumnIndex = Random.nextInt(3)
        val columnIndexes = arrayOf(Random.nextInt(3), Random.nextInt(2))
        if (columnIndexes[0] == columnIndexes[1])
            columnIndexes[1] = (columnIndexes[1] + 1) % 3
        (0..8).forEach { lineIndex ->
            val tmp = table.cells[lineIndex][boxColumnIndex * 3 + columnIndexes[0]]
            table.cells[lineIndex][boxColumnIndex * 3 + columnIndexes[0]] =
                table.cells[lineIndex][boxColumnIndex * 3 + columnIndexes[1]]
            table.cells[lineIndex][boxColumnIndex * 3 + columnIndexes[1]] = tmp

            val givenTmp = table.givenNumbers[lineIndex * 9 + boxColumnIndex * 3 + columnIndexes[0]]
            table.givenNumbers[lineIndex * 9 + boxColumnIndex * 3 + columnIndexes[0]] =
                table.givenNumbers[lineIndex * 9 + boxColumnIndex * 3 + columnIndexes[1]]
            table.givenNumbers[lineIndex * 9 + boxColumnIndex * 3 + columnIndexes[1]] = givenTmp
        }
    }

    private fun switchRandomBoxLines() {
        val boxLineIndexes = arrayOf(Random.nextInt(3), Random.nextInt(2))
        if (boxLineIndexes[0] == boxLineIndexes[1])
            boxLineIndexes[1] = (boxLineIndexes[1] + 1) % 3
        (0..8).forEach { columnIndex ->
            (0..2).forEach { lineIndex ->
                val tmp = table.cells[boxLineIndexes[0] * 3 + lineIndex][columnIndex]
                table.cells[boxLineIndexes[0] * 3 + lineIndex][columnIndex] =
                    table.cells[boxLineIndexes[1] * 3 + lineIndex][columnIndex]
                table.cells[boxLineIndexes[1] * 3 + lineIndex][columnIndex] = tmp

                val givenTmp =
                    table.givenNumbers[(boxLineIndexes[0] * 3 + lineIndex) * 9 + columnIndex]
                table.givenNumbers[(boxLineIndexes[0] * 3 + lineIndex) * 9 + columnIndex] =
                    table.givenNumbers[(boxLineIndexes[1] * 3 + lineIndex) * 9 + columnIndex]
                table.givenNumbers[(boxLineIndexes[1] * 3 + lineIndex) * 9 + columnIndex] = givenTmp
            }
        }
    }

    private fun switchRandomBoxColumns() {
        val boxColumnIndexes = arrayOf(Random.nextInt(3), Random.nextInt(2))
        if (boxColumnIndexes[0] == boxColumnIndexes[1])
            boxColumnIndexes[1] = (boxColumnIndexes[1] + 1) % 3
        (0..8).forEach { lineIndex ->
            (0..2).forEach { columnIndex ->
                val tmp = table.cells[lineIndex][boxColumnIndexes[0] * 3 + columnIndex]
                table.cells[lineIndex][boxColumnIndexes[0] * 3 + columnIndex] =
                    table.cells[lineIndex][boxColumnIndexes[1] * 3 + columnIndex]
                table.cells[lineIndex][boxColumnIndexes[1] * 3 + columnIndex] = tmp

                val givenTmp =
                    table.givenNumbers[lineIndex * 9 + boxColumnIndexes[0] * 3 + columnIndex]
                table.givenNumbers[lineIndex * 9 + boxColumnIndexes[0] * 3 + columnIndex] =
                    table.givenNumbers[lineIndex * 9 + boxColumnIndexes[1] * 3 + columnIndex]
                table.givenNumbers[lineIndex * 9 + boxColumnIndexes[1] * 3 + columnIndex] = givenTmp
            }
        }
    }

}