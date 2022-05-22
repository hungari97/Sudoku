package com.example.sudoku.utility

fun IntArray.encode(): String {
    return buildString {
        this@encode.forEach {
            append(it)
        }
    }
}