package com.example.lastbite.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.lastbite.fragments.CartFragment
import com.example.lastbite.fragments.HomeFragment
import com.example.lastbite.R
import com.example.lastbite.adapters.CartAdapter
import com.example.lastbite.viewmodels.CartViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class CartActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var adapter: CartAdapter

    // 1. Usa el ViewModel
    private val cartViewModel: CartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

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
        replaceFragment(CartFragment())
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frame_nav_container, fragment).commit()
    }
}
