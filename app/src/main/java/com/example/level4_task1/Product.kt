package com.example.level4_task1

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/** Data class called Reminder which has a String representing the reminder **/
@Entity(tableName = "productTable")
data class Product(

    @ColumnInfo(name = "name")
    var productName: String,

    @ColumnInfo(name = "quantity")
    var productQuantity: Short, // Int

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null

)