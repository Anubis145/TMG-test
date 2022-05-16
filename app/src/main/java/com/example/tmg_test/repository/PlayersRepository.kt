package com.example.tmg_test.repository

import androidx.room.*
import com.example.tmg_test.model.PlayerModel
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*

@Dao
interface PlayersRepository {
    @Query("SELECT * FROM PlayerModel")
    fun getAll(): Flowable<List<PlayerModel>>

    @Query("DELETE FROM PlayerModel")
    fun deleteAll() : Completable

    @Update
    fun update(model: PlayerModel) : Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Throws(android.database.SQLException::class)
    fun insert(model: PlayerModel) : Single<Long>

    @Delete
    fun delete(model: PlayerModel)

    @Query("SELECT * FROM PlayerModel WHERE :id = id")
    fun getById(id: Int) : Single<PlayerModel?>
}
