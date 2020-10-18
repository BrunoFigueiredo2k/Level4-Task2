package com.example.level4_task2

import android.content.Context

class GameRepository(context: Context) {

    private val gameDao: GameDao

    init {
        val database = GameRoomDatabase.getDatabase(context)
        gameDao = database!!.gameDao()
    }

    suspend fun getAllGames(): List<Game> {
        return gameDao.getAllGames()
    }

    suspend fun insertGame(game: Game) {
        gameDao.insertGame(game)
    }

    suspend fun deleteAllGames() {
        gameDao.deleteAllGames()
    }

    suspend fun countDraws(): List<Game> {
        return gameDao.countDraws()
    }

    suspend fun countWins(): List<Game> {
        return gameDao.countWins()
    }
    suspend fun countLosses(): List<Game> {
        return gameDao.countLosses()
    }

}
