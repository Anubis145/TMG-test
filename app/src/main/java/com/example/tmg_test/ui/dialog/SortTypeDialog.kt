package com.example.tmg_test.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tmg_test.R
import com.example.tmg_test.databinding.DialogSortTypeBinding
import com.example.tmg_test.ui.base.BaseDialogFragment
import com.example.tmg_test.utils.SORT_TYPE_GAMES
import com.example.tmg_test.utils.SORT_TYPE_WINS

class SortTypeDialog(
    var selectedSortType: String,
    val savedCallback: (savedSortType: String) -> Unit
) : BaseDialogFragment() {

    private var newSelectedSortType: String = selectedSortType
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
            if (selectedSortType == SORT_TYPE_WINS)
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
            savedCallback(newSelectedSortType)
            dismiss()
        }
        bind.sortTypeDialogButtonClose.setOnClickListener { dismiss() }
    }
}
