package com.example.tmg_test.ui.main.games

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.tmg_test.R
import com.example.tmg_test.model.GameModel
import com.example.tmg_test.ui.adapter.GamesAdapter
import com.example.tmg_test.ui.base.BaseFragment
import com.example.tmg_test.utils.observeFlow
import kotlinx.android.synthetic.main.fragment_games.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GamesFragment : BaseFragment() {

    val vm: GamesVM by viewModels()

    lateinit var gamesAdapter: GamesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_games, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initListeners()
        observeViewModel()
    }

    private fun observeViewModel() {
        observeFlow(vm.viewState, ::render)
        observeFlow(vm.event, ::event)
    }

    private fun event(event: GamesVM.GamesEvent?) {
        when(event){
            is GamesVM.GamesEvent.Error ->
                Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
            is GamesVM.GamesEvent.OpenFragment -> {
                findNavController().navigate(R.id.gamesFragment_to_recordGame)
            }
        }
    }

    private fun render(viewState: GamesVM.GamesViewState?) {
        when(viewState){
            is GamesVM.GamesViewState.GamesList -> {
                initGamesReecycler(viewState.gamesList)
            }
        }
    }

    private fun initGamesReecycler(gamesList: List<GameModel>) {
        gamesAdapter = GamesAdapter(gamesList){
            vm.onGameItemClick(it)
        }

        vGamesFragmentGamesRecycler.adapter = gamesAdapter
    }

    private fun initListeners() {
        vGamesFragmentRecordGame.setOnClickListener { vm.onRecordGameClick() }
    }
}