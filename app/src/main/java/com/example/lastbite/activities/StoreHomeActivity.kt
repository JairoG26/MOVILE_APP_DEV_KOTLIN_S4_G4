package com.example.lastbite.activities

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.lastbite.fragments.AccountFragment
import com.example.lastbite.NetworkChangeReceiver
import com.example.lastbite.fragments.NoInternetFragment
import com.example.lastbite.fragments.StoreListFragment
import com.example.lastbite.R
import com.example.lastbite.databinding.ActivityStoreHomeBinding

class StoreHomeActivity : AppCompatActivity() {

    // private lateinit var bottomNavigationView: BottomNavigationView
    private var networkReceiver: NetworkChangeReceiver? = null
    private lateinit var binding : ActivityStoreHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        binding = ActivityStoreHomeBinding.inflate(layoutInflater)
        //setContentView(R.layout.activity_store_home)
        setContentView(binding.root)

        // bottomNavigationView = findViewById(R.id.bottomNavigationStore)
        binding.bottomNavigationStore.setOnItemSelectedListener { menuItem ->
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