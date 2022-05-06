package com.example.sudoku.database.util

import com.example.sudoku.model.data.DiagonalTable
import com.example.sudoku.model.data.Table

enum class Table0Initialize {
    NORMAL {
        override fun create0Table(): Table {
            val givenN = intArrayOf(
                3, 5, 8,
                10, 13, 16,
                22, 26,
                28, 32, 33, 34,
                36, 40, 44,
                46, 47, 48, 52,
                54, 58,
                64, 67, 70,
                72, 75, 77
            )
            return Table(
                "N_0_",
                intArrayOf(
                    9, 8, 1, 7, 2, 4, 3, 6, 5,
                    3, 2, 4, 6, 1, 5, 8, 7, 9,
                    7, 6, 5, 9, 8, 3, 1, 4, 2,
                    1, 9, 7, 8, 3, 6, 2, 5, 4,
                    6, 4, 2, 5, 7, 1, 9, 3, 8,
                    8, 5, 3, 2, 4, 9, 7, 1, 6,
                    4, 7, 6, 3, 9, 8, 5, 2, 1,
                    5, 3, 8, 1, 6, 2, 4, 9, 7,
                    2, 1, 9, 4, 5, 7, 6, 8, 3
                ), BooleanArray(81) {
                    givenN.contains(it)
                })
        }
    },
    DIAGONAL {
        override fun create0Table(): DiagonalTable {
            val givenN = intArrayOf(
                0, 4, 6,
                12, 13, 15,
                19, 21, 26,
                28, 29,
                39, 40, 42, 43,
                45, 47, 49, 51,
                56, 57, 59, 60,
                65, 71,
                72, 80
            )
            return DiagonalTable(
                "D_0_", intArrayOf(
                    9, 7, 6, 4, 8, 1, 3, 2, 5,
                    1, 4, 3, 2, 5, 9, 7, 8, 6,
                    5, 2, 8, 3, 7, 6, 1, 9, 4,
                    6, 9, 4, 5, 1, 8, 2, 3, 7,
                    8, 1, 2, 7, 3, 4, 5, 6, 9,
                    7, 3, 5, 9, 6, 2, 4, 1, 8,
                    4, 6, 7, 8, 2, 3, 9, 5, 1,
                    2, 5, 1, 6, 9, 7, 8, 4, 3,
                    3, 8, 9, 1, 4, 5, 6, 7, 2
                ), BooleanArray(81) {
                    givenN.contains(it)
                }
            )
        }
    },
    CREATE_NORMAL {
        override fun create0Table(): Table {
            val givenN = intArrayOf(
            )
            return Table(
                "NC_0_",
                intArrayOf(
                    0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0
                ), BooleanArray(81) {
                    givenN.contains(it)
                }
            )
        }
    };

    abstract fun create0Table(): Table

}