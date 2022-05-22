package com.example.sudoku.utility

fun String.decodeAsBooleanArray(): BooleanArray {
    return toCharArray()
        .map { it == '1' }
        .toBooleanArray()
}

fun String.decodeAsNumberArray(): IntArray {
    return toCharArray()
        .map { it - '0' }
        .toIntArray()
}