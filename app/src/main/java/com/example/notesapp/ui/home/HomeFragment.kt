package com.example.notesapp.ui.home

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesapp.BaseFragment
import com.example.notesapp.R
import com.example.notesapp.Utils.Utils
import com.example.notesapp.Utils.isBiometricsSupported
import com.example.notesapp.Utils.showPermissionAlertDialog
import com.example.notesapp.databinding.FragmentHomeBinding
import com.example.notesapp.repository.Notes
import com.example.notesapp.viewModel.HomeViewModel
import java.util.concurrent.Executor

class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: HomeAdapter

    private var permissionDialog: AlertDialog? = null

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

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

        binding.toolbar.toolbarHome.title = "Notes"
        binding.toolbar.toolbarHome.inflateMenu(R.menu.home_menu)

        adapter = HomeAdapter(ArrayList()) { note ->
            navigateToEditNote(note)
        }
        binding.rvList.adapter = adapter
        binding.rvList.layoutManager = LinearLayoutManager(requireContext())


        binding.toolbar.toolbarHome.setOnMenuItemClickListener { menuItem ->
            val id = menuItem.itemId
            if (id == R.id.action_open_locked_notes) {
                if (requireActivity().isBiometricsSupported()) {
                    checkCompatibility()
                }
            }
            true
        }
    }

    private fun checkCompatibility() {
        val biometricManager = BiometricManager.from(requireContext())
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> authenticate()
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                Toast.makeText(
                    requireContext(),
                    "No biometric features available on this device", Toast.LENGTH_SHORT
                ).show()
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Toast.makeText(
                    requireContext(),
                    "Biometric features are currently unavailable", Toast.LENGTH_SHORT
                ).show()
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> showDialog()
            else -> {
                Toast.makeText(
                    requireContext(),
                    "Biometrics not accessible", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun authenticate() {
        executor = ContextCompat.getMainExecutor(requireContext())
        biometricPrompt = BiometricPrompt(requireActivity(), executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(requireContext(), "$errString", Toast.LENGTH_SHORT).show()
                    // settingsViewModel.updateFaceIdSwitch(false)
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.authentication_succeeded), Toast.LENGTH_SHORT
                    ).show()
                    navigateToLockedNotes()
                    // settingsViewModel.updateFaceIdSwitch(true)
                    // permissionViewModel.biometricAuthenticationDone()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.authentication_failed), Toast.LENGTH_SHORT
                    ).show()
//                    settingsViewModel.updateFaceIdSwitch(false)
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Please Authenticate")
            .setSubtitle("Unlock with fingerprint")
            .setNegativeButtonText("Use Password")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    fun showDialog() {
        if (permissionDialog == null) {
            permissionDialog = showPermissionAlertDialog {
                cancelable = false

                title.text = "Permission Needed"
                desc.text = "Please allow us to use Biometric Authentication"
                doneButton.text = "Yes"
                closeButton.text = "No"

                closeIconClickListener {
                    //settingsViewModel.updateFaceIdSwitch(false)
                    //   permissionViewModel.dialogClose()
                }

                doneIconClickListener {
                    try {
                        if (requireContext().isBiometricsSupported()) {
                            val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                                putExtra(
                                    Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                                    android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG
                                )
                            }
                            startActivityForResult(enrollIntent, FACE_ID_REQUEST_CODE)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.biometric_not_accessible), Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: ActivityNotFoundException) {
                        Log.e(javaClass.simpleName, "exception: $e")
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.biometric_not_accessible), Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        Log.e(javaClass.simpleName, "exception: $e")
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.biometric_not_accessible), Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        permissionDialog?.show()
    }


    override fun setupObservers() {
        /*viewModel.isLoading.observe(viewLifecycleOwner) {
            isLoading(it)
        }*/
        binding.fabAdd.setOnClickListener {
            navigateToEditNote()
        }

        viewModel.unLockedNotes.observe(viewLifecycleOwner) {
            adapter.mList = it
            adapter.notifyDataSetChanged()
            if (it.isEmpty()) {
                binding.message = "No Notes Found!"
            } else {
                binding.message = ""
            }
        }

        /*  viewModel.error().observe(viewLifecycleOwner, EventObserver { error ->
              if (errorDialog != null && errorDialog?.isShowing == true) {
                  return@EventObserver
              }
              binding.message = showErrorDialog(error)
          })*/
    }

    private fun navigateToEditNote(note: Notes? = null) {
        val action =
            HomeFragmentDirections.actionHomeFragmentToEditNoteFragment(note, false)
        navigate(action)
    }

    private fun navigateToLockedNotes() {
        val action =
            HomeFragmentDirections.actionHomeFragmentToLockedNotesFragment()
        navigate(action)
    }

    companion object {
        private const val FACE_ID_REQUEST_CODE = 100
    }
}