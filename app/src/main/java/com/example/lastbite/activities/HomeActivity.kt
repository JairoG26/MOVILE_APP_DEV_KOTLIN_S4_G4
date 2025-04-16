package com.example.lastbite.activities

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import com.example.lastbite.CartFragment
import com.example.lastbite.HomeFragment
import com.example.lastbite.NetworkChangeReceiver
import com.example.lastbite.NoInternetFragment
import com.example.lastbite.R

class HomeActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private var networkReceiver: NetworkChangeReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        setContentView(R.layout.activity_home)

        bottomNavigationView = findViewById(R.id.bottomNavigation)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.homeIcon -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.cartIcon -> {
                    replaceFragment(CartFragment())
                    true
                } else -> false
            }
        }

        networkReceiver = NetworkChangeReceiver { isConnected ->
            if (!isConnected) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_nav_container, NoInternetFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }

        registerReceiver(
            networkReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )

        replaceFragment(HomeFragment())

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frame_nav_container, fragment).commit()
    }

}