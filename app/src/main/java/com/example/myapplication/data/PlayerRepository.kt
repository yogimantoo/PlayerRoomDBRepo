package com.example.myapplication.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class PlayerRepository(private val playerDao: PlayerDao) {
    val allPlayers: LiveData<List<Player>> = playerDao.getAllPlayers()
    val searchResults = MutableLiveData<List<Player>>()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertPlayer(newplayer: Player) {
        coroutineScope.launch(Dispatchers.IO) {
            playerDao.insertPlayer(newplayer)
        }
    }

    fun deletePlayer(name: String) {
        coroutineScope.launch(Dispatchers.IO) {
            playerDao.deletePlayer(name)
        }
    }

    fun findPlayer(name: String) {
        coroutineScope.launch(Dispatchers.Main) {
            searchResults.value = asyncFind(name).await()
        }
    }

    private fun asyncFind(name: String): Deferred<List<Player>?> =
        coroutineScope.async(Dispatchers.IO) {
            return@async playerDao.findPlayer(name)
        }
}