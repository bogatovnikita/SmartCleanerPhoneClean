package yin_kio.garbage_clean.presentation.garbage_list

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import yin_kio.garbage_clean.domain.use_case.GarbageFilesUseCase

class ViewModel(
    private val useCases: GarbageFilesUseCase,
    private val coroutineScope: CoroutineScope,
) : GarbageFilesUseCase by useCases{


    private val _state = MutableStateFlow(ScreenState())
    val state: StateFlow<ScreenState> = _state.asStateFlow()

    private val _commands = MutableSharedFlow<Command>()
    val commands = _commands.asSharedFlow()



    fun update(newState: (oldState: ScreenState) -> ScreenState){
        _state.value = newState(_state.value)
    }

    fun sendCommand(command: Command){
        coroutineScope.launch {
            _commands.emit(command)
        }
    }

}