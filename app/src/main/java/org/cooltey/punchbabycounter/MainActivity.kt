package org.cooltey.punchbabycounter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import org.cooltey.punchbabycounter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    fun interface OnBackPressedListener {
        fun onBackPressed()
    }

    private lateinit var binding: ActivityMainBinding
    var onBackPressedListener: OnBackPressedListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.findNavController()
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_profile))
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        onBackPressedListener?.onBackPressed() ?: super.onBackPressed()
    }

    fun goToProfileTab() {
        binding.navView.selectedItemId = R.id.navigation_profile
    }
}