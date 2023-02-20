package nl.pdik.level4.task1.repository

import android.content.Context
import nl.pdik.level4.task1.database.GameDao
import nl.pdik.level4.task1.database.GameRoomDatabase
import nl.pdik.level4.task1.model.Game

class GameRepository(context: Context) {
    private val gameDao: GameDao

    init {
        val database = GameRoomDatabase.getDatabase(context)
        gameDao = database!!.gameDao()
    }

    suspend fun insert(game: Game) = gameDao.insert(game)

    suspend fun delete(game: Game) = gameDao.delete(game)

    fun getGames() = gameDao.getGames()

    suspend fun deleteAll() = gameDao.deleteAll()

}