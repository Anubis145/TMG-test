package com.example.tmg_test.ui.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.tmg_test.repository.GamesRepository
import com.example.tmg_test.repository.LocalRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
abstract class BaseDialogFragment : DialogFragment() {
    @Inject
    lateinit var localRepository: LocalRepository


    override fun onResume() {
        super.onResume()
        val inset = InsetDrawable(ColorDrawable(Color.TRANSPARENT), 56)
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawable(inset)
    }
}