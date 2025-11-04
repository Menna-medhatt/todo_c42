package com.route.todoappc42.ui.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "todos")
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "title")
    val title: String = "",

    @ColumnInfo(name = "description")
    val description: String = "",

    @ColumnInfo(name = "date")
    val date: Long,

    @ColumnInfo(name = "is_done")
    val isDone: Boolean = false
) : Parcelable