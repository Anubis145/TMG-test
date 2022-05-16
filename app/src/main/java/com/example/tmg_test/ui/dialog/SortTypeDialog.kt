package com.example.tmg_test.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.tmg_test.R
import com.example.tmg_test.databinding.DialogSortTypeBinding
import com.example.tmg_test.ui.base.BaseDialogFragment
import com.example.tmg_test.ui.main.leaderList.LeaderListViewModel
import com.example.tmg_test.utils.SORT_TYPE_GAMES
import com.example.tmg_test.utils.SORT_TYPE_WINS

class SortTypeDialog() : BaseDialogFragment() {

    private val leaderListVM: LeaderListViewModel by activityViewModels()
    private var newSelectedSortType: String = SORT_TYPE_WINS
    lateinit var bind: DialogSortTypeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = DialogSortTypeBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initView()
    }

    private fun initView() {
        bind.sortTypeDialogToggleGroup.check(
            if (localRepository.leaderSortType == SORT_TYPE_WINS)
                R.id.sortTypeDialogWinsCheck
            else
                R.id.sortTypeDialogGamesCheck
        )
    }

    private fun initListeners() {
        bind.sortTypeDialogWinsCheck.setOnClickListener { newSelectedSortType = SORT_TYPE_WINS }
        bind.sortTypeDialogGamesCheck.setOnClickListener { newSelectedSortType = SORT_TYPE_GAMES }

        bind.sortTypeDialogImageClose.setOnClickListener { dismiss() }
        bind.sortTypeDialogSave.setOnClickListener {
            leaderListVM.onSortTypeChange(newSelectedSortType)
            dismiss()
        }
        bind.sortTypeDialogButtonClose.setOnClickListener { dismiss() }
    }
}
