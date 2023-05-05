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

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val logger: Logger by lazy {
        JitsuLogger.getInstance(
            endpoint = "http://$LOCAL_HOST:8000/",
            apiKey = "s2s.iluf8ek9uulm4pzmw8spf1.lpihxlbcu6pz5tdlygfkkd"
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logger.logEvent("name" to "MainActivity.onCreate")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navController.addOnDestinationChangedListener { _, navDestination, _ ->
            val destination = navDestination.label
            Log.d("MainActivity", navDestination.toString())
            Log.d("MainActivity", "Destination changed to $destination")
            logger.logEvent(
                "name" to "MainActivity.onDestinationChanged",
                "destination" to destination,
            )
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

    private companion object {
        /**
         * 10.0.2.2 is the Android emulator's alias to localhost, see the docs on
         * [Network address space](https://developer.android.com/studio/run/emulator-networking.html#networkaddresses).
         */
        private const val LOCAL_HOST = "10.0.2.2"
    }
}