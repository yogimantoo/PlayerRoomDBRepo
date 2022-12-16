package com.example.myapplication.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "players")
class Player(@ColumnInfo(name = "playerName") var name: String) {
    @PrimaryKey(autoGenerate = true)
    @NotNull
    @ColumnInfo(name = "playerId")
    var id: Int = 0
}