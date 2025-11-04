package com.route.todoappc42.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.route.todoappc42.database.daos.TodoDao
import com.route.todoappc42.ui.model.Todo

@Database(entities = [Todo::class], version = 2)
abstract class MyDatabase : RoomDatabase() {
    abstract fun getTodoDao(): TodoDao

    companion object {
        private var database: MyDatabase? = null

        fun init(context: Context){
            if(database == null){
                database = Room.databaseBuilder(context, MyDatabase::class.java, "Todo")
                    .fallbackToDestructiveMigration(true)
                    .allowMainThreadQueries()
                    .build()
            }
        }
        fun getInstance(context: Context): MyDatabase{
            return database!!
        }
    }
}
