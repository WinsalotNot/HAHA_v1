package com.example.HAHA

import android.content.Context
import android.hardware.biometrics.BiometricManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.HAHA.Data.PayoutData
import com.example.HAHA.Fragments.HomeFragment
import com.example.HAHA.ViewModel.UIViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity(), HomeFragment.OnDrawerToggleListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationViewDrawer: NavigationView
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navController: NavController
    private var canNavigateToJobPosting = false
    private var lastToastTime: Long = 0
    private val toastCooldownTime: Long = 1000 // 500 milliseconds (0.5 seconds)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val uiViewModel: UIViewModel = ViewModelProvider(this).get(UIViewModel::class.java)
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationViewDrawer = findViewById(R.id.nav_view_drawer)
        bottomNavigationView = findViewById(R.id.bottom_navigation)

        uiViewModel.availability.observe(this) {
            canNavigateToJobPosting = if (it.lowercase() == "not available") {
                true
            } else {
                false
            }
        }

        // Initialize NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Set up ActionBarDrawerToggle
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        // Set up NavigationView with NavController
        navigationViewDrawer.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.logoutDrawer -> {
                    navController.popBackStack(R.id.loginPage, false)
                    navController.navigate(R.id.loginPage)
                }
                R.id.withdraw_wallet -> {
                    navController.navigate(R.id.withdrawFragment)
                }
                R.id.privacy -> {
                    navController.navigate(R.id.privacyFragment)
                }
                else -> {
                    navController.navigate(R.id.homeFragment2)
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // Set up BottomNavigationView with NavController
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment2 -> navController.navigate(R.id.homeFragment2)
                R.id.leaderboardFragment -> navController.navigate(R.id.ranking_page)
                R.id.jobPosting ->
                    if (canNavigateToJobPosting) {
                        navController.navigate(R.id.jobPosting)
                    } else {
                        showToastIfNeeded("You Have Posted! Currently Only 1 Post Per Person")
                    }
                R.id.transactionHistory -> navController.navigate(R.id.transactionHistory)
                R.id.chatFragment -> navController.navigate(R.id.chatListFragment)
            }
            true
        }

        // Listen for destination changes
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val navhostfragmentView = findViewById<View>(R.id.nav_host_fragment)
            when (destination.id) {
                R.id.loginPage, R.id.signupPage, R.id.topupPage, R.id.detailsFragment, R.id.withdrawFragment, R.id.detailHistoryFragment -> {

                    // LOCK DRAWERLAYOUT
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

                    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                        v.setPadding(0, 0, 0, 0)
                        insets
                    }

                    // Hide the BottomNavigationView
                    bottomNavigationView.z = -1f // Push it behind
                    bottomNavigationView.animate()
                        .translationY(bottomNavigationView.height.toFloat()) // Move it down
                        .setDuration(300)
                        .start()

                    // Remove padding for fullscreen
                    navhostfragmentView.setPadding(0, 0, 0, 0)

                }
                R.id.homeFragment2 -> {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)  // Unlock when necessary

                    // Restore padding and the BottomNavigationView visibility for other fragments
                    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                        v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                        insets
                    }

                    // Add Padding back to maintain the bottom space
                    navhostfragmentView.setPadding(0, 0, 0, 60)

                    // Bring the BottomNavigationView back into view
                    bottomNavigationView.z = 1f // Bring it back to the foreground
                    bottomNavigationView.animate()
                        .translationY(0f) // Reset translation
                        .setDuration(300)
                        .start()
                }
                else -> {
                    // LOCK DRAWERLAYOUT
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

                    // Restore padding and the BottomNavigationView visibility for other fragments
                    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                        v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                        insets
                    }

                    // Add Padding back to maintain the bottom space
                    navhostfragmentView.setPadding(0, 0, 0, 60)

                    // Bring the BottomNavigationView back into view
                    bottomNavigationView.z = 1f // Bring it back to the foreground
                    bottomNavigationView.animate()
                        .translationY(0f) // Reset translation
                        .setDuration(300)
                        .start()
                }
            }
        }
}


    override fun onDrawerToggle() {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START)
        } else {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    private fun showToastIfNeeded(message: String) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastToastTime > toastCooldownTime) {
            // Show the Toast and update lastToastTime
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            lastToastTime = currentTime
        }
    }

    fun showLoading() {
        findViewById<ProgressBar>(R.id.loadingWheel).isIndeterminate = false
        findViewById<ConstraintLayout>(R.id.loadingTint).visibility = View.VISIBLE
        findViewById<ProgressBar>(R.id.loadingWheel).visibility = View.VISIBLE
    }

    fun hideLoading() {
        findViewById<ProgressBar>(R.id.loadingWheel).isIndeterminate = false
        findViewById<ConstraintLayout>(R.id.loadingTint).visibility = View.GONE
        findViewById<ProgressBar>(R.id.loadingWheel).visibility = View.GONE
    }

    fun bottomnavpicker(which : Int) {
        bottomNavigationView.selectedItemId = which
    }
}

