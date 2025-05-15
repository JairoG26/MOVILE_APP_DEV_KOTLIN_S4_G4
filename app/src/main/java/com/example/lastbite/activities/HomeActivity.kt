package com.example.lastbite.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import com.example.lastbite.fragments.AccountFragment
import com.example.lastbite.fragments.CartFragment
import com.example.lastbite.fragments.HomeFragment
import com.example.lastbite.R
import com.example.lastbite.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        //setContentView(R.layout.activity_home)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // bottomNavigationView = findViewById(R.id.bottomNavigation)
        bottomNavigationView = binding.bottomNavigation

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.homeIcon -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.cartIcon -> {
                    replaceFragment(CartFragment())
                    true
                }
                    R.id.navigationIcon -> {
                    replaceFragment(AccountFragment())
                    true
                } else -> false
            }
        }

        replaceFragment(HomeFragment())

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frame_nav_container, fragment).commit()
    }

}