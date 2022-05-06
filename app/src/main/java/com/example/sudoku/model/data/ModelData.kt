package com.example.sudoku.model.data

import com.example.sudoku.database.DBConnector
import com.example.sudoku.database.entity.CellState
import com.example.sudoku.database.entity.GameState
import com.example.sudoku.database.entity.TableState
import kotlin.random.Random

class ModelData {
    private lateinit var table: Table

    fun getTable(tableType: TableType): Table {
        table = DBConnector.getTable(tableType)
        return table
    }

    private fun BooleanArray.encode(): String {
        return buildString {
            this@encode.forEach {
                append(if (it) 1 else 0)
            }
        }
    }

    fun saveGameState(table: Table, remainingHelpCount: Int, secondsPassed: Int) {
        val gameState =
            GameState(remainingHelpCount = remainingHelpCount, secondsPassed = secondsPassed)
        val tableState = TableState(reference = table.reference)
        tableState.cells = table.cells
            .flatten()
            .map {
                CellState(
                    solutionNumber = it.solutionNumber,
                    given = it.given,
                    allPossibilitiesEncoded = it.allPossibilities.encode(),
                    shownPossibilitiesEncoded = it.shownPossibilities.encode(),
                    chosenNumber = it.chosenNumber
                )
            }
            .toTypedArray()
        gameState.table = tableState
        DBConnector.saveGameState(gameState)
    }

    private fun String.decodeAsBooleanArray(): BooleanArray {
        return toCharArray()
            .map { it == '1' }
            .toBooleanArray()
    }

    fun loadGameStateOrNull(): GameStateDto? {
        val gameState = DBConnector.loadGameState() ?: return null
        val cells = gameState.table!!.cells!!
            .toList()
            .chunked(9)
            .map { cellRow ->
                cellRow.map { cellState ->
                    Cell(
                        solutionNumber = cellState.solutionNumber,
                        given = cellState.given
                    ).apply {
                        chosenNumber = cellState.chosenNumber
                        allPossibilities = cellState
                            .allPossibilitiesEncoded
                            .decodeAsBooleanArray()
                        shownPossibilities = cellState
                            .shownPossibilitiesEncoded
                            .decodeAsBooleanArray()
                    }
                }.toTypedArray()
            }.toTypedArray()
        val reference = gameState.table!!.reference
        val table: Table = when {
            reference.startsWith("N_") ->
                Table(
                    reference,
                    cells
                )
            else ->
                DiagonalTable(
                    reference,
                    cells
                )
        }
        return GameStateDto(
            gameState.remainingHelpCount,
            gameState.secondsPassed,
            table
        )
    }
    /*
    fun generateNewTable(): Table {
        repeat(6) { switchRandomBoxColumns() }
        repeat(6) { switchRandomBoxLines() }
        repeat(18) { switchRandomColumns() }
        repeat(18) { switchRandomLines() }
        return table
    }
    */

    private fun switchRandomLines() {
        val boxLineIndex = Random.nextInt(3)
        val lineIndexes = arrayOf(Random.nextInt(3), Random.nextInt(2))
        if (lineIndexes[0] == lineIndexes[1])
            lineIndexes[1] = (lineIndexes[1] + 1) % 3
        (0..8).forEach { index ->
            val tmp = table.cells[boxLineIndex * 3 + lineIndexes[0]][index]
            table.cells[boxLineIndex * 3 + lineIndexes[0]][index] =
                table.cells[boxLineIndex * 3 + lineIndexes[1]][index]
            table.cells[boxLineIndex * 3 + lineIndexes[1]][index] = tmp
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
            }
        }
    }

}