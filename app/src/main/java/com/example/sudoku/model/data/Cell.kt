package com.example.sudoku.model.data

class Cell(val solutionNumber: Int, val given: Boolean) {
    var allPossibilities = BooleanArray(9) { true }
    var shownPossibilities = BooleanArray(9) { false }
    var chosenNumber: Int = 0
        set(value) {
            if ((!given && !isChosenNumberCorrect()) || (given && field == 0)) {
                field = value
            }
        }

    init {
        if (given) {
            chosenNumber = solutionNumber
            for (value in 0..8) {
                allPossibilities[value] = (value == solutionNumber - 1)
                shownPossibilities[value] = (value == solutionNumber - 1)
            }
        }
    }

    fun isChosenNumberCorrect(): Boolean {
        return chosenNumber == solutionNumber
    }

    fun writeAnswer(answer: Int) {
        if (!given) {
            chosenNumber = answer
            /*for (value in 0..8) {
                shownPossibilities[value] = (value == chosenNumber - 1)
            }*/
        }
    }

    fun writeTip(tip: Int): Boolean {
        if (!given && chosenNumber == 0) {
            shownPossibilities[tip - 1] = !shownPossibilities[tip - 1]
            return shownPossibilities[tip - 1]
        }
        return false
    }

    fun hideNumbers() {
        if (!given) {
            chosenNumber = 0
            shownPossibilities.fill(false)
        }
    }

    fun reduceAllPossibilities(onlyPossibility:Int){
        if (!given) {
            allPossibilities.fill(false)
            allPossibilities[onlyPossibility - 1] = true
        }
    }

    fun givePossibility(){
        hideNumbers()
        chosenNumber = solutionNumber
        reduceAllPossibilities(chosenNumber)
    }

}