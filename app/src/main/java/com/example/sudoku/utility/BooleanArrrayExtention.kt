package com.example.sudoku.utility


fun BooleanArray.encode(): String {
    return buildString {
        this@encode.forEach {
            append(if (it) 1 else 0)
        }
    }
}