package com.claude.poem.data.repository

import android.content.Context
import com.claude.poem.data.local.AppDatabase
import com.claude.poem.data.model.Poem
import com.claude.poem.data.model.PoemEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PoemRepository(private val context: Context) {
    private val db = AppDatabase.getInstance(context)
    private val dao = db.poemDao()

    suspend fun initializeIfNeeded() {
        if (dao.getPoemCount() == 0) {
            val poems = loadPoemsFromJson()
            dao.insertAll(poems)
        }
    }

    suspend fun getRandomPoem(): Poem? {
        initializeIfNeeded()
        return dao.getRandomPoem()?.toPoem()
    }

    suspend fun getPoemById(id: Long): Poem? {
        return dao.getPoemById(id)?.toPoem()
    }

    suspend fun toggleFavorite(id: Long) {
        dao.toggleFavorite(id)
    }

    fun getFavoritePoems(): Flow<List<Poem>> {
        return dao.getFavoritePoems().map { entities ->
            entities.map { it.toPoem() }
        }
    }

    suspend fun getFavoriteCount(): Int = dao.getFavoriteCount()

    private fun loadPoemsFromJson(): List<PoemEntity> {
        val json = context.assets.open("poems.json").bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<PoemJson>>() {}.type
        val poems: List<PoemJson> = Gson().fromJson(json, type)
        return poems.map { it.toEntity() }
    }

    private data class PoemJson(
        val id: Long,
        val title: String,
        val author: String,
        val dynasty: String,
        val content: String,
        val tags: List<String>?
    ) {
        fun toEntity() = PoemEntity(
            id = id,
            title = title,
            author = author,
            dynasty = dynasty,
            content = content,
            tags = tags?.joinToString(",") ?: ""
        )
    }
}
