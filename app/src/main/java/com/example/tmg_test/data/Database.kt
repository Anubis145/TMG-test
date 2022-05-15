package com.example.tmg_test.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tmg_test.model.GameModel
import com.example.tmg_test.model.PlayerModel
import com.example.tmg_test.repository.GamesRepository
import com.example.tmg_test.repository.PlayersRepository

@Database(
    entities = [PlayerModel::class, GameModel::class],
    version = 2,
    exportSchema = false
)
abstract class Database : RoomDatabase() {
    abstract fun playersDao() : PlayersRepository
    abstract fun gamesDao() : GamesRepository
}