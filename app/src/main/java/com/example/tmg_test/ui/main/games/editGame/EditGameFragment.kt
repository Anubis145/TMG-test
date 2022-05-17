package com.example.tmg_test.ui.main.games.editGame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.tmg_test.R
import com.example.tmg_test.databinding.FragmentEditGameBinding
import com.example.tmg_test.ui.base.BaseFragment
import com.example.tmg_test.utils.extension.doOnTextChange
import com.example.tmg_test.utils.observeFlow
import com.example.tmg_test.utils.extension.setText

class EditGameFragment : BaseFragment() {

    val vm: EditGameViewModel by viewModels()
    private lateinit var bind: FragmentEditGameBinding

    private lateinit var firstPlayerSpinnerAdapter: ArrayAdapter<String>
    private lateinit var secondPlayerSpinnerAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = FragmentEditGameBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initFirstPlayerSpinner()
        initSecondPlayerSpinner()

        initListeners()
        observeViewModel()
    }

    private fun observeViewModel() {
        observeFlow(vm.viewState, ::render)
        observeFlow(vm.event, ::event)
    }

    private fun event(event: EditGameViewModel.EditGameEvent?) {
        when (event) {
            is EditGameViewModel.EditGameEvent.CloseFragment ->
                findNavController().popBackStack()
            is EditGameViewModel.EditGameEvent.Error -> {
                Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun render(viewState: EditGameViewModel.EditGameViewState?) {
        when (viewState) {
            is EditGameViewModel.EditGameViewState.SelectedEditGameModel -> {
                bind.editGameFragmentFirstPlayerScore.setText(viewState.selectedModel.firstPlayerScore.toString())
                bind.editGameFragmentSecondPlayerScore.setText(viewState.selectedModel.secondPlayerScore.toString())

                updateFirstPlayerSpinner(viewState.playersListNames, viewState.selectedModel.firstPlayer.name)
                updateSecondPlayerSpinner(viewState.playersListNames, viewState.selectedModel.secondPlayer.name)
            }
            is EditGameViewModel.EditGameViewState.PlayersList -> {
                updateFirstPlayerSpinner(viewState.playersListNames)
                updateSecondPlayerSpinner(viewState.playersListNames)
            }
        }
    }

    private fun updateSecondPlayerSpinner(playerList: List<String>, selectedPlayerName: String = "") {
        secondPlayerSpinnerAdapter.clear()
        secondPlayerSpinnerAdapter.addAll(playerList)
        secondPlayerSpinnerAdapter.notifyDataSetChanged()
        bind.editGameFragmentSecondPlayersSpinner.setSelection(playerList.indexOf(selectedPlayerName))
    }

    private fun updateFirstPlayerSpinner(playerList: List<String>, selectedPlayerName: String = "") {
        firstPlayerSpinnerAdapter.clear()
        firstPlayerSpinnerAdapter.addAll(playerList)
        firstPlayerSpinnerAdapter.notifyDataSetChanged()
        bind.editGameFragmentFirstPlayersSpinner.setSelection(playerList.indexOf(selectedPlayerName))
    }

    private fun initSecondPlayerSpinner() {
        secondPlayerSpinnerAdapter = ArrayAdapter(requireContext(), R.layout.spinner_custom, mutableListOf())
        bind.editGameFragmentSecondPlayersSpinner.adapter = secondPlayerSpinnerAdapter

        bind.editGameFragmentSecondPlayersSpinner.onItemSelectedListener =
            (object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    vm.onSecondPlayerSelect(position)
                }
            })
    }

    private fun initFirstPlayerSpinner() {
        firstPlayerSpinnerAdapter = ArrayAdapter(requireContext(), R.layout.spinner_custom, mutableListOf())
        bind.editGameFragmentFirstPlayersSpinner.adapter = firstPlayerSpinnerAdapter

        bind.editGameFragmentFirstPlayersSpinner.onItemSelectedListener =
            (object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    vm.onFirstPlayerSelect(position)
                }
            })
    }

    private fun initListeners() {
        bind.editGameFragmentFirstPlayerScore.doOnTextChange { vm.onFirstPlayerScoreChange(it) }
        bind.editGameFragmentSecondPlayerScore.doOnTextChange { vm.onSecondPlayerScoreChange(it) }
        bind.editGameFragmentSave.setOnClickListener { vm.onSaveClick() }
        bind.editGameFragmentCancel.setOnClickListener { vm.onCancelClick() }
    }
}
