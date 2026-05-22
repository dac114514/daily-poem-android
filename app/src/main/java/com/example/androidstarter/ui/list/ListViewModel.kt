package com.example.androidstarter.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidstarter.data.model.SampleItem
import com.example.androidstarter.data.repository.SampleRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ListViewModel : ViewModel() {

    private val repository = SampleRepository()

    val items: StateFlow<List<SampleItem>> = repository.observeItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
