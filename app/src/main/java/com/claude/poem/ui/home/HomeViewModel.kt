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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

data class SharePreview(val bitmap: Bitmap, val uri: Uri)

data class CardStack(
    val left: Poem,
    val center: Poem,
    val right: Poem,
)

class HomeViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle,
) : AndroidViewModel(application) {
    private val repository = PoemRepository(application)
    private val statsRepo = StatsRepository(application)

    private val _allPoems = MutableStateFlow<List<Poem>>(emptyList())
    private val _stack = MutableStateFlow<CardStack?>(null)

    val stack: StateFlow<CardStack?> = _stack.asStateFlow()

    val centerPoem: StateFlow<Poem?> = _stack
        .map { it?.center }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val leftPoem: StateFlow<Poem?> = _stack
        .map { it?.left }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val rightPoem: StateFlow<Poem?> = _stack
        .map { it?.right }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            _isLoading.value = true
            val all = repository.getAllPoems()
            _allPoems.value = all
            if (all.isNotEmpty()) {
                val savedCenterId = savedStateHandle.get<Long>(KEY_CENTER_ID)
                _stack.value = buildStack(centerIdHint = savedCenterId)
            }
            _isLoading.value = false
        }
    }

    fun swipeNext() {
        val current = _stack.value ?: return
        val all = _allPoems.value
        if (all.isEmpty()) return

        val newLeft = drawRandom(
            excludingIds = setOf(current.center.id, current.right.id),
            fallback = all,
        )
        val newRight = drawRandom(
            excludingIds = setOf(current.center.id, current.right.id, newLeft.id),
            fallback = all,
        )
        val newStack = CardStack(
            left = newLeft,
            center = current.right,
            right = newRight,
        )
        _stack.value = newStack
        savedStateHandle[KEY_CENTER_ID] = newStack.center.id
        viewModelScope.launch { statsRepo.recordView() }
    }

    fun swipePrev() {
        val current = _stack.value ?: return
        val all = _allPoems.value
        if (all.isEmpty()) return

        val newRight = drawRandom(
            excludingIds = setOf(current.left.id, current.center.id),
            fallback = all,
        )
        val newLeft = drawRandom(
            excludingIds = setOf(current.left.id, current.center.id, newRight.id),
            fallback = all,
        )
        val newStack = CardStack(
            left = newLeft,
            center = current.left,
            right = newRight,
        )
        _stack.value = newStack
        savedStateHandle[KEY_CENTER_ID] = newStack.center.id
        viewModelScope.launch { statsRepo.recordView() }
    }

    fun toggleFavorite() {
        val center = _stack.value?.center ?: return
        viewModelScope.launch {
            repository.toggleFavorite(center.id)
            val updated = repository.getPoemById(center.id) ?: return@launch
            _stack.value = _stack.value?.copy(center = updated)
            _allPoems.value = _allPoems.value.map { if (it.id == updated.id) updated else it }
        }
    }

    suspend fun prepareSharePreview(context: Context): SharePreview? {
        val center = _stack.value?.center ?: return null
        cleanupShareImage(context)
        val bitmap = withContext(Dispatchers.Default) { renderShareBitmap(center) }
        val uri = saveShareBitmap(context, bitmap, center.id)
        return SharePreview(bitmap, uri)
    }

    fun cleanupShareImage(context: Context) {
        val dir = File(context.cacheDir, SHARE_CACHE_DIR)
        if (dir.exists()) {
            dir.deleteRecursively()
        }
    }

    private fun buildStack(centerIdHint: Long?): CardStack {
        val all = _allPoems.value
        val center = centerIdHint
            ?.let { id -> all.firstOrNull { it.id == id } }
            ?: all.random()

        val others = all.filter { it.id != center.id }.shuffled()
        val left = others.getOrNull(0) ?: center
        val right = others.getOrNull(1) ?: others.getOrNull(0) ?: center

        return CardStack(left = left, center = center, right = right)
    }

    private fun drawRandom(excludingIds: Set<Long>, fallback: List<Poem>): Poem {
        val pool = fallback.filter { it.id !in excludingIds }
        return if (pool.isNotEmpty()) pool.random() else fallback.random()
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
        const val KEY_CENTER_ID = "center_poem_id"
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
