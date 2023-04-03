package yin_kio.garbage_clean.presentation

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import yin_kio.garbage_clean.domain.use_cases.GarbageFilesUseCases

class ViewModel(
    private val useCases: GarbageFilesUseCases,
    private val coroutineScope: CoroutineScope
) : GarbageFilesUseCases by useCases{

    private val _state = MutableStateFlow(ScreenState())
    val state: StateFlow<ScreenState> = _state.asStateFlow()

    private val _commands = MutableSharedFlow<Command>()
    val commands = _commands.asSharedFlow()


    fun update(newState: (oldState: ScreenState) -> ScreenState){
        Log.d("!!!", "update")
        _state.value = newState(_state.value)
    }

    fun sendCommand(command: Command){
        coroutineScope.launch {
            _commands.emit(command)
        }
    }

}