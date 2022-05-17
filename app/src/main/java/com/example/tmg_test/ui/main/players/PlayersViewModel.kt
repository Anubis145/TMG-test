package com.example.tmg_test.ui.main.players

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmg_test.R
import com.example.tmg_test.model.PlayerModel
import com.example.tmg_test.repository.PlayersRepository
import com.example.tmg_test.repository.SchedulersRepository
import com.example.tmg_test.ui.base.BaseViewModel
import com.example.tmg_test.utils.emitFlow
import com.example.tmg_test.utils.extension.addToComposite
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayersViewModel @Inject constructor(
    private val playersRepository: PlayersRepository,
    private val schedulersRepository: SchedulersRepository,
) : BaseViewModel() {

    private val _viewState = MutableStateFlow<PlayersViewState>(PlayersViewState.Default)
    val viewState = _viewState.asSharedFlow()

    private val _event = MutableSharedFlow<PlayersEvent>()
    val event = _event.asSharedFlow()

    init {
        getPlayersList()
    }

    private fun getPlayersList() {
        playersRepository.getAll()
            .compose(schedulersRepository.flowableTransformer())
            .subscribe({
                emitFlow(_viewState, PlayersViewState.PlayersList(it))
            }, ::error).addToComposite(compositeDisposable)
    }

    private fun error(t: Throwable) {
        emitFlow(_event, PlayersEvent.Error(t.message))
    }

    fun onAddPlayerClick() = viewModelScope.launch {
        _event.emit(PlayersEvent.OpenFragment(R.id.addPlayerFragment))
    }

    sealed class PlayersViewState {
        object Default : PlayersViewState()
        data class PlayersList(var playersList: List<PlayerModel>) : PlayersViewState()
    }

    sealed class PlayersEvent {
        data class OpenFragment(var fragmentId: Int) : PlayersEvent()
        data class Error(var message: String?) : PlayersEvent()
    }
}
