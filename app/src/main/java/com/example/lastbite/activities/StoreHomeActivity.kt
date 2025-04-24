package com.example.lastbite.activities

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.lastbite.AccountFragment
import com.example.lastbite.CartFragment
import com.example.lastbite.NetworkChangeReceiver
import com.example.lastbite.NoInternetFragment
import com.example.lastbite.StoreListFragment
import com.example.lastbite.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class StoreHomeActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private var networkReceiver: NetworkChangeReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        setContentView(R.layout.activity_store_home)

        bottomNavigationView = findViewById(R.id.bottomNavigationStore)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.homeIcon -> {
                    replaceFragment(StoreListFragment())
                    true
                }
                R.id.navigationIcon -> {
                    replaceFragment(AccountFragment())
                    true
                } else -> false
            }
        }

        networkReceiver = NetworkChangeReceiver { isConnected ->
            if (!isConnected) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_store_nav_container, NoInternetFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }

        registerReceiver(
            networkReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )

        replaceFragment(StoreListFragment())

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frame_store_nav_container, fragment).commit()
    }
}