package yin_kio.garbage_clean.presentation.garbage_list

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import yin_kio.garbage_clean.domain.use_cases.GarbageFilesUseCases

class ViewModel(
    private val useCases: GarbageFilesUseCases,
    private val coroutineScope: CoroutineScope,
    presenter: Presenter
) : GarbageFilesUseCases by useCases{


    private val _state = MutableStateFlow(ScreenState(
        size = presenter.presentUnknownSize(),
        buttonText = presenter.presentButtonText(false),
        garbageGroups = presenter.presentGarbageWithoutPermission(),
        isShowPermissionRequired = false
    ))
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