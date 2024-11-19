package com.example.servigo

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), HomeFragment.OnDrawerToggleListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationViewDrawer: NavigationView
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationViewDrawer = findViewById(R.id.nav_view_drawer)
        bottomNavigationView = findViewById(R.id.bottom_navigation)

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
                R.id.transactionHistoryDrawer -> {
                    navController.navigate(R.id.transactionHistory)
                }
                R.id.logoutDrawer -> {
                    // Clear the backstack and navigate to login
                    navController.popBackStack(R.id.loginPage, false)  // Clear the backstack
                    navController.navigate(R.id.loginPage)
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
                R.id.transactionHistory -> navController.navigate(R.id.transactionHistory)
            }
            true
        }

        // Listen for destination changes
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val navhostfragmentView = findViewById<View>(R.id.nav_host_fragment)
            when (destination.id) {
                R.id.loginPage, R.id.signupPage, R.id.topupPage -> {

                    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                        v.setPadding(0, 0, 0, 0)
                        insets
                    }

                    // LOCK DRAWERLAYOUT
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

                    // Hide the BottomNavigationView
                    bottomNavigationView.z = -1f // Push it behind
                    bottomNavigationView.animate()
                        .translationY(bottomNavigationView.height.toFloat()) // Move it down
                        .setDuration(300)
                        .start()

                    // Remove padding for fullscreen
                    navhostfragmentView.setPadding(0, 0, 0, 0)

                }
                else -> {
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
}







