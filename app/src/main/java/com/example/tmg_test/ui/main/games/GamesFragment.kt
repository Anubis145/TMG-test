package com.example.tmg_test.ui.main.games

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.tmg_test.R
import com.example.tmg_test.databinding.FragmentGamesBinding
import com.example.tmg_test.model.GameModel
import com.example.tmg_test.ui.adapter.GamesAdapter
import com.example.tmg_test.ui.base.BaseFragment
import com.example.tmg_test.utils.observeFlow

class GamesFragment : BaseFragment() {

    val vm: GamesViewModel by viewModels()
    private lateinit var bind: FragmentGamesBinding

    private lateinit var gamesAdapter: GamesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = FragmentGamesBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initListeners()
        observeViewModel()
    }

    private fun observeViewModel() {
        observeFlow(vm.viewState, ::render)
        observeFlow(vm.event, ::event)
    }

    private fun event(event: GamesViewModel.GamesEvent?) {
        when (event) {
            is GamesViewModel.GamesEvent.Error ->
                Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
            is GamesViewModel.GamesEvent.OpenFragment -> {
                findNavController().navigate(R.id.editGameFragment, event.args)
            }
        }
    }

    private fun render(viewState: GamesViewModel.GamesViewState?) {
        when (viewState) {
            is GamesViewModel.GamesViewState.GamesList -> {
                initGamesReecycler(viewState.gamesList)
            }
        }
    }

    private fun initGamesReecycler(gamesList: List<GameModel>) {
        gamesAdapter = GamesAdapter(gamesList) {
            vm.onGameItemClick(it)
        }

        bind.gamesFragmentGamesRecycler.adapter = gamesAdapter
    }

    private fun initListeners() {
        bind.gamesFragmentRecordGame.setOnClickListener { vm.onRecordGameClick() }
    }
}
