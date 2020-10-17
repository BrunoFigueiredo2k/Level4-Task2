package com.example.level4_task2

import androidx.annotation.DrawableRes
import androidx.room.*
import java.util.*

/** Data class called Reminder which has a String representing the reminder **/
@Entity(tableName = "gameTable")
data class Game(

    @ColumnInfo(name = "date")
    var date: Date?,

    @ColumnInfo(name = "moveComputer")
    @DrawableRes var moveComputer: Int,

    @ColumnInfo(name = "movePlayer")
    @DrawableRes var movePlayer: Int,

    @ColumnInfo(name = "result")
    var result: String,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null

)