package com.example.sudoku.viewmodels.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sudoku.model.TableRepository
import javax.inject.Inject

class CreateViewModelFactory @Inject constructor(private val tableRepository: TableRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateViewModel::class.java)) {
            return CreateViewModel(tableRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}