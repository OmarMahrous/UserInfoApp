package com.madarsoft.userinfoapp.ui.adduser

import android.os.Bundle
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.madarsoft.userinfoapp.R
import com.madarsoft.userinfoapp.data.User
import com.madarsoft.userinfoapp.databinding.FragmentAddUserBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.lang.Exception

@AndroidEntryPoint // Require this annotation because it has its own view model
class AddUserFragment : Fragment(R.layout.fragment_add_user) {

    private val viewModel: AddUserViewModel by viewModels()


    private lateinit var binding: FragmentAddUserBinding

    private lateinit var nameTextWatcher: TextWatcher
    private lateinit var ageTextWatcher: TextWatcher
    private lateinit var jobTitleTextWatcher: TextWatcher

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAddUserBinding.bind(view)

        binding.apply {

            observeTextInputs(this)

            setupGenderDropDownComponent(this)

            saveBtn.setOnClickListener {
                navigateToConfirmationDialog()
            }
        }

        checkConfirmationDialog()

        observeAddUserEvent()

    }

    private fun navigateToConfirmationDialog() {
        val action = AddUserFragmentDirections.actionAddUserFragmentToAddUserDialogFragment()
        findNavController().navigate(action)
    }

    /**
     * Check if user is confirmed saving user data
     * then, insert user data
     */
    private fun checkConfirmationDialog() {
        setFragmentResultListener("add_user_request") { _, bundle ->

            val result = bundle.getString("add_result")

            if (result == "confirm_add") {
                viewModel.saveData()
            }
        }
    }

    private fun observeTextInputs(binding: FragmentAddUserBinding) {
        binding.apply {
            nameTextWatcher = nameEditText.addTextChangedListener {
                viewModel.userName = it.toString()
            }

            ageTextWatcher = ageEditText.addTextChangedListener {
                viewModel.userAge = it.toString()
            }

            jobTitleTextWatcher = jobTitleEditText.addTextChangedListener {
                viewModel.userJobTitle = it.toString()
            }
        }
    }

    private fun setupGenderDropDownComponent(fragmentAddUserBinding: FragmentAddUserBinding) {

        val genderList = resources.getStringArray(R.array.gender_array)

        val listAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            genderList
        )

        fragmentAddUserBinding.genderAutocompleteTextview.setAdapter(listAdapter)

        fragmentAddUserBinding.genderAutocompleteTextview.setOnItemClickListener { adapterView, view, position, l ->


            viewModel.userGender = genderList[position]
        }
    }


    private fun observeAddUserEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addUserEventFlow.collect { event ->
                when (event) {
                    is AddUserViewModel.AddUserEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_SHORT).show()
                    }

                    is AddUserViewModel.AddUserEvent.UpdateUIOnUserInserted -> {
                        Snackbar.make(
                            requireView(),
                            "User created successfully !",
                            Snackbar.LENGTH_SHORT
                        ).show()

                        showDetailsButton(event.user)
                    }
                }
            }
        }
    }

    private fun showDetailsButton(user: User) {
        try {
            binding.apply {
                showDetailsBtn.visibility = View.VISIBLE
                showDetailsBtn.setOnClickListener {
                    navigateToDetailsPage(user)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun navigateToDetailsPage(user: User) {

        val action = AddUserFragmentDirections.actionAddUserFragmentToUserDetailsFragment(user)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearTextInputsListeners()
    }

    private fun clearTextInputsListeners() {
        try {
            binding.apply {
                nameEditText.removeTextChangedListener(nameTextWatcher)
                ageEditText.removeTextChangedListener(ageTextWatcher)
                jobTitleEditText.removeTextChangedListener(jobTitleTextWatcher)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}