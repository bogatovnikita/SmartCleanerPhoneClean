package com.smart.cleaner.phoneclean.presentation.ui.base

import androidx.annotation.UiThread
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


abstract class BaseViewModel<T>(initState: T) : ViewModel() {

    protected val _screenState: MutableStateFlow<T> = MutableStateFlow(initState)
    val screenState = _screenState.asStateFlow()

    @UiThread
    protected inline fun updateState(newValue: (T) -> T) {
        _screenState.value = newValue.invoke(screenState.value)
    }

}