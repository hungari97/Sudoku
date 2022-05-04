package com.example.sudoku.database

import androidx.annotation.WorkerThread
import com.example.sudoku.SudokuApp
import com.example.sudoku.database.tableitem.TableItem
import com.example.sudoku.model.data.TableType
import com.example.sudoku.model.data.Table
import com.example.sudoku.utility.forEachCell

class DatabaseRepositoryImpl : DatabaseRepository {
    private val db = (SudokuApp).database

    init {
        /*  if (getAllTable().isEmpty()) {
              dbInitialise()
          }*/
        getAllTable().forEach { delete(it) }
        dbInitialise()

    }

    private fun decodeTable(tableEncoded: String, reference: String): Table {

        val tableCoded: CharArray = tableEncoded.toCharArray()

        val temp = tableCoded.map { it - '0' }

        return Table(
            solutionArray = temp.slice(0 until 81).toIntArray(),
            givenNumbers = temp.slice(81 until 162).map { it > 0 }.toBooleanArray(),
            referenceID = reference
        )
    }

    @WorkerThread
    override fun getTable(tableType: TableType): Table {
        val data = db.tableDao().getTableByNum("N_0_")
        return decodeTable(data.tableArray, data.referenceNumber)

    }

    fun delete(item: Table) {
        val data = StringBuilder()
        item.cells.forEachCell { cell ->
            data.append(cell.solutionNumber)
        }

        item.givenNumbers.forEach {
            if (it) {
                data.append(2)
            } else {
                data.append(0)
            }
        }
        val bditem = TableItem(0, item.getRefence(), data.toString())
        db.tableDao().deleteTable(bditem)
    }

    @WorkerThread
    override fun insertTable(item: Table) {
        val data = StringBuilder()
        item.cells.forEachCell { cell ->
            data.append(cell.solutionNumber)
        }
        item.givenNumbers.forEach {
            if (it) {
                data.append(2)
            } else {
                data.append(0)
            }
        }
        val bditem = TableItem(0, item.getRefence(), data.toString())

        db.tableDao().insertItem(bditem)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun getAllTable(): List<Table> {
        return db.tableDao().getAll()
            .map { data ->
                decodeTable(data.tableArray, data.referenceNumber)
            }
    }

    private fun dbInitialise() {
        val tables = ArrayList<Table>()

        var givenN = intArrayOf(
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
            Table(
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
                }, "N_1_"
            )
        )

        givenN = intArrayOf(
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
            Table(
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
                }, "N_2_"
            )
        )

        givenN = intArrayOf(
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
            Table(
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
                ), BooleanArray(81) {
                    givenN.contains(it)
                }, "D_1_"
            )
        )

        givenN = intArrayOf(
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
            Table(
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
                }, "D_2_"
            )
        )
        tables.forEach { tab ->
            insertTable(tab)
        }
    }

}