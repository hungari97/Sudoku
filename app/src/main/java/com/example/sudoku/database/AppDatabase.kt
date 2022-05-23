package com.example.sudoku.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sudoku.database.dao.CellStateDao
import com.example.sudoku.database.dao.GameStateDao
import com.example.sudoku.database.dao.TableItemDao
import com.example.sudoku.database.dao.TableStateDao
import com.example.sudoku.database.entity.CellState
import com.example.sudoku.database.entity.GameState
import com.example.sudoku.database.entity.TableItem
import com.example.sudoku.database.entity.TableState

@Database(
    entities = [TableItem::class, GameState::class, TableState::class, CellState::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tableDao(): TableItemDao
    abstract fun gameStateDao(): GameStateDao
    abstract fun tableStateDao(): TableStateDao
    abstract fun cellStateDao(): CellStateDao

    companion object {
        private var instance //only one interface
                : AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "Database_name"
                )
                    .fallbackToDestructiveMigration()
                    //.addCallback(roomCallBack)
                    .build()
            }
            return instance
        }
    }
}