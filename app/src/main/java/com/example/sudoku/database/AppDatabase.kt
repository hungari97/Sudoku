package com.example.sudoku.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sudoku.database.tableitem.TableDao
import com.example.sudoku.database.tableitem.TableItem

@Database(entities = [TableItem::class], version = 1)
//@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tableDao(): TableDao

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

        /*
        private val roomCallBack: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

            }
        }

         */
    }
}