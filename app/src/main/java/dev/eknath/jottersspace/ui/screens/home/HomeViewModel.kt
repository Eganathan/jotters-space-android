package dev.eknath.jottersspace.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.eknath.jottersspace.JotsRepository
import dev.eknath.jottersspace.entities.ApiResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: JotsRepository
) : ViewModel() {

    private val _bulkJots = MutableStateFlow<ApiResponse?>(value = null)
    val bulkJots = _bulkJots.asStateFlow()

    private fun fetchJots() {
        viewModelScope.launch {
            _bulkJots.value = repository.getBulkJots()
        }
    }

    init {
        fetchJots()
    }
}
