package com.example.tmg_test.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tmg_test.R
import com.example.tmg_test.ui.base.BaseDialogFragment
import com.example.tmg_test.utils.SORT_TYPE_GAMES
import com.example.tmg_test.utils.SORT_TYPE_WINS
import kotlinx.android.synthetic.main.dialog_sort_type.*

class SortTypeDialog(
    var selectedSortType: String,
    val savedCallback: (savedSortType: String) -> Unit
) : BaseDialogFragment() {

    private var newSelectedSortType: String = selectedSortType

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_sort_type, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initView()
    }

    private fun initView() {
        vSortTypeDialogToggleGroup.check(
            if(selectedSortType == SORT_TYPE_WINS)
                R.id.vSortTypeDialogWinsCheck
            else
                R.id.vSortTypeDialogGamesCheck
        )
    }

    private fun initListeners() {
        vSortTypeDialogWinsCheck.setOnClickListener { newSelectedSortType = SORT_TYPE_WINS }
        vSortTypeDialogGamesCheck.setOnClickListener { newSelectedSortType = SORT_TYPE_GAMES }

        vSortTypeDialogImageClose.setOnClickListener { dismiss() }
        vSortTypeDialogSave.setOnClickListener {
            savedCallback(newSelectedSortType)
            dismiss()
        }
        vSortTypeDialogButtonClose.setOnClickListener { dismiss() }
    }
}