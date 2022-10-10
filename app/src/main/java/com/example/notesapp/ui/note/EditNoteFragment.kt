package com.example.notesapp.ui.note

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.notesapp.BaseFragment
import com.example.notesapp.R
import com.example.notesapp.databinding.FragmentEditNoteBinding
import com.example.notesapp.repository.Notes
import com.example.notesapp.viewModel.HomeViewModel


class EditNoteFragment : BaseFragment(R.layout.fragment_edit_note) {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentEditNoteBinding
    private val args: EditNoteFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!::binding.isInitialized) {
            binding = FragmentEditNoteBinding.inflate(inflater, container, false)

            setupViews()
        }

        setupObservers()
        return binding.root
    }

    private fun setupViews() {
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        binding.toolbar.setupWithNavController(
            findNavController(),
            appBarConfiguration
        )

        binding.toolbar.setNavigationIcon(R.drawable.ic_back_arrow)
        binding.toolbar.title = "Note"
        binding.toolbar.inflateMenu(R.menu.edit_menu)

        binding.note = args.note
    }

    override fun onPause() {
        super.onPause()

        val note = binding.note
        if (note?.id != null && note.id > 0) {
            note.title = binding.etTitle.text.toString().trim()
            note.description = binding.etDescription.text.toString().trim()
            viewModel.updateNote(note)
        } else {
            if(binding.etTitle.text.toString().trim().isNotEmpty()
                && binding.etDescription.text.toString().trim().isNotEmpty()){
                viewModel.addNote(
                    Notes(
                        title = binding.etTitle.text.toString().trim(),
                        description = binding.etDescription.text.toString().trim(),
                        isLocked = false
                    )
                )
            }

        }

    }

    override fun setupObservers() {
        /*viewModel.isLoading.observe(viewLifecycleOwner) {
            isLoading(it)
        }*/
/*
        viewModel._singleNote.observe(viewLifecycleOwner) {
            binding.note = it
        }*/

        /*  viewModel.error().observe(viewLifecycleOwner, EventObserver { error ->
              if (errorDialog != null && errorDialog?.isShowing == true) {
                  return@EventObserver
              }
              binding.message = showErrorDialog(error)
          })*/
    }
}