package com.example.ambient_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FavoritesFragment : Fragment() {

    // View Model for Database
    private val viewModel: FavoritesViewModel by viewModels {
        FavoritesViewModelFactory((activity?.application as Application).favoritesRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)
        // Set up recycler view
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = FavoritesAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity?.baseContext)
        viewModel.allEntries.observe(viewLifecycleOwner) {entry -> entry.let{adapter.submitList(it)}}

        return view
    }

    // Handler for FAB
    fun addNew(view: View) {
        val addDialog = AddDialog()
        addDialog.show(requireActivity().supportFragmentManager, getString(R.string.button_add))
    }
}