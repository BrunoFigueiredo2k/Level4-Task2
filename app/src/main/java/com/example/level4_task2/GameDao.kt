package com.example.level4_task2

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GameDao {

    @Query("SELECT * FROM gameTable")
    suspend fun getAllProducts(): List<Game>

    @Insert
    suspend fun insertProduct(game: Game)

    @Delete
    suspend fun deleteProduct(game: Game)

    @Query("DELETE FROM gameTable")
    suspend fun deleteAllProducts()

}
