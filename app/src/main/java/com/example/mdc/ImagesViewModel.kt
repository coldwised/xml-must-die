package com.example.mdc

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.repository.Repository
import com.example.mdc.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ImagesViewModel @Inject constructor(
    private val repo: Repository,
): ViewModel() {

    private val _state = MutableStateFlow(listOf<String>())
    val state = _state.asStateFlow()

    init {
        init()
    }

    private fun init() {
        viewModelScope.launch {
            repo.getImagesUrlList().collect {
                _state.value = (it as Resource.Success).data
            }
        }
    }
}