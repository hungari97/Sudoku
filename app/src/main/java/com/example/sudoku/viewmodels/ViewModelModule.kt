package com.example.sudoku.viewmodels

import com.example.sudoku.model.TableRepository
import com.example.sudoku.viewmodels.create.CreateViewModelFactory
import com.example.sudoku.viewmodels.main.MainViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class ViewModelModule {
    @Provides
    fun providesMainViewModelFactory(tableRepository: TableRepository): MainViewModelFactory {
        return MainViewModelFactory(tableRepository)
    }

    @Provides
    fun providesCreateViewModelFactory(tableRepository: TableRepository): CreateViewModelFactory {
        return CreateViewModelFactory(tableRepository)
    }

}