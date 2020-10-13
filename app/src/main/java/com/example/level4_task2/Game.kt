package com.example.level4_task2

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/** Data class called Reminder which has a String representing the reminder **/
@Entity(tableName = "gameTable")
data class Game(

    @ColumnInfo(name = "date")
    var date: Date?,

    @ColumnInfo(name = "moveComputer")
    var moveComputer: String,

    @ColumnInfo(name = "movePlayer")
    var movePlayer: String,

    @ColumnInfo(name = "result")
    var result: String,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null

)