package com.example.tmg_test.ui.main.players.addPlayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.tmg_test.R
import com.example.tmg_test.ui.base.BaseFragment
import com.example.tmg_test.utils.getText
import com.example.tmg_test.utils.observeFlow
import kotlinx.android.synthetic.main.fragment_add_player.*
import kotlinx.coroutines.flow.collectLatest

class AddPlayerFragment : BaseFragment() {

    val vm: AddPlayerVM by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeViewModel()
        initListeners()
    }

    private fun observeViewModel(){
        observeFlow(vm.event, ::event)
    }

    private fun event(event: AddPlayerVM.AddPlayerEvent?) {
        when(event){
            is AddPlayerVM.AddPlayerEvent.CloseFragment -> findNavController().popBackStack()
            is AddPlayerVM.AddPlayerEvent.Error ->
                Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initListeners() {
        vAddPlayerFragmentSave.setOnClickListener { vm.onSaveClick(vAddPlayerFragmentName.getText()) }
        vAddPlayerFragmentCancel.setOnClickListener { vm.onCancelClick() }
    }
}