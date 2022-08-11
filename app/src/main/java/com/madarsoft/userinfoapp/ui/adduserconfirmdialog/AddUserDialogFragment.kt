package com.madarsoft.userinfoapp.ui.adduserconfirmdialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

class AddUserDialogFragment : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle("Confirm saving user data")
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                navigateBackToAddUserPage()
            })
            .setNegativeButton("Cancel", null)
            .create()

    private fun navigateBackToAddUserPage() {
        setFragmentResult("add_user_request", bundleOf("add_result" to "confirm_add"))

        findNavController().popBackStack()
    }
}