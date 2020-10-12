package com.example.level4_task2

import android.content.Context

class GameRepository(context: Context) {

    private val gameDao: GameDao

    init {
        val database = GameRoomDatabase.getDatabase(context)
        gameDao = database!!.gameDao()
    }

    suspend fun getAllProducts(): List<Game> {
        return gameDao.getAllProducts()
    }

    suspend fun insertProduct(product: Game) {
        gameDao.insertProduct(product)
    }

    suspend fun deleteProduct(product: Game) {
        gameDao.deleteProduct(product)
    }

    suspend fun deleteAllProducts() {
        gameDao.deleteAllProducts()
    }

}
