package com.example.lastbite.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.lastbite.R
import com.example.lastbite.SessionManager
import com.example.lastbite.databinding.ActivityTop1storeBinding
import com.example.lastbite.viewmodels.Top1StoreViewModel
import com.example.lastbite.models.Store

class Top1StoreActivity : AppCompatActivity() {

    private lateinit var binding : ActivityTop1storeBinding
    private var top1StoreViewModel = Top1StoreViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        //setContentView(R.layout.activity_auth)
        binding = ActivityTop1storeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentUser = SessionManager.getUser()
        currentUser?.let {
            top1StoreViewModel.getTop1Store(currentUser.user_id)
        }

        val top1Store = Observer<Store> { store ->
            binding.textView10.text = store.nit
            binding.textView11.text = store.name
            binding.textView12.text = store.address
        }

        top1StoreViewModel.top1Store.observe(this, top1Store)
    }
}