package com.example.tmg_test.repository

import androidx.room.*
import com.example.tmg_test.model.GameModel
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface GamesRepository {
    @Query("SELECT * FROM GameModel")
    fun getAll(): Flowable<List<GameModel>>

    @Query("DELETE FROM GameModel")
    fun deleteAll() : Completable

    @Update
    fun update(model: GameModel) : Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Throws(android.database.SQLException::class)
    fun insert(model: GameModel) : Single<Long>

    @Delete
    fun delete(model: GameModel)

    @Query("SELECT * FROM GameModel WHERE secondPlayerModelname = :name OR firstPlayerModelname = :name")
    fun getPlayedCountByPlayerName(name: String) : Flowable<List<GameModel>>
}
