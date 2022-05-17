package com.example.tmg_test.ui.main.games

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.viewModelScope
import com.example.tmg_test.R
import com.example.tmg_test.model.GameModel
import com.example.tmg_test.repository.GamesRepository
import com.example.tmg_test.repository.SchedulersRepository
import com.example.tmg_test.ui.base.BaseViewModel
import com.example.tmg_test.utils.EDIT_GAME_EXTRA_SELECTED_GAME
import com.example.tmg_test.utils.emitFlow
import com.example.tmg_test.utils.extension.addToComposite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GamesViewModel @Inject constructor(
    private val schedulersRepository: SchedulersRepository,
    private val gamesRepository: GamesRepository,
) : BaseViewModel() {

    private val _viewState = MutableStateFlow<GamesViewState>(GamesViewState.Default)
    val viewState = _viewState.asStateFlow()

    private val _event = MutableSharedFlow<GamesEvent>()
    val event = _event.asSharedFlow()

    init {
        getGamesRecords()
    }

    fun onRecordGameClick() = viewModelScope.launch {
        _event.emit(GamesEvent.OpenFragment(R.id.editGameFragment))
    }

    private fun getGamesRecords() {
        gamesRepository.getAll()
            .compose(schedulersRepository.flowableTransformer())
            .subscribe({
                emitFlow(_viewState, GamesViewState.GamesList(it))
            }, ::error).addToComposite(compositeDisposable)
    }

    private fun error(t: Throwable) = viewModelScope.launch {
        _event.emit(GamesEvent.Error(t.message))
    }

    fun onGameItemClick(gameModel: GameModel) = viewModelScope.launch {
        val args = bundleOf(Pair(EDIT_GAME_EXTRA_SELECTED_GAME, gameModel))
        _event.emit(GamesEvent.OpenFragment(R.id.editGameFragment, args))
    }

    sealed class GamesViewState {
        object Default : GamesViewState()
        data class GamesList(var gamesList: List<GameModel>) : GamesViewState()
    }

    sealed class GamesEvent {
        data class OpenFragment(var fragmentId: Int, var args: Bundle? = null) : GamesEvent()
        data class Error(var message: String?) : GamesEvent()
    }
}
