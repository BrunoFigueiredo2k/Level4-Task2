package com.example.level4_task2

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GameDao {

    @Query("SELECT * FROM gameTable")
    suspend fun getAllGames(): List<Game>

    @Insert
    suspend fun insertGame(game: Game)

    @Query("DELETE FROM gameTable")
    suspend fun deleteAllGames()

    // Stats queries
    @Query("SELECT COUNT(\"result\") FROM gameTable WHERE result = 'Draw'")
    suspend fun countDraws(): List<Game>

    @Query("SELECT COUNT(\"result\") FROM gameTable WHERE result = 'You win!'")
    suspend fun countWins(): List<Game>

    @Query("SELECT COUNT(\"result\") FROM gameTable WHERE result = 'You lose!'")
    suspend fun countLosses(): List<Game>
}
