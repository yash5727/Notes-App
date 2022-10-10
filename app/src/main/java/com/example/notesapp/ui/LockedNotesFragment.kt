package com.example.notesapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesapp.BaseFragment
import com.example.notesapp.R
import com.example.notesapp.databinding.FragmentHomeBinding
import com.example.notesapp.repository.Notes
import com.example.notesapp.ui.home.HomeAdapter
import com.example.notesapp.viewModel.HomeViewModel

class LockedNotesFragment : BaseFragment(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!::binding.isInitialized) {
            binding = FragmentHomeBinding.inflate(inflater, container, false)

            setupViews()
        }

        setupObservers()
        return binding.root
    }

    private fun setupViews() {
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        binding.toolbar.toolbarHome.setupWithNavController(
            findNavController(),
            appBarConfiguration
        )

        binding.toolbar.toolbarHome.setNavigationIcon(R.drawable.ic_back_arrow)
        binding.toolbar.toolbarHome.title = "Locked Notes"

        adapter = HomeAdapter(ArrayList()) { note ->
            navigateToEditNote(note)
        }
        binding.rvList.adapter = adapter
        binding.rvList.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun setupObservers() {
        binding.fabAdd.setOnClickListener {
            navigateToEditNote()
        }

        viewModel.lockedNotes.observe(viewLifecycleOwner) {
            adapter.mList = it
            adapter.notifyDataSetChanged()
            if (it.isEmpty()) {
                binding.message = "No Notes Found!"
            } else {
                binding.message = ""
            }
        }
    }

    private fun navigateToEditNote(note: Notes? = null) {
        val action =
            LockedNotesFragmentDirections.actionLockedNotesFragmentToEditNoteFragment(note, true)
        navigate(action)
    }
}