package com.example.tmg_test.repository.impl

import android.content.Context
import android.graphics.drawable.Drawable
import com.example.tmg_test.repository.ResourceRepository

class ResourceRepositoryImpl(val context: Context) : ResourceRepository {
    override fun getString(resId: Int): String = context.getString(resId)
    override fun getColor(resId: Int): Int = context.resources.getColor(resId)
    override fun getDrawable(resId: Int): Drawable = context.resources.getDrawable(resId)
}

