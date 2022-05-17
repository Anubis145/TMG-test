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

class SortTypeDialog : BaseDialogFragment() {

    private val leaderListVM: LeaderListViewModel by activityViewModels()
    private lateinit var bind: DialogSortTypeBinding

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
    }

    private fun initListeners() {
        bind.sortTypeDialogWinsCheck.setOnClickListener { leaderListVM.onSortTypeChange(SORT_TYPE_WINS) }
        bind.sortTypeDialogGamesCheck.setOnClickListener { leaderListVM.onSortTypeChange(SORT_TYPE_GAMES) }

        bind.sortTypeDialogImageClose.setOnClickListener { dismiss() }
        bind.sortTypeDialogSave.setOnClickListener {
            leaderListVM.onSortTypeSave()
            dismiss()
        }
        bind.sortTypeDialogButtonClose.setOnClickListener { dismiss() }
    }
}
