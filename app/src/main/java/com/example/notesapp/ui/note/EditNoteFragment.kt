package com.example.notesapp.ui.note

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.notesapp.BaseFragment
import com.example.notesapp.R
import com.example.notesapp.Utils.Utils
import com.example.notesapp.databinding.FragmentEditNoteBinding
import com.example.notesapp.repository.Notes
import com.example.notesapp.viewModel.HomeViewModel
import java.util.logging.LogManager


class EditNoteFragment : BaseFragment(R.layout.fragment_edit_note) {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentEditNoteBinding
    private val args: EditNoteFragmentArgs by navArgs()
    private var isLocked = false

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
        isLocked = args.isLocked

        if (args.note == null) {
            val menuItemDelete = binding.toolbar.menu.findItem(R.id.action_delete)
            menuItemDelete.isVisible = false
        }
        val menuItemLock = binding.toolbar.menu.findItem(R.id.action_lock)
        setLockStatus(menuItemLock)

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            val id = menuItem.itemId
            if (id == R.id.action_delete) {
                binding.note?.let { note ->
                    note.id?.let { id ->
                        Utils.showDialog(
                            context = requireContext(),
                            overrideThemeResId = com.google.android.material.R.style.Theme_MaterialComponents_Light_Dialog_Alert,
                            title = "Delete Note",
                            description = "Are you sure you want to delete this note?",
                            isCancelable = true,
                            titleOfPositiveButton = "Yes",
                            positiveButtonFunction = {
                                viewModel.deleteNote(id)
                                findNavController().popBackStack()
                            },
                            titleOfNegativeButton = "No"
                        ).show()
                    }
                }
            } else if (id == R.id.action_lock) {
                isLocked = !isLocked
                setLockStatus(menuItem)
            }
            true
        }
    }

    private fun setLockStatus(menuItem: MenuItem) {
        if (isLocked) {
            menuItem.icon = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_lock
            )
        } else {
            menuItem.icon = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_unlock
            )
        }
        requireActivity().invalidateMenu()
    }

    override fun onPause() {
        super.onPause()

        val note = binding.note
        if (note?.id != null && note.id > 0) {
            note.title = binding.etTitle.text.toString().trim()
            note.isLocked = isLocked
            note.description = binding.etDescription.text.toString().trim()
            viewModel.updateNote(note)
        } else {
            if (binding.etTitle.text.toString().trim().isNotEmpty()
                && binding.etDescription.text.toString().trim().isNotEmpty()
            ) {
                viewModel.addNote(
                    Notes(
                        title = binding.etTitle.text.toString().trim(),
                        description = binding.etDescription.text.toString().trim(),
                        isLocked = isLocked
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