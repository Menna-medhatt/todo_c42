package com.route.todoappc42.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.route.todoappc42.ui.model.Todo

@Dao
interface TodoDao {

    @Update
    fun updateTodo(todo: Todo)

    @Insert
    fun addTodo(todo: Todo)

    @Delete
    fun deleteTodo(todo: Todo)


    @Query("SELECT * FROM todos")
    fun getAllTodos(): List<Todo>


    @Query("SELECT * FROM todos WHERE id = :id")
    fun getTodoById(id: Int): Todo


    @Query("SELECT * FROM todos WHERE date = :date")
    fun getTodosByDate(date: Long): List<Todo>
}