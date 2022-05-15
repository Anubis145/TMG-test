package com.example.tmg_test.ui.main.leaderList

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.tmg_test.R
import com.example.tmg_test.model.PlayerModel
import com.example.tmg_test.ui.adapter.PlayersListAdapter
import com.example.tmg_test.ui.base.BaseFragment
import com.example.tmg_test.ui.dialog.SortTypeDialog
import com.example.tmg_test.utils.observeFlow
import kotlinx.android.synthetic.main.fragment_leader_list.*


class LeaderListFragment : BaseFragment() {

    val vm: LeaderListVM by viewModels()

    private lateinit var playersAdapter: PlayersListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_leader_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        initPlayersRecycler()

        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        vm.onResume()
    }

    private fun observeViewModel() {
        observeFlow(vm.viewState, ::render)
        observeFlow(vm.event, ::event)
    }

    private fun event(event: LeaderListVM.LeadersEvent?) {
        when(event){
            is LeaderListVM.LeadersEvent.ShowSortTypeDialog -> {
                val dialog = SortTypeDialog(event.selectedSortType) { selectedSortType ->
                    vm.onSortTypeSaved(selectedSortType)
                }
                dialog.show(childFragmentManager, dialog.tag)
            }
            is LeaderListVM.LeadersEvent.Error -> {
                Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun render(viewState: LeaderListVM.LeadersViewState?) {
        when(viewState){
            is LeaderListVM.LeadersViewState.PlayersList -> {
                playersAdapter.items = viewState.playersList
                playersAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun initPlayersRecycler(playerList: List<PlayerModel> = listOf()) {
        playersAdapter = PlayersListAdapter(playerList)

        vLeaderListFragmentRecycler.adapter = playersAdapter
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