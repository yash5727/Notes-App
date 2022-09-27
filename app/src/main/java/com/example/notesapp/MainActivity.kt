package com.example.notesapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import com.example.notesapp.viewModel.HomeViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_main)

        // setUpViews()

        //viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
    }

    /*private fun setUpViews() {
        val navHostFragment = supportFragmentManager.findFragmentById(androidx.navigation.R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }*/
}