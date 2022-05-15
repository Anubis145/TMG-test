package com.example.tmg_test.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlayerModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    var name: String
)