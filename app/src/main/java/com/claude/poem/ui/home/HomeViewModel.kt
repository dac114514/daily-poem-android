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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

data class SharePreview(val bitmap: Bitmap, val uri: Uri)

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

    suspend fun prepareSharePreview(context: Context): SharePreview? {
        val poem = _currentPoem.value ?: return null
        cleanupShareImage(context)
        val bitmap = withContext(Dispatchers.Default) { renderShareBitmap(poem) }
        val uri = saveShareBitmap(context, bitmap, poem.id)
        return SharePreview(bitmap, uri)
    }

    fun cleanupShareImage(context: Context) {
        val dir = File(context.cacheDir, SHARE_CACHE_DIR)
        if (dir.exists()) {
            dir.deleteRecursively()
        }
    }

    private fun saveShareBitmap(context: Context, bitmap: Bitmap, poemId: Long): Uri {
        val dir = File(context.cacheDir, SHARE_CACHE_DIR)
        dir.mkdirs()
        val file = File(dir, "poem_${poemId}.png")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file,
        )
    }

    private fun renderShareBitmap(poem: Poem): Bitmap {
        val textAreaWidth = CANVAS_WIDTH - PADDING * 2

        val titlePaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.parseColor(ShareColors.TEXT_PRIMARY)
            textSize = 34f
            typeface = android.graphics.Typeface.SERIF
            textAlign = android.graphics.Paint.Align.CENTER
        }
        val authorPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.parseColor(ShareColors.TEXT_SECONDARY)
            textSize = 18f
            typeface = android.graphics.Typeface.SANS_SERIF
            textAlign = android.graphics.Paint.Align.CENTER
        }
        val contentPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.parseColor(ShareColors.TEXT_PRIMARY)
            textSize = 22f
            typeface = android.graphics.Typeface.SERIF
        }
        val dynastyPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.parseColor(ShareColors.ACCENT)
            alpha = 40
            textSize = 110f
            typeface = android.graphics.Typeface.SERIF
            textAlign = android.graphics.Paint.Align.RIGHT
        }
        val signaturePaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.parseColor(ShareColors.TEXT_SECONDARY)
            textSize = 14f
            typeface = android.graphics.Typeface.SERIF
            textAlign = android.graphics.Paint.Align.CENTER
        }
        val decoPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.parseColor(ShareColors.ACCENT)
        }

        val titleLayout = buildLayout(poem.title, titlePaint, textAreaWidth, 1f, 0f)
        val authorLayout = buildLayout(poem.author, authorPaint, textAreaWidth, 1f, 0f)
        val contentLayout = buildLayout(poem.content, contentPaint, textAreaWidth, 1.7f, 0f)

        val totalHeight = (
            PADDING +
                titleLayout.height + TITLE_GAP +
                authorLayout.height + AUTHOR_GAP +
                contentLayout.height + WATERMARK_GAP +
                BOTTOM_PADDING
            ).toInt()

        val bitmap = Bitmap.createBitmap(CANVAS_WIDTH, totalHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(android.graphics.Color.parseColor(ShareColors.BG))

        canvas.drawRect(
            PADDING.toFloat(),
            TOP_DECO_BAR_TOP,
            (PADDING + TOP_DECO_BAR_W).toFloat(),
            TOP_DECO_BAR_TOP + TOP_DECO_BAR_H,
            decoPaint,
        )

        var y = PADDING.toFloat()

        canvas.save()
        canvas.translate(PADDING.toFloat(), y)
        titleLayout.draw(canvas)
        canvas.restore()
        y += titleLayout.height + TITLE_GAP

        canvas.save()
        canvas.translate(PADDING.toFloat(), y)
        authorLayout.draw(canvas)
        canvas.restore()
        y += authorLayout.height + AUTHOR_GAP

        canvas.save()
        canvas.translate(PADDING.toFloat(), y)
        contentLayout.draw(canvas)
        canvas.restore()

        val watermarkY = (totalHeight - BOTTOM_PADDING).toFloat()
        canvas.drawText(
            poem.dynasty.take(2),
            (CANVAS_WIDTH - 40).toFloat(),
            watermarkY,
            dynastyPaint,
        )

        canvas.drawText(
            "—  每日诗文",
            (CANVAS_WIDTH / 2).toFloat(),
            watermarkY,
            signaturePaint,
        )

        return bitmap
    }

    private fun buildLayout(
        text: String,
        paint: TextPaint,
        width: Int,
        spacingMultiplier: Float,
        spacingAdd: Float,
    ): StaticLayout {
        return StaticLayout.Builder
            .obtain(text, 0, text.length, paint, width)
            .setAlignment(Layout.Alignment.ALIGN_CENTER)
            .setLineSpacing(spacingAdd, spacingMultiplier)
            .setIncludePad(false)
            .build()
    }

    private companion object {
        const val KEY_POEM_ID = "current_poem_id"
        const val SHARE_CACHE_DIR = "poem_share"

        const val CANVAS_WIDTH = 800
        const val PADDING = 56
        const val TOP_DECO_BAR_TOP = 40f
        const val TOP_DECO_BAR_W = 64f
        const val TOP_DECO_BAR_H = 4f
        const val TITLE_GAP = 28f
        const val AUTHOR_GAP = 40f
        const val WATERMARK_GAP = 24f
        const val BOTTOM_PADDING = 56f
    }

    private object ShareColors {
        const val BG = "#F5F0EB"
        const val TEXT_PRIMARY = "#1D1B18"
        const val TEXT_SECONDARY = "#4C443C"
        const val ACCENT = "#D97757"
    }
}
