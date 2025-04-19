package com.example.lastbite
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lastbite.models.Store
import com.example.lastbite.models.StoreAdapter
import com.example.lastbite.viewmodels.AuthViewModel
import com.example.lastbite.viewmodels.StoreViewModel
import com.example.lastbite.viewmodels.UserStoreViewModel

class StoreListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StoreAdapter

    private val authViewModel: AuthViewModel by viewModels()
    private val userStoreViewModel: UserStoreViewModel by viewModels()
    private val storeViewModel: StoreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_store_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerViewStores1)
        adapter = StoreAdapter(emptyList()) { store ->
            Log.d("DEBUG", "Tiendas recibidas: ${store}")
            goToStoreDetail(store)
        }
        Log.d("DEBUG", "Tiendas recibidas:")
        recyclerView.adapter = adapter
        Log.d("DEBUG", "Tiendas recibidas:1")
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        Log.d("DEBUG", "Tiendas recibidas:2")

        // Paso 1: Observar usuario logueado
        authViewModel.user.observe(viewLifecycleOwner) { user ->
            Log.d("DEBUG", "Tiendas recibidas: ${user}")
            user?.let {
                // Paso 2: Pedir los IDs de tiendas asociadas a este usuario
                userStoreViewModel.fetchStoreIdsByUser(user.user_id)
            }
        }

        // Paso 3: Cuando ya tengamos los IDs, pedimos las tiendas
        userStoreViewModel.storeIds.observe(viewLifecycleOwner) { ids ->
            storeViewModel.fetchStoresByIds(ids)
        }

        // Paso 4: Observar tiendas cargadas y mostrar en el RecyclerView
        storeViewModel.storesByUser.observe(viewLifecycleOwner) { storeList ->
            Log.d("DEBUG", "Tiendas recibidas: ${storeList.size}")
            adapter = StoreAdapter(storeList) { store ->
                goToStoreDetail(store)
            }
            recyclerView.adapter = adapter
        }

    }

    private fun goToStoreDetail(store: Store) {
        Toast.makeText(requireContext(), "Tienda seleccionada: ${store.name}", Toast.LENGTH_SHORT).show()
        // Aquí podrías navegar a otro fragment si lo deseas
    }
}