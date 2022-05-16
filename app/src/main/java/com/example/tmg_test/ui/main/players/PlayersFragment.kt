package com.example.tmg_test.ui.main.players

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.tmg_test.databinding.FragmentPlayersBinding
import com.example.tmg_test.model.PlayerModel
import com.example.tmg_test.ui.adapter.PlayersListAdapter
import com.example.tmg_test.ui.base.BaseFragment
import com.example.tmg_test.utils.observeFlow

class PlayersFragment : BaseFragment() {

    val vm: PlayersViewModel by viewModels()
    lateinit var bind: FragmentPlayersBinding

    private lateinit var playersAdapter: PlayersListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = FragmentPlayersBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeViewModel()
        initListeners()
    }

    override fun onResume() {
        super.onResume()
        vm.onResume()
    }

    private fun observeViewModel() {
        observeFlow(vm.viewState, ::render)
        observeFlow(vm.event, ::event)
    }

    private fun event(event: PlayersViewModel.PlayersEvent) {
        when (event) {
            is PlayersViewModel.PlayersEvent.OpenFragment -> {
                findNavController().navigate(event.fragmentId)
            }
            is PlayersViewModel.PlayersEvent.Error ->
                Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun render(viewState: PlayersViewModel.PlayersViewState) {
        when (viewState) {
            is PlayersViewModel.PlayersViewState.PlayersList -> {
                initPlayersRecyclerView(viewState.playersList)
            }
        }
    }

    private fun initPlayersRecyclerView(playersList: List<PlayerModel>) {
        playersAdapter = PlayersListAdapter(playersList)

        bind.playersFragmentPlayerList.adapter = playersAdapter
    }

    private fun initListeners() {
        bind.playersFragmentAddPlayer.setOnClickListener { vm.onAddPlayerClick() }
    }
}