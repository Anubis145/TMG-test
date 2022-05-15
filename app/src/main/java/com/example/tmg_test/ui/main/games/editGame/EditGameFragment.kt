package com.example.tmg_test.ui.main.games.editGame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.tmg_test.R
import com.example.tmg_test.model.PlayerModel
import com.example.tmg_test.ui.base.BaseFragment
import com.example.tmg_test.utils.doOnTextChange
import com.example.tmg_test.utils.observeFlow
import com.example.tmg_test.utils.setText
import kotlinx.android.synthetic.main.fragment_edit_game.*

class EditGameFragment : BaseFragment() {

    val vm: EditGameVM by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initListeners()
        observeViewModel()
    }

    private fun observeViewModel() {
        observeFlow(vm.viewState, ::render)
        observeFlow(vm.event, ::event)
    }

    private fun event(event: EditGameVM.EditGameEvent?) {
        when(event){
            is EditGameVM.EditGameEvent.CloseFragment ->
                findNavController().popBackStack()
            is EditGameVM.EditGameEvent.Error -> {
                Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun render(viewState: EditGameVM.EditGameViewState?) {
        when(viewState){
            is EditGameVM.EditGameViewState.SelectedEditGameModel ->{
                vEditGameFragmentFirstPlayerScore.setText(viewState.selectedModel.firstPlayerScore.toString())
                vEditGameFragmentSecondPlayerScore.setText(viewState.selectedModel.secondPlayerScore.toString())

                initFirstPlayerSpinner(viewState.playersList, viewState.selectedModel.firstPlayer)
                initSecondPlayerSpinner(viewState.playersList, viewState.selectedModel.secondPlayer)
            }
            is EditGameVM.EditGameViewState.PlayersList ->{
                initFirstPlayerSpinner(viewState.playersList)
                initSecondPlayerSpinner(viewState.playersList)
            }
        }
    }

    private fun initSecondPlayerSpinner(
        playersList: List<PlayerModel>,
        selectedPlayerModel: PlayerModel? = null
    ) {
        val playersNamesList = playersList.map { it.name }
        val playersAdapter = ArrayAdapter(requireContext(), R.layout.spinner_custom, playersNamesList)
        vEditGameFragmentSecondPlayersSpinner.adapter = playersAdapter

        vEditGameFragmentSecondPlayersSpinner.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                vm.onSecondPlayerSelect(playersList[position])
            }
        })

        vEditGameFragmentSecondPlayersSpinner.setSelection(playersList.indexOf(selectedPlayerModel))
    }

    private fun initFirstPlayerSpinner(
        playersList: List<PlayerModel>,
        selectedPlayerModel: PlayerModel? = null
    ) {
        val playersNamesList = playersList.map { it.name }
        val playersAdapter = ArrayAdapter(requireContext(), R.layout.spinner_custom, playersNamesList)
        vEditGameFragmentFirstPlayersSpinner.adapter = playersAdapter

        vEditGameFragmentFirstPlayersSpinner.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                vm.onFirstPlayerSelect(playersList[position])
            }
        })

        vEditGameFragmentFirstPlayersSpinner.setSelection(playersList.indexOf(selectedPlayerModel))
    }

    private fun initListeners() {
        vEditGameFragmentFirstPlayerScore.doOnTextChange { vm.onFirstPlayerScoreChange(it) }
        vEditGameFragmentSecondPlayerScore.doOnTextChange { vm.onSecondPlayerScoreChange(it) }
        vEditGameFragmentSave.setOnClickListener { vm.onSaveClick() }
        vEditGameFragmentCancel.setOnClickListener { vm.onCancelClick() }
    }
}