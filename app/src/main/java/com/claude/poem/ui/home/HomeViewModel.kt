package com.claude.poem.ui.home

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.net.Uri
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.claude.poem.data.model.Poem
import com.claude.poem.data.repository.PoemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PoemRepository(application)

    private val _currentPoem = MutableStateFlow<Poem?>(null)
    val currentPoem: StateFlow<Poem?> = _currentPoem.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        refreshPoem()
    }

    fun refreshPoem() {
        viewModelScope.launch {
            _isLoading.value = true
            _currentPoem.value = repository.getRandomPoem()
            _isLoading.value = false
        }
    }

    fun toggleFavorite() {
        val poem = _currentPoem.value ?: return
        viewModelScope.launch {
            repository.toggleFavorite(poem.id)
            refreshCurrentPoem()
        }
    }

    private suspend fun refreshCurrentPoem() {
        val poem = _currentPoem.value ?: return
        _currentPoem.value = repository.getPoemById(poem.id)
    }

    fun getShareText(): String {
        val poem = _currentPoem.value ?: return ""
        return "「${poem.title}」\n${poem.content}\n\n——${poem.author}《${poem.title}》"
    }

    fun getShareImageUri(context: Context): Uri? {
        val poem = _currentPoem.value ?: return null

        val width = 800
        val padding = 64
        val textWidth = width - padding * 2

        val titlePaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.parseColor("#1D1B18")
            textSize = 42f
            typeface = android.graphics.Typeface.SERIF
            textAlign = android.graphics.Paint.Align.CENTER
        }
        val authorPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.parseColor("#4C443C")
            textSize = 24f
            textAlign = android.graphics.Paint.Align.CENTER
        }
        val contentPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.parseColor("#1D1B18")
            textSize = 26f
            typeface = android.graphics.Typeface.SERIF
        }

        // Build content text with line breaks
        val contentText = poem.content.replace("\\n", "\n")

        // Measure heights
        val titleLayout = StaticLayout(poem.title, titlePaint, textWidth, Layout.Alignment.ALIGN_CENTER, 1f, 0f, false)
        val authorLayout = StaticLayout(poem.author, authorPaint, textWidth, Layout.Alignment.ALIGN_CENTER, 1f, 0f, false)
        val contentLayout = StaticLayout(contentText, contentPaint, textWidth, Layout.Alignment.ALIGN_CENTER, 1.8f, 0f, false)

        val totalHeight = padding * 2 + titleLayout.height + 40 + authorLayout.height + 60 + contentLayout.height

        val bitmap = Bitmap.createBitmap(width, totalHeight.toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(android.graphics.Color.parseColor("#FAF6F1"))

        // Draw dynasty watermark background
        val dynastyPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.parseColor("#D97757")
            alpha = 16  // ~6% opacity
            textSize = 180f
            typeface = android.graphics.Typeface.SERIF
            textAlign = android.graphics.Paint.Align.RIGHT
        }
        canvas.save()
        canvas.translate(width - 20f, totalHeight - 30f)
        canvas.drawText(poem.dynasty.take(2), 0f, 0f, dynastyPaint)
        canvas.restore()

        // Draw title
        canvas.save()
        canvas.translate(padding.toFloat(), padding.toFloat())
        titleLayout.draw(canvas)
        canvas.restore()

        // Draw author
        canvas.save()
        canvas.translate(padding.toFloat(), padding + titleLayout.height + 40f)
        authorLayout.draw(canvas)
        canvas.restore()

        // Draw content
        canvas.save()
        canvas.translate(padding.toFloat(), padding + titleLayout.height + 40 + authorLayout.height + 60f)
        contentLayout.draw(canvas)
        canvas.restore()

        // Save to cache
        val dir = File(context.cacheDir, "poem_share")
        dir.mkdirs()
        val file = File(dir, "poem_${poem.id}.png")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }

        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }

    fun cleanupShareImage(context: Context) {
        val dir = File(context.cacheDir, "poem_share")
        if (dir.exists()) {
            dir.deleteRecursively()
        }
    }
}
