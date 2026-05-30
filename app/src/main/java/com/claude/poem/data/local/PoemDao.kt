package com.claude.poem.data.local

import androidx.room.*
import com.claude.poem.data.model.PoemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PoemDao {
    @Query("SELECT * FROM poems WHERE is_favorite = 1 ORDER BY favorited_at DESC")
    fun getFavoritePoems(): Flow<List<PoemEntity>>

    @Query("SELECT * FROM poems ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomPoem(): PoemEntity?

    @Query("SELECT * FROM poems WHERE id = :id")
    suspend fun getPoemById(id: Long): PoemEntity?

    @Query("SELECT COUNT(*) FROM poems")
    suspend fun getPoemCount(): Int

    @Query("SELECT COUNT(*) FROM poems WHERE is_favorite = 1")
    suspend fun getFavoriteCount(): Int

    @Query("UPDATE poems SET is_favorite = CASE WHEN is_favorite = 0 THEN 1 ELSE 0 END, favorited_at = CASE WHEN is_favorite = 0 THEN :now ELSE NULL END WHERE id = :id")
    suspend fun toggleFavorite(id: Long, now: Long = System.currentTimeMillis())

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(poems: List<PoemEntity>)
}
