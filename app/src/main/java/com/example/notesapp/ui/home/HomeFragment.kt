package com.example.notesapp.ui.home

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
import com.example.notesapp.viewModel.HomeViewModel

class HomeFragment : BaseFragment(R.layout.fragment_home) {

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
        binding.toolbarSearch.toolbar.setupWithNavController(
            findNavController(),
            appBarConfiguration
        )

        //binding.toolbarSearch.toolbar.setNavigationIcon(R.drawable.ic_back_arrow)
        //binding.toolbarSearch.svUsername.visibility = View.GONE
        binding.toolbarSearch.toolbar.title = "Notes"

        adapter = HomeAdapter(ArrayList()) { id ->
            navigateToEditNote(id)
        }
        binding.rvList.adapter = adapter
        binding.rvList.layoutManager = LinearLayoutManager(requireContext())

       // viewModel.addNote(Notes(title = "2", description = "b", isLocked = false))
        adapter.notifyDataSetChanged()
    }

    override fun setupObservers() {
        /*viewModel.isLoading.observe(viewLifecycleOwner) {
            isLoading(it)
        }*/

        viewModel.notes.observe(viewLifecycleOwner) {
            adapter.mList = it
            adapter.notifyDataSetChanged()
        }

        /*  viewModel.error().observe(viewLifecycleOwner, EventObserver { error ->
              if (errorDialog != null && errorDialog?.isShowing == true) {
                  return@EventObserver
              }
              binding.message = showErrorDialog(error)
          })*/
    }

    private fun navigateToEditNote(noteId: Int) {
        /*  val action =
              ListProfileFragmentDirections.actionListProfileFragmentToSearchProfileFragment(userName)
          navigate(action)*/
    }
}