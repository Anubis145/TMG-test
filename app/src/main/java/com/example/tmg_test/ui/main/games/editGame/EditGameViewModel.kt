package com.example.tmg_test.ui.main.games.editGame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmg_test.R
import com.example.tmg_test.model.GameModel
import com.example.tmg_test.model.PlayerModel
import com.example.tmg_test.repository.*
import com.example.tmg_test.utils.EditScreenState
import com.example.tmg_test.utils.emitFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditGameViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    private val schedulersRepository: SchedulersRepository,
    private val gamesRepository: GamesRepository,
    private val playersRepository: PlayersRepository,
    private val resourceRepository: ResourceRepository,
) : ViewModel() {
    private val _viewState = MutableStateFlow<EditGameViewState>(EditGameViewState.Default)
    val viewState = _viewState.asSharedFlow()

    private val _event = MutableSharedFlow<EditGameEvent>()
    val event = _event.asSharedFlow()

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var selectedGameModel = localRepository.selectedEditGameRecord
    private var state = EditScreenState.EDIT
    private var allPlayersList = listOf<PlayerModel>()

    private var selectedFirstPlayer: PlayerModel? = selectedGameModel?.firstPlayer
    private var selectedSecondPlayer: PlayerModel? = selectedGameModel?.secondPlayer
    private var firstPlayerScore = selectedGameModel?.firstPlayerScore ?: 0
    private var secondPlayerScore = selectedGameModel?.secondPlayerScore ?: 0

    init {
        state =
            if (localRepository.selectedEditGameRecord == null) EditScreenState.CREATE else EditScreenState.EDIT

        initData()
    }

    private fun initData() {
        val disposable = playersRepository.getAll()
            .compose(schedulersRepository.flowableTransformer())
            .subscribe({

                allPlayersList = it

                if (state == EditScreenState.EDIT) {
                    emitFlow(
                        _viewState,
                        EditGameViewState.SelectedEditGameModel(
                            selectedModel = localRepository.selectedEditGameRecord!!,
                            playersListNames = it.map { it.name }
                        )
                    )
                } else {
                    emitFlow(_viewState, EditGameViewState.PlayersList(playersListNames = it.map { it.name }))
                }
            }, ::error)
        compositeDisposable.add(disposable)

    }

    fun onSaveClick() {
        if (!isFieldsValid()) return

        when (state) {
            EditScreenState.EDIT -> {
                val selectedGame = localRepository.selectedEditGameRecord!!

                selectedGame.firstPlayer = selectedFirstPlayer!!
                selectedGame.secondPlayer = selectedSecondPlayer!!
                selectedGame.firstPlayerScore = firstPlayerScore
                selectedGame.secondPlayerScore = secondPlayerScore

                val disposable = gamesRepository.update(selectedGame)
                    .compose(schedulersRepository.completableTransformer())
                    .subscribe({
                        emitFlow(_event, EditGameEvent.CloseFragment)
                    }, ::error)
                compositeDisposable.add(disposable)
            }
            EditScreenState.CREATE -> {
                val newGameModel = GameModel(
                    0,
                    selectedFirstPlayer!!,
                    firstPlayerScore,
                    selectedSecondPlayer!!,
                    secondPlayerScore
                )

                val disposable = gamesRepository.insert(newGameModel)
                    .compose(schedulersRepository.singleTransformer())
                    .subscribe({
                        emitFlow(_event, EditGameEvent.CloseFragment)
                    }, ::error)
                compositeDisposable.add(disposable)
            }
        }
    }

    private fun isFieldsValid(): Boolean {
        var result = true
        if (selectedFirstPlayer == null || selectedSecondPlayer == null) {
            result = false
            emitFlow(
                _event,
                EditGameEvent.Error(resourceRepository.getString(R.string.error_you_should_choose_two_players))
            )
        }
        if (selectedFirstPlayer?.id == selectedSecondPlayer?.id) {
            result = false
            emitFlow(
                _event,
                EditGameEvent.Error(resourceRepository.getString(R.string.error_choose_different_players))
            )
        }
        if (firstPlayerScore == 0 && secondPlayerScore == 0) {
            result = false
            emitFlow(
                _event,
                EditGameEvent.Error(resourceRepository.getString(R.string.error_one_player_one_score_point))
            )
        }
        return result
    }

    fun onFirstPlayerSelect(selectedPlayerPos: Int) {
        if(allPlayersList.isNotEmpty()){
            selectedFirstPlayer = allPlayersList[selectedPlayerPos]
        }
    }

    fun onSecondPlayerSelect(selectedPlayerPos: Int) {
        if(allPlayersList.isNotEmpty()){
            selectedSecondPlayer = allPlayersList[selectedPlayerPos]
        }
    }

    fun onFirstPlayerScoreChange(score: String) {
        firstPlayerScore = score.toIntOrNull() ?: 0
    }

    fun onSecondPlayerScoreChange(score: String) {
        secondPlayerScore = score.toIntOrNull() ?: 0
    }

    fun onCancelClick() = viewModelScope.launch {
        _event.emit(EditGameEvent.CloseFragment)
    }

    private fun error(t: Throwable) = viewModelScope.launch {
        _event.emit(EditGameEvent.Error(t.message))
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    sealed class EditGameViewState {
        object Default : EditGameViewState()
        data class SelectedEditGameModel(
            var selectedModel: GameModel,
            var playersListNames: List<String>
        ) : EditGameViewState()
        data class PlayersList(var playersListNames: List<String>) : EditGameViewState()
    }

    sealed class EditGameEvent {
        object CloseFragment : EditGameEvent()
        data class Error(var message: String?) : EditGameEvent()
    }
}
