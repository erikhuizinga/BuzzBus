package com.github.erikhuizinga.buzzbus

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.github.erikhuizinga.buzzbus.analytics.JitsuLogger
import com.github.erikhuizinga.buzzbus.analytics.Logger
import com.github.erikhuizinga.buzzbus.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val logger: Logger by lazy {
        JitsuLogger.getInstance(
            endpoint = URL("http://localhost:8000/"),
            apiKey = "android.iluf8ek9uulm4pzmw8spf1.792sasq4vhc68a7rtk3hpq"
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logger.logEvent("MainActivity.onCreate")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navController.addOnDestinationChangedListener { _, navDestination, _ ->
            val destination = navDestination.label
            Log.d("MainActivity", navDestination.toString())
            Log.d("MainActivity", "Destination changed to $destination")
            logger.logEvent("MainActivity.onDestinationChanged", "destination" to destination)
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}