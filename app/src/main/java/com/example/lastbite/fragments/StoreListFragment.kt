package com.example.lastbite.fragments
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lastbite.R
import com.example.lastbite.SessionManager
import com.example.lastbite.StoreManager
import com.example.lastbite.models.Store
import com.example.lastbite.adapters.StoreAdapter
import com.example.lastbite.viewmodels.HomeViewModel
import com.example.lastbite.viewmodels.StoreViewModel
import com.example.lastbite.viewmodels.UserStoreViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class StoreListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StoreAdapter

    private val userStoreViewModel: UserStoreViewModel by viewModels()
    private val storeViewModel: StoreViewModel by viewModels()
    // private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_store_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var fabAddStore = view.findViewById<FloatingActionButton>(R.id.fabAddStore)

        fabAddStore.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame_store_nav_container, CreateStoreFragment()) // Usa el ID del contenedor en tu Activity
                .addToBackStack(null) // Para que el usuario pueda volver atrás
                .commit()
        }

        recyclerView = view.findViewById(R.id.recyclerViewStores)
        adapter = StoreAdapter(emptyList()) { store, _ ->
            Log.d("StoreListFragment", "The fetched store is the following: ${store}")
            goToStoreDetail(store)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        // Paso 1: Observar usuario logueado
        val currentUser = SessionManager.getUser()
        currentUser?.let {
            userStoreViewModel.fetchStoreIdsByUser(it.user_id)
            Log.d("StoreListFragment", "The user used for retrieving the store IDs is $it.")
            adapter.updateUserId(it.user_id)
        }

        // Paso 3: Cuando ya tengamos los IDs, pedimos las tiendas
        userStoreViewModel.storeIds.observe(viewLifecycleOwner) { ids ->
            storeViewModel.fetchStoresByIds(ids)
        }

        // Paso 4: Observar tiendas cargadas y mostrar en el RecyclerView
        storeViewModel.storesByUser.observe(viewLifecycleOwner) { storeList ->
            Log.d("StoreListFragment", "The total of fetched stores is ${storeList.size}.")
            adapter = StoreAdapter(storeList) { store, _ ->
                goToStoreDetail(store)
            }
            recyclerView.adapter = adapter
        }

    }

    private fun goToStoreDetail(store: Store) {
        val storeProductFragment = StoreProductFragment()
        StoreManager.storeId = store.store_id
        val bundle = Bundle().apply {
            putInt("storeId", store.store_id!!) // Guardamos el ID como Int
        }
        storeProductFragment.arguments = bundle

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame_store_nav_container, storeProductFragment) // Usa el ID del contenedor en tu Activity
            .addToBackStack(null) // Para que el usuario vuelva a atrás
            .commit()
    }
}