package com.example.tmg_test.ui.main.players.addPlayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.tmg_test.databinding.FragmentAddPlayerBinding
import com.example.tmg_test.ui.base.BaseFragment
import com.example.tmg_test.utils.extension.getText
import com.example.tmg_test.utils.observeFlow

class AddPlayerFragment : BaseFragment() {

    val vm: AddPlayerViewModel by viewModels()
    private lateinit var bind: FragmentAddPlayerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = FragmentAddPlayerBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeViewModel()
        initListeners()
    }

    private fun observeViewModel() {
        observeFlow(vm.event, ::event)
    }

    private fun event(event: AddPlayerViewModel.AddPlayerEvent?) {
        when (event) {
            is AddPlayerViewModel.AddPlayerEvent.CloseFragment -> findNavController().popBackStack()
            is AddPlayerViewModel.AddPlayerEvent.Error ->
                Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initListeners() {
        bind.addPlayerFragmentSave.setOnClickListener { vm.onSaveClick(bind.addPlayerFragmentName.getText()) }
        bind.addPlayerFragmentCancel.setOnClickListener { vm.onCancelClick() }
    }
}
