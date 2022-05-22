package com.example.sudoku.database

import androidx.annotation.WorkerThread
import com.example.sudoku.SudokuApp
import com.example.sudoku.database.entity.GameState
import com.example.sudoku.database.entity.TableItem
import com.example.sudoku.model.data.TableType
import com.example.sudoku.model.data.table.DiagonalTable
import com.example.sudoku.model.data.table.NormalTable
import com.example.sudoku.model.data.table.OddEvenTable
import com.example.sudoku.model.data.table.Table
import com.example.sudoku.utility.decodeAsBooleanArray
import com.example.sudoku.utility.decodeAsNumberArray
import com.example.sudoku.utility.encode
import com.example.sudoku.utility.get

class DatabaseRepositoryImpl : DatabaseRepository {
    private val database = (SudokuApp).database

    init {
        getAllTable().forEach { delete(it) }
        dbInitialise()

    }

    private fun decodeTable(tableEncoded: String, id: String, tableType: TableType): Table {

        return when (tableType) {
            TableType.NORMAL -> NormalTable(
                solutionArray = tableEncoded
                    .slice(0 until 81)
                    .decodeAsNumberArray(),
                givenNumbers = tableEncoded
                    .slice(81 until 162)
                    .decodeAsBooleanArray(),
                id = id
            )
            TableType.DIAGONAL -> DiagonalTable(
                solutionArray = tableEncoded
                    .slice(0 until 81)
                    .decodeAsNumberArray(),
                givenNumbers = tableEncoded
                    .slice(81 until 162)
                    .decodeAsBooleanArray(),
                id = id
            )
            TableType.ODD_EVEN -> OddEvenTable(
                solutionArray = tableEncoded
                    .slice(0 until 81)
                    .decodeAsNumberArray(),
                givenNumbers = tableEncoded
                    .slice(81 until 162)
                    .decodeAsBooleanArray(),
                oddEvenGroup = tableEncoded
                    .slice(162 until 243)
                    .decodeAsBooleanArray(),
                id = id
            )
        }
    }

    @WorkerThread
    override fun getTable(tableType: TableType): Table {
        val tablesWithType = database.tableDao().getTableByType(tableType.name)
        val chosenTable = tablesWithType.random()
        return decodeTable(chosenTable.content, chosenTable.id, tableType)

    }

    fun delete(record: Table) {
        database.tableDao().deleteTable(record.id)
    }

    @WorkerThread
    override fun insertTable(table: Table) {
        val content = buildString {
            append(
                table.cells
                    .flatten()
                    .map { cell -> cell.solutionNumber }
                    .toIntArray()
                    .encode()
            )
            append(
                table.cells
                    .flatten()
                    .map { cell -> cell.given }
                    .toBooleanArray()
                    .encode()
            )
            table.cells[0, 0].groupFlags.indices.forEach { groupIndex ->
                append(
                    table.cells
                        .flatten()
                        .map { cell -> cell.groupFlags[groupIndex] }
                        .toBooleanArray()
                        .encode()
                )
            }
        }

        val type = when (table) {
            is NormalTable -> TableType.NORMAL
            is DiagonalTable -> TableType.DIAGONAL
            is OddEvenTable -> TableType.ODD_EVEN
        }.name

        val tableItem = TableItem(table.id, type, content)

        database.tableDao().insertItem(tableItem)
    }

    override fun loadGameState(): GameState? {
        return database.gameStateDao().getAll()
            .firstOrNull()
            ?.apply {
                table = database.tableStateDao().getByGameStateId(id)
                table?.apply {
                    cells = database.cellStateDao()
                        .getByTableStateId(id)
                        .toTypedArray()
                }
            }
    }

    override fun saveGameState(gameState: GameState) {
        deleteGameState()
        database.gameStateDao().insert(gameState)
        database.tableStateDao().insert(gameState.table!!)
        gameState.table!!.cells!!.forEach {
            database.cellStateDao().insert(it)
        }
    }

