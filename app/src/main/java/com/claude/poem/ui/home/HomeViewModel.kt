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
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.claude.poem.data.local.StatsRepository
import com.claude.poem.data.model.Poem
import com.claude.poem.data.repository.PoemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class HomeViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle,
) : AndroidViewModel(application) {
    private val repository = PoemRepository(application)
    private val statsRepo = StatsRepository(application)

    private val _currentPoem = MutableStateFlow<Poem?>(null)
    val currentPoem: StateFlow<Poem?> = _currentPoem.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        val savedId = savedStateHandle.get<Long>(KEY_POEM_ID)
        if (savedId != null && savedId > 0) {
            viewModelScope.launch {
                _currentPoem.value = repository.getPoemById(savedId)
                if (_currentPoem.value == null) refreshPoem()
            }
        } else {
            refreshPoem()
        }
    }

    fun refreshPoem() {
        viewModelScope.launch {
            _isLoading.value = true
            val poem = repository.getRandomPoem()
            _currentPoem.value = poem
            if (poem != null) {
                savedStateHandle[KEY_POEM_ID] = poem.id
                statsRepo.recordView()
            }
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

    fun getShareImageUri(context: Context): Uri? {
        val poem = _currentPoem.value ?: return null

        cleanupShareImage(context)

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

        val titleLayout = StaticLayout(poem.title, titlePaint, textWidth, Layout.Alignment.ALIGN_CENTER, 1f, 0f, false)
        val authorLayout = StaticLayout(poem.author, authorPaint, textWidth, Layout.Alignment.ALIGN_CENTER, 1f, 0f, false)
        val contentLayout = StaticLayout(poem.content, contentPaint, textWidth, Layout.Alignment.ALIGN_CENTER, 1.8f, 0f, false)

        val totalHeight = padding * 2 + titleLayout.height + 40 + authorLayout.height + 60 + contentLayout.height

        val bitmap = Bitmap.createBitmap(width, totalHeight.toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(android.graphics.Color.parseColor("#FAF6F1"))

        val dynastyPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.parseColor("#D97757")
            alpha = 16
            textSize = 180f
            typeface = android.graphics.Typeface.SERIF
            textAlign = android.graphics.Paint.Align.RIGHT
        }
        canvas.save()
        canvas.translate(width - 20f, totalHeight - 30f)
        canvas.drawText(poem.dynasty.take(2), 0f, 0f, dynastyPaint)
        canvas.restore()

        canvas.save()
        canvas.translate(padding.toFloat(), padding.toFloat())
        titleLayout.draw(canvas)
        canvas.restore()

        canvas.save()
        canvas.translate(padding.toFloat(), padding + titleLayout.height + 40f)
        authorLayout.draw(canvas)
        canvas.restore()

        canvas.save()
        canvas.translate(padding.toFloat(), padding + titleLayout.height + 40 + authorLayout.height + 60f)
        contentLayout.draw(canvas)
        canvas.restore()

        val dir = File(context.cacheDir, "poem_share")
        dir.mkdirs()
        val file = File(dir, "poem_${poem.id}.png")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        bitmap.recycle()

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

    private companion object {
        const val KEY_POEM_ID = "current_poem_id"
    }
}
