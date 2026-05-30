package com.example.androidstarter.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidstarter.data.model.Poem
import com.example.androidstarter.data.repository.PoemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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
}
