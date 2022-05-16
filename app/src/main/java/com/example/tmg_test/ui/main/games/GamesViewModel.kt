package com.example.tmg_test.ui.main.games

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmg_test.R
import com.example.tmg_test.model.GameModel
import com.example.tmg_test.repository.GamesRepository
import com.example.tmg_test.repository.LocalRepository
import com.example.tmg_test.repository.SchedulersRepository
import com.example.tmg_test.utils.emitFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
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
    private val localRepository: LocalRepository,
) : ViewModel() {

    private val _viewState = MutableStateFlow<GamesViewState>(GamesViewState.Default)
    val viewState = _viewState.asStateFlow()

    private val _event = MutableSharedFlow<GamesEvent>()
    val event = _event.asSharedFlow()

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
        getGamesRecords()
    }

    fun onRecordGameClick() = viewModelScope.launch {
        localRepository.selectedEditGameRecord = null
        _event.emit(GamesEvent.OpenFragment(R.id.editGameFragment))
    }

    private fun getGamesRecords() {
        val disposable = gamesRepository.getAll()
            .compose(schedulersRepository.flowableTransformer())
            .subscribe({
                emitFlow(_viewState, GamesViewState.GamesList(it))
            }, ::error)
        compositeDisposable?.add(disposable)
    }

    private fun error(t: Throwable) = viewModelScope.launch {
        _event.emit(GamesEvent.Error(t.message))
    }

    fun onGameItemClick(gameModel: GameModel) = viewModelScope.launch {
        localRepository.selectedEditGameRecord = gameModel
        _event.emit(GamesEvent.OpenFragment(R.id.editGameFragment))
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    sealed class GamesViewState {
        object Default : GamesViewState()
        data class GamesList(var gamesList: List<GameModel>) : GamesViewState()
    }

    sealed class GamesEvent {
        data class OpenFragment(var fragmentId: Int) : GamesEvent()
        data class Error(var message: String?) : GamesEvent()
    }
}
