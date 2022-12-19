package com.example.myapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [(Player::class)], version = 1, exportSchema = false)
abstract class PlayerRoomDatabase: RoomDatabase() {
    abstract fun playerDao(): PlayerDao
    companion object {
        private var INSTANCE: PlayerRoomDatabase? = null
        @OptIn(InternalCoroutinesApi::class)
        fun getInstance(context: Context): PlayerRoomDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PlayerRoomDatabase::class.java,
                        "player_database"
                    ).fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}