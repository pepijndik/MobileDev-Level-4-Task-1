package nl.pdik.level4.task1.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import nl.pdik.level4.task1.model.Game

@Dao
interface GameDao {

    @Query("SELECT * from game ORDER BY `release` ASC")
    fun getGames(): LiveData<List<Game>>

    @Insert
    suspend fun insert(game: Game)

    @Insert
    suspend fun insert(game: List<Game>)

    @Delete
    suspend fun delete(game: Game)

    @Query("DELETE from game")
    suspend fun deleteAll()

}