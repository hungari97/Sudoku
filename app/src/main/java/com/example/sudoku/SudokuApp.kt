package com.example.sudoku

import android.app.Application
import android.net.Uri
import com.example.sudoku.database.AppDatabase

class SudokuApp : Application() {

    companion object {
        lateinit var database: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()


        database = AppDatabase.getInstance(this)!!

        component = DaggerSudokuAppComponent
            .builder()
            .build()
    }
}

var component: SudokuAppComponent = DaggerSudokuAppComponent
    .builder()
    .build()