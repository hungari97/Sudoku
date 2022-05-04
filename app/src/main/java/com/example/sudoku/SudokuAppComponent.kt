package com.example.sudoku

import com.example.sudoku.model.RepositoryModule
import com.example.sudoku.viewmodels.ViewModelModule
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        ViewModelModule::class,
        RepositoryModule::class
    ]
)

interface SudokuAppComponent {
    fun inject(activity: TableActivity)
    fun inject(activity: CreateTableActivity)
}