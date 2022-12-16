package com.example.myapplication

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.Player
import com.example.myapplication.data.PlayerRepository
import com.example.myapplication.data.PlayerRoomDatabase

class PlayerViewModel (application: Application) : ViewModel() {

    val allPlayers: LiveData<List<Player>>
    private val repository: PlayerRepository
    val searchResults: MutableLiveData<List<Player>>

    init {
        val playerDb = PlayerRoomDatabase.getInstance(application)
        val playerDao = playerDb.playerDao()
        repository = PlayerRepository(playerDao)

        allPlayers = repository.allPlayers
        searchResults = repository.searchResults
    }

    fun insertPlayer(player: Player) {
        repository.insertPlayer(player)
    }

    fun findPlayer(name: String) {
        repository.findPlayer(name)
    }

    fun deletePlayer(name: String) {
        repository.deletePlayer(name)
    }
}
