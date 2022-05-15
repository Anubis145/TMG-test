package com.example.tmg_test.ui.main.players

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.tmg_test.R
import com.example.tmg_test.model.PlayerModel
import com.example.tmg_test.ui.adapter.PlayersListAdapter
import com.example.tmg_test.ui.base.BaseFragment
import com.example.tmg_test.utils.observeFlow
import kotlinx.android.synthetic.main.fragment_players.*

class PlayersFragment : BaseFragment() {

    val vm: PlayersVM by viewModels()

    private lateinit var playersAdapter: PlayersListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_players, container, false)
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

    private fun event(event: PlayersVM.PlayersEvent) {
        when(event){
            is PlayersVM.PlayersEvent.OpenFragment-> {
                findNavController().navigate(event.fragmentId)
            }
            is PlayersVM.PlayersEvent.Error->
                Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun render(viewState: PlayersVM.PlayersViewState) {
        when(viewState){
            is PlayersVM.PlayersViewState.PlayersList->{
                initPlayersRecyclerView(viewState.playersList)
            }
        }
    }

    private fun initPlayersRecyclerView(playersList: List<PlayerModel>) {
        playersAdapter = PlayersListAdapter(playersList)

        vPlayersFragmentPlayerList.adapter = playersAdapter
    }

    private fun initListeners(){
        vPlayersFragmentAddPlayer.setOnClickListener { vm.onAddPlayerClick() }
    }
}