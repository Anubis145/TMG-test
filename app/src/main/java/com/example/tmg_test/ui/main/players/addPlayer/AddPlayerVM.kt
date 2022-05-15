package com.example.tmg_test.ui.main.players.addPlayer

import androidx.lifecycle.viewModelScope
import com.example.tmg_test.R
import com.example.tmg_test.model.PlayerModel
import com.example.tmg_test.repository.PlayersRepository
import com.example.tmg_test.repository.ResourceRepository
import com.example.tmg_test.repository.SchedulersRepository
import com.example.tmg_test.ui.base.BaseViewModel
import com.example.tmg_test.utils.emitFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPlayerVM @Inject constructor(
    private val schedulersRepository: SchedulersRepository,
    private val playersRepository: PlayersRepository,
    private val resourceRepository: ResourceRepository,
) : BaseViewModel() {

    private val _event = MutableSharedFlow<AddPlayerEvent>()
    val event = _event.asSharedFlow()

    private var compositeDisposable: CompositeDisposable? = null

    init {
        compositeDisposable = CompositeDisposable()
    }

    fun onSaveClick(name: String) {
        if(!isFieldsValid(name)) return

        val newPlayerModel = PlayerModel(
            id = 0,
            name = name
        )

        val disposable = playersRepository.insert(newPlayerModel)
            .compose(schedulersRepository.singleTransformer())
            .subscribe({
                viewModelScope.launch{
                    _event.emit(AddPlayerEvent.CloseFragment)
                }
            }, ::error)
        compositeDisposable?.add(disposable)
    }

    fun onCancelClick() = viewModelScope.launch {
        _event.emit(AddPlayerEvent.CloseFragment)
    }

    private fun error(t: Throwable) = viewModelScope.launch {
        _event.emit(AddPlayerEvent.Error(t.message))
    }

    private fun isFieldsValid(name: String): Boolean {
        var result = true

        if(name.isEmpty()){
            result = false
            emitFlow(_event, AddPlayerEvent.Error(resourceRepository.getString(R.string.error_name_not_valid)))
        }

        return result
    }

    override fun onCleared() {
        super.onCleared()
        if(compositeDisposable != null){
            compositeDisposable?.clear()
            compositeDisposable = null
        }
    }


    sealed class AddPlayerEvent{
        object CloseFragment : AddPlayerEvent()
        data class Error(var message: String?) : AddPlayerEvent()
    }
}