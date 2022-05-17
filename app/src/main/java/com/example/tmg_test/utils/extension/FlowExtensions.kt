package com.example.tmg_test.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

fun <T> Fragment.observeFlow(flow: Flow<T>, observer: Observer<T>){
    this.viewLifecycleOwner.lifecycleScope.launchWhenResumed {
        flow.collectLatest {
            observer.onChanged(it)
        }
    }
}

fun <T> ViewModel.emitFlow(flow: MutableSharedFlow<T>, any: T) {
    this.viewModelScope.launch {
        flow.emit(any)
    }
}

fun <T> ViewModel.emitFlow(flow: MutableStateFlow<T>, any: T) {
    this.viewModelScope.launch {
        flow.emit(any)
    }
}
