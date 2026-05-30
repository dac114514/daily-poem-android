package com.claude.poem.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "poems")
data class PoemEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val author: String,
    val dynasty: String,
    val content: String,
    val tags: String = "",
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean = false,
    @ColumnInfo(name = "favorited_at") val favoritedAt: Long? = null
) {
    fun toPoem(): Poem = Poem(
        id = id,
        title = title,
        author = author,
        dynasty = dynasty,
        content = content,
        tags = if (tags.isBlank()) emptyList() else tags.split(","),
        isFavorite = isFavorite,
        favoritedAt = favoritedAt
    )
}

data class Poem(
    val id: Long,
    val title: String,
    val author: String,
    val dynasty: String,
    val content: String,
    val tags: List<String> = emptyList(),
    val isFavorite: Boolean = false,
    val favoritedAt: Long? = null
)
