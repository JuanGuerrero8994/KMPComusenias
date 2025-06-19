package org.example.project.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.example.project.data.core.Resource

open class BaseViewModel:ViewModel() {
    // Lógica común para manejar el resultado del use case
    protected fun <T> fetchData(
        flowCollector: MutableStateFlow<Resource<T>>,
        block: suspend () -> Flow<Resource<T>>
    ) {
        viewModelScope.launch {
            block().collect {
                flowCollector.value = it
            }
        }
    }
}