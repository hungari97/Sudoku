package com.example.sudoku.model

import com.example.sudoku.model.data.ModelData
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun providesModelData(): ModelData {

        return ModelData()
    }

    @Provides
    @Singleton
    fun providesTableRepository(data: ModelData):TableRepository{

        return TableRepositoryImpl(data)
    }

}