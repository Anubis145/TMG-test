package com.example.tmg_test.ui.main.leaderList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmg_test.model.GameModel
import com.example.tmg_test.model.PlayerModel
import com.example.tmg_test.repository.GamesRepository
import com.example.tmg_test.repository.LocalRepository
import com.example.tmg_test.repository.PlayersRepository
import com.example.tmg_test.repository.SchedulersRepository
import com.example.tmg_test.utils.SORT_TYPE_GAMES
import com.example.tmg_test.utils.SORT_TYPE_WINS
import com.example.tmg_test.utils.emitFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderListViewModel @Inject constructor(
    private val schedulersRepository: SchedulersRepository,
    private val gamesRepository: GamesRepository,
    private val playersRepository: PlayersRepository,
    private val localRepository: LocalRepository,
) : ViewModel() {

    private val _viewState = MutableStateFlow<LeadersViewState>(LeadersViewState.Default)
    val viewState = _viewState.asSharedFlow()

    private val _event = MutableSharedFlow<LeadersEvent>()
    val event = _event.asSharedFlow()

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    private var allPlayersList = listOf<PlayerModel>()
    private var allGamesList = listOf<GameModel>()

    init {
        initData()
    }

    private fun initData() {
        getLeadersList(localRepository.leaderSortType)
    }

    fun onSortByClick() {
        emitFlow(_event, LeadersEvent.ShowSortTypeDialog(localRepository.leaderSortType))
    }

    fun onSortTypeChange(newSelectedSortType: String) {
        localRepository.leaderSortType = newSelectedSortType
        getLeadersList(newSelectedSortType)
    }

    private fun getLeadersList(newSelectedSortType: String) {
        val disposable = playersRepository.getAll()
            .compose(schedulersRepository.flowableTransformer())
            .flatMap {
                allPlayersList = it
                gamesRepository.getAll()
            }
            .subscribe({
                viewModelScope.launch {
                    allGamesList = it

                    val leadersList: List<PlayerModel> = when (newSelectedSortType) {
                        SORT_TYPE_WINS -> {
                            sortPlayersByWins(allPlayersList, allGamesList)
                        }
                        SORT_TYPE_GAMES -> {
                            sortPlayersByGames(allPlayersList, allGamesList)
                        }
                        else -> listOf()
                    }

                    val playerListViewState = LeadersViewState.PlayersList(leadersList)

                    _viewState.emit(playerListViewState)
                }
            }, ::error)
        compositeDisposable.add(disposable)
    }

    private fun error(t: Throwable) {
        emitFlow(_event, LeadersEvent.Error(t.message))
    }

    private fun sortPlayersByWins(
        allPlayersList: List<PlayerModel>,
        allGamesList: List<GameModel>
    ): List<PlayerModel> {
        val playerAndWinPair = mutableListOf<Pair<PlayerModel, List<GameModel>>>()

        allPlayersList.forEach { player ->
            val playedGames =
                allGamesList.filter { it.firstPlayer == player || it.secondPlayer == player }
            val wonGames = playedGames.filter {
                if (it.firstPlayer == player) {
                    it.firstPlayerScore > it.secondPlayerScore
                } else {
                    it.secondPlayerScore > it.firstPlayerScore
                }
            }

            playerAndWinPair.add(Pair(player, wonGames))
        }

        playerAndWinPair.sortByDescending { it.second.size }

        return playerAndWinPair.map { it.first }
    }

    private fun sortPlayersByGames(
        allPlayersList: List<PlayerModel>,
        allGamesList: List<GameModel>
    ): List<PlayerModel> {
        val playerAndGamesPair = mutableListOf<Pair<PlayerModel, List<GameModel>>>()

        allPlayersList.forEach { player ->
            val playedGames =
                allGamesList.filter { it.firstPlayer == player || it.secondPlayer == player }
            playerAndGamesPair.add(Pair(player, playedGames))
        }

        playerAndGamesPair.sortByDescending { it.second.size }

        return playerAndGamesPair.map { it.first }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    sealed class LeadersViewState {
        object Default : LeadersViewState()
        data class PlayersList(var playersList: List<PlayerModel>) : LeadersViewState()
    }

    sealed class LeadersEvent {
        data class ShowSortTypeDialog(val selectedSortType: String) : LeadersEvent()
        data class Error(var message: String?) : LeadersEvent()
    }
}
