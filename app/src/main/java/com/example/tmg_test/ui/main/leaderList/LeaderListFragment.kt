package com.example.tmg_test.ui.main.leaderList

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.tmg_test.R
import com.example.tmg_test.databinding.FragmentLeaderListBinding
import com.example.tmg_test.model.PlayerModel
import com.example.tmg_test.ui.adapter.PlayersListAdapter
import com.example.tmg_test.ui.base.BaseFragment
import com.example.tmg_test.ui.dialog.SortTypeDialog
import com.example.tmg_test.utils.observeFlow

class LeaderListFragment : BaseFragment() {

    val vm: LeaderListViewModel by activityViewModels()
    private lateinit var bind: FragmentLeaderListBinding

    private lateinit var playersAdapter: PlayersListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = FragmentLeaderListBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        initPlayersRecycler()

        observeViewModel()
    }

    private fun observeViewModel() {
        observeFlow(vm.viewState, ::render)
        observeFlow(vm.event, ::event)
    }

    private fun event(event: LeaderListViewModel.LeadersEvent?) {
        when (event) {
            is LeaderListViewModel.LeadersEvent.ShowSortTypeDialog -> {
                val dialog = SortTypeDialog()
                dialog.show(childFragmentManager, dialog.tag)
            }
            is LeaderListViewModel.LeadersEvent.Error -> {
                Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun render(viewState: LeaderListViewModel.LeadersViewState?) {
        when (viewState) {
            is LeaderListViewModel.LeadersViewState.PlayersList -> {
                playersAdapter.items = viewState.playersList
                playersAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun initPlayersRecycler(playerList: List<PlayerModel> = listOf()) {
        playersAdapter = PlayersListAdapter(playerList)

        bind.leaderListFragmentRecycler.adapter = playersAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.leader_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.actionSortBy -> {

                vm.onSortByClick()

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
