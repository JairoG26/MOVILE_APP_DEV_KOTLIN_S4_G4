package com.example.lastbite.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lastbite.models.Store
import com.example.lastbite.repositories.StoreRepository

class Top1StoreViewModel {

    private var _stateGetTop3Stores = MutableLiveData<Boolean>()
    val stateGetTop3Stores : LiveData<Boolean> = _stateGetTop3Stores
    private var _top1Store = MutableLiveData<Store>()
    val top1Store : LiveData<Store> = _top1Store
    private var storeRepository = StoreRepository()

    fun getTop1Store(user_id : Int?) {

        if (user_id == null) {
            Log.d("GetTop1StoreViewModel", "The User_ID is null")
        } else {
            storeRepository.getTop1Store(user_id, { callback, top1Store ->
                _stateGetTop3Stores.value = callback
                _top1Store.value = top1Store
            })
        }
    }
}