package com.example.tmg_test.ui.main.games.editGame

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.tmg_test.R
import com.example.tmg_test.model.GameModel
import com.example.tmg_test.model.PlayerModel
import com.example.tmg_test.repository.*
import com.example.tmg_test.ui.base.BaseViewModel
import com.example.tmg_test.utils.EDIT_GAME_EXTRA_SELECTED_GAME
import com.example.tmg_test.utils.EditScreenState
import com.example.tmg_test.utils.emitFlow
import com.example.tmg_test.utils.extension.addToComposite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditGameViewModel @Inject constructor(
    private val schedulersRepository: SchedulersRepository,
    private val gamesRepository: GamesRepository,
    private val playersRepository: PlayersRepository,
    private val resourceRepository: ResourceRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val _viewState = MutableStateFlow<EditGameViewState>(EditGameViewState.Default)
    val viewState = _viewState.asSharedFlow()

    private val _event = MutableSharedFlow<EditGameEvent>()
    val event = _event.asSharedFlow()

    private var selectedGameModel: GameModel? = null
    private var state = EditScreenState.EDIT
    private var allPlayersList = listOf<PlayerModel>()

    private var selectedFirstPlayer: PlayerModel? = selectedGameModel?.firstPlayer
    private var selectedSecondPlayer: PlayerModel? = selectedGameModel?.secondPlayer
    private var firstPlayerScore = selectedGameModel?.firstPlayerScore ?: 0
    private var secondPlayerScore = selectedGameModel?.secondPlayerScore ?: 0

    init {
        initData()
    }

    private fun initData() {
        selectedGameModel = savedStateHandle.get(EDIT_GAME_EXTRA_SELECTED_GAME) as GameModel?
        state = if (selectedGameModel == null) EditScreenState.CREATE else EditScreenState.EDIT

        playersRepository.getAll()
            .compose(schedulersRepository.flowableTransformer())
            .subscribe({

                allPlayersList = it

                if (state == EditScreenState.EDIT) {
                    emitFlow(
                        _viewState,
                        EditGameViewState.SelectedEditGameModel(
                            selectedModel = selectedGameModel!!,
                            playersListNames = it.map { it.name }
                        )
                    )
                } else {
                    emitFlow(_viewState, EditGameViewState.PlayersList(playersListNames = it.map { it.name }))
                }
            }, ::error).addToComposite(compositeDisposable)
    }

    fun onSaveClick() {
        if (!isFieldsValid()) return

        when (state) {
            EditScreenState.EDIT -> {
                val selectedGame = selectedGameModel!!

                selectedGame.firstPlayer = selectedFirstPlayer!!
                selectedGame.secondPlayer = selectedSecondPlayer!!
                selectedGame.firstPlayerScore = firstPlayerScore
                selectedGame.secondPlayerScore = secondPlayerScore

                gamesRepository.update(selectedGame)
                    .compose(schedulersRepository.completableTransformer())
                    .subscribe({
                        emitFlow(_event, EditGameEvent.CloseFragment)
                    }, ::error).addToComposite(compositeDisposable)
            }
            EditScreenState.CREATE -> {
                val newGameModel = GameModel(
                    0,
                    selectedFirstPlayer!!,
                    firstPlayerScore,
                    selectedSecondPlayer!!,
                    secondPlayerScore
                )

                gamesRepository.insert(newGameModel)
                    .compose(schedulersRepository.singleTransformer())
                    .subscribe({
                        emitFlow(_event, EditGameEvent.CloseFragment)
                    }, ::error).addToComposite(compositeDisposable)
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
