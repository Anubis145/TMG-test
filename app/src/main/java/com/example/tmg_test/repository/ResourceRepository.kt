package com.example.tmg_test.repository

import android.graphics.drawable.Drawable

interface ResourceRepository {

    fun getString(resId: Int): String
    fun getColor(resId: Int): Int
    fun getDrawable(resId: Int): Drawable

}
