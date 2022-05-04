package com.example.sudoku.viewmodels.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sudoku.model.TableRepository
import javax.inject.Inject

class MainViewModelFactory @Inject constructor(private val tableRepository: TableRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(tableRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}