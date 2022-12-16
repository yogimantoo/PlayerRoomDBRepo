package com.example.myapplication.data
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PlayerDao {

    @Insert
    fun insertPlayer(player: Player)

    @Query("SELECT * FROM players WHERE playerName = :name")
    fun findPlayer(name: String): List<Player>

    @Query("DELETE FROM players WHERE playerName = :name")
    fun deletePlayer(name: String)

    @Query("SELECT * FROM players")
    fun getAllPlayers(): LiveData<List<Player>>
}