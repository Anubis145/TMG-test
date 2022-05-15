package com.example.tmg_test.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GameModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "gameModelId")
    var id: Int = 0,

    @Embedded(prefix = "firstPlayerModel")
    var firstPlayer: PlayerModel,
    var firstPlayerScore: Int,

    @Embedded(prefix = "secondPlayerModel")
    var secondPlayer: PlayerModel,
    var secondPlayerScore: Int
)