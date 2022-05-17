package com.example.tmg_test.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Entity
@Parcelize
data class GameModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "gameModelId")
    var id: Int = 0,

    @Embedded(prefix = "firstPlayerModel")
    var firstPlayer: @RawValue PlayerModel,
    var firstPlayerScore: Int,

    @Embedded(prefix = "secondPlayerModel")
    var secondPlayer: @RawValue  PlayerModel,
    var secondPlayerScore: Int
) : Parcelable