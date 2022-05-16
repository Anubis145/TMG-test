package com.example.tmg_test.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tmg_test.R
import com.example.tmg_test.databinding.ActivityMainBinding
import com.example.tmg_test.ui.base.BaseActivity

class MainActivity : BaseActivity() {

    val vm: MainViewModel by viewModels()
    lateinit var bind: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        bind.mainActivityBottomnav.setupWithNavController(findNavController(R.id.nav_host_fragment))
    }
}