    override fun deleteGameState() {
        with(database) {
            gameStateDao().getAll()
                .forEach { gameState ->
                    val tableState = tableStateDao().getByGameStateId(gameState.id)
                    tableState?.let {
                        cellStateDao()
                            .getByTableStateId(tableState.id)
                            .forEach { cellStateDao().delete(it) }
                        tableStateDao().delete(tableState)
                    }
                    gameStateDao().delete(gameState)
                }
        }
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun getAllTable(): List<Table> {
        return database.tableDao().getAll()
            .map { data ->
                decodeTable(data.content, data.id, TableType.valueOf(data.type))
            }
    }

    private fun dbInitialise() {
        val tables = ArrayList<Table>()

        var givenN = sortedSetOf(
            0, 1, 4, 5, 8,
            12, 14,
            19, 23, 25,
            34,
            36, 38, 39, 41, 42, 44,
            46,
            55, 57, 61,
            66, 68,
            72, 75, 76, 79, 80
        )

        tables.add(
            NormalTable(
                "N_0",
                intArrayOf(
                    7, 2, 5, 1, 9, 6, 4, 8, 3,
                    4, 6, 3, 2, 8, 5, 9, 7, 1,
                    9, 8, 1, 3, 7, 4, 5, 2, 6,
                    3, 7, 2, 9, 4, 8, 1, 6, 5,
                    1, 9, 6, 5, 2, 3, 8, 4, 7,
                    5, 4, 8, 6, 1, 7, 2, 3, 9,
                    6, 3, 4, 8, 5, 1, 7, 9, 2,
                    8, 1, 9, 7, 6, 2, 3, 5, 4,
                    2, 5, 7, 4, 3, 9, 6, 1, 8
                ), BooleanArray(81) {
                    givenN.contains(it)
                }
            )
        )

        givenN = sortedSetOf(
            0, 4, 5,
            9, 10, 12, 16,
            20, 21, 23, 24, 25, 26,
            28, 29, 30, 33, 35,
            36, 37, 43,
            45, 47, 49, 50, 52,
            56, 57, 59, 60,
            64, 66, 68, 70, 71,
            72, 73, 76, 77, 79, 80
        )
        tables.add(
            NormalTable(
                "N_1",
                intArrayOf(
                    3, 8, 6, 1, 5, 2, 4, 9, 7,
                    2, 5, 7, 3, 4, 9, 6, 1, 8,
                    9, 1, 4, 6, 8, 7, 5, 2, 3,
                    6, 9, 3, 2, 7, 1, 8, 4, 5,
                    5, 7, 1, 8, 6, 4, 2, 3, 9,
                    4, 2, 8, 9, 3, 5, 7, 6, 1,
                    1, 6, 5, 4, 9, 8, 3, 7, 2,
                    7, 3, 2, 5, 1, 6, 9, 8, 4,
                    8, 4, 9, 7, 2, 3, 1, 5, 6
                ), BooleanArray(81) {
                    givenN.contains(it)
                }
            )
        )

        givenN = sortedSetOf(
            3, 4,
            9, 10, 13,
            18, 20, 21, 24, 26,
            27, 28, 31, 33,
            39, 41,
            47, 49, 52, 53,
            54, 56, 59, 60, 62,
            67, 70, 71,
            76, 77
        )
        tables.add(
            DiagonalTable(
                "D_0",
                intArrayOf(
                    2, 5, 4, 8, 1, 7, 9, 3, 6,
                    8, 6, 3, 4, 9, 2, 7, 1, 5,
                    9, 1, 7, 5, 6, 3, 8, 2, 4,
                    5, 3, 8, 1, 7, 9, 6, 4, 2,
                    1, 2, 9, 6, 5, 4, 3, 7, 8,
                    4, 7, 6, 3, 2, 8, 1, 5, 9,
                    6, 9, 2, 7, 3, 5, 4, 8, 1,
                    3, 4, 1, 2, 8, 6, 5, 9, 7,
                    7, 8, 5, 9, 4, 1, 2, 6, 3
                ),
                BooleanArray(81) {
                    givenN.contains(it)
                },
            )
        )

        givenN = sortedSetOf(
            2, 6, 7,
            9, 14,
            18, 26,
            28, 30, 32,
            48, 50, 52,
            54, 62,
            66, 71,
            74, 75, 78
        )
        tables.add(
            DiagonalTable(
                "D_1",
                intArrayOf(
                    5, 7, 4, 2, 9, 6, 8, 1, 3,
                    6, 9, 8, 7, 1, 3, 4, 2, 5,
                    2, 3, 1, 4, 8, 5, 6, 9, 7,
                    8, 5, 2, 6, 7, 1, 3, 4, 9,
                    1, 6, 3, 8, 4, 9, 5, 7, 2,
                    9, 4, 7, 5, 3, 2, 1, 8, 6,
                    3, 2, 9, 1, 6, 8, 7, 5, 4,
                    4, 8, 6, 9, 5, 7, 2, 3, 1,
                    7, 1, 5, 3, 2, 4, 9, 6, 8
                ), BooleanArray(81) {
                    givenN.contains(it)
                }
            )
        )

        givenN = sortedSetOf(
            4, 5,
            14, 15,
            19, 20, 24,
            27, 28,
            36, 40, 44,
            52, 53,
            56, 60, 61,
            65, 66,
            75, 76
        )
        val oddEven = sortedSetOf(
            12, 14,
            22,
            28, 34,
            38, 42,
            46, 52,
            58,
            66, 68

        )
        tables.add(
            OddEvenTable(
                "O_0",
                intArrayOf(
                    3, 2, 8, 7, 4, 1, 6, 9, 5,
                    5, 7, 6, 8, 9, 2, 4, 3, 1,
                    4, 1, 9, 3, 6, 5, 7, 8, 2,
                    9, 8, 3, 4, 5, 6, 1, 2, 7,
                    6, 5, 2, 1, 7, 3, 8, 4, 9,
                    7, 4, 1, 9, 2, 8, 5, 6, 3,
                    2, 6, 7, 5, 8, 9, 3, 1, 4,
                    8, 3, 5, 2, 1, 4, 9, 7, 6,
                    1, 9, 4, 6, 3, 7, 2, 5, 8
                ), BooleanArray(81) {
                    givenN.contains(it)
                }, BooleanArray(81) {
                    oddEven.contains(it)
                }
            )
        )
        tables.forEach { tab ->
            insertTable(tab)
        }
    }

}