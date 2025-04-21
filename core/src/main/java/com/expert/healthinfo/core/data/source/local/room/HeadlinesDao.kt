package com.expert.healthinfo.core.data.source.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.expert.healthinfo.core.data.source.local.entity.HeadlinesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HeadlinesDao {

    @Query("SELECT * FROM headlines")
    fun getAllHeadlines(): Flow<List<HeadlinesEntity>>

    @Query("SELECT * FROM headlines WHERE isFavorite = 1")
    fun getFavoriteHeadlines(): Flow<List<HeadlinesEntity>>

    @Query("SELECT EXISTS(SELECT * FROM headlines WHERE id = :idHeadlines AND isFavorite = 1)")
    fun isHeadlineFavorite(idHeadlines: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHeadlines(headlines: HeadlinesEntity)

    @Delete
    suspend fun deleteFavoriteHeadlines(headlines: HeadlinesEntity): Int
}